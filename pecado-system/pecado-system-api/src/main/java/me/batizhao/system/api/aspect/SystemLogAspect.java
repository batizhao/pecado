package me.batizhao.system.api.aspect;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.system.api.annotation.SystemLog;
import me.batizhao.system.api.dto.LogDTO;
import me.batizhao.system.api.event.SystemLogEvent;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author batizhao
 * @since 2020-04-01
 **/
@Slf4j
@Aspect
@Component
@AllArgsConstructor
public class SystemLogAspect {

    private ApplicationContext applicationContext;

    @Around("@annotation(systemLog)")
    @SneakyThrows
    public Object around(ProceedingJoinPoint point, SystemLog systemLog) {
        long startTime = System.currentTimeMillis();
        Object result = point.proceed();
        long endTime = System.currentTimeMillis();

        LogDTO logDTO = getLogDTO(point, systemLog);
        logDTO.setResult(result.toString());
        logDTO.setSpend((int) (endTime - startTime));

        applicationContext.publishEvent(new SystemLogEvent(logDTO));
        return result;
    }

    @AfterThrowing(value = "@annotation(systemLog)", throwing = "throwable")
    public void afterThrowing(JoinPoint point, SystemLog systemLog, Throwable throwable) {
        LogDTO logDTO = getLogDTO(point, systemLog);
        logDTO.setResult(throwable.getMessage());
        logDTO.setSpend(0);
        applicationContext.publishEvent(new SystemLogEvent(logDTO));
    }

    private LogDTO getLogDTO(JoinPoint point, SystemLog systemLog) {
        Signature signature = point.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        LogDTO logDTO = new LogDTO();

        if (!StringUtils.isEmpty(systemLog.value())) {
            logDTO.setDescription(systemLog.value());
        } else if (method.isAnnotationPresent(ApiOperation.class)) {
            ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
            logDTO.setDescription(apiOperation.value());
        } else {
            logDTO.setDescription("请使用 @ApiOperation 或者 @SystemLog 的 value 属性。");
        }

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();

        logDTO.setIp(request.getRemoteAddr());
        logDTO.setHttpRequestMethod(request.getMethod());
        logDTO.setClassName(method.getDeclaringClass().getName());
        logDTO.setClassMethod(method.getName());
        logDTO.setParameter(getParameter(method, point.getArgs()));
        logDTO.setClientId(getClientId());
        logDTO.setUsername(getUsername());
        logDTO.setUrl(request.getRequestURL().toString());
        logDTO.setCreatedTime(LocalDateTime.now());
        return logDTO;
    }

    /**
     * 获取客户端
     *
     * @return clientId
     */
    private String getClientId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof OAuth2Authentication) {
            OAuth2Authentication auth2Authentication = (OAuth2Authentication) authentication;
            return auth2Authentication.getOAuth2Request().getClientId();
        }
        return null;
    }

    /**
     * 获取用户名称
     *
     * @return username
     */
    private String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        return authentication.getName();
    }

    /**
     * 获取方法参数
     *
     * @param method
     * @param args
     * @return
     */
    private String getParameter(Method method, Object[] args) {
        List<Object> argList = new ArrayList<>();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            //将RequestBody注解修饰的参数作为请求参数
            RequestBody requestBody = parameters[i].getAnnotation(RequestBody.class);
            if (requestBody != null) {
                argList.add(args[i]);
            }
            //将RequestParam注解修饰的参数作为请求参数
            RequestParam requestParam = parameters[i].getAnnotation(RequestParam.class);
            if (requestParam != null) {
                Map<String, Object> map = new HashMap<>();
                String key = parameters[i].getName();
                if (!StringUtils.isEmpty(requestParam.value())) {
                    key = requestParam.value();
                }
                map.put(key, args[i]);
                argList.add(map);
            }
        }
        if (argList.isEmpty()) {
            return null;
        } else if (argList.size() == 1) {
            return argList.get(0).toString();
        } else {
            return argList.toString();
        }
    }

}
