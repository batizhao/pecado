package me.batizhao.system.api.aspect;

import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.constant.MQConstants;
import me.batizhao.system.api.annotation.SystemLog;
import me.batizhao.system.api.dto.LogDTO;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author batizhao
 * @since 2020-04-01
 **/
@Slf4j
@Aspect
@Component
//@AllArgsConstructor
public class SystemLogAspect {

//    private ApplicationContext applicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Around("@annotation(systemLog)")
    @SneakyThrows
    public Object around(ProceedingJoinPoint point, SystemLog systemLog) {
        LogDTO logDTO = getLogDTO(point, systemLog);

        long startTime = System.currentTimeMillis();
        Object result = point.proceed();
        long endTime = System.currentTimeMillis();

        logDTO.setResult(result.toString());
        logDTO.setSpend((int) (endTime - startTime));

//        applicationContext.publishEvent(new SystemLogEvent(logDTO));
        rocketMQTemplate.syncSend(MQConstants.TOPIC_SYSTEM_LOG, logDTO);
        return result;
    }

    @AfterThrowing(value = "@annotation(systemLog)", throwing = "throwable")
    public void afterThrowing(JoinPoint point, SystemLog systemLog, Throwable throwable) {
        LogDTO logDTO = getLogDTO(point, systemLog);
        logDTO.setResult(throwable.getMessage());
        logDTO.setSpend(0);

        rocketMQTemplate.syncSend(MQConstants.TOPIC_SYSTEM_LOG, logDTO);
//        applicationContext.publishEvent(new SystemLogEvent(logDTO));
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

        HttpServletRequest request = ((ServletRequestAttributes) Objects
                .requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        logDTO.setIp(request.getRemoteAddr());
        logDTO.setHttpRequestMethod(request.getMethod());
        logDTO.setClassName(method.getDeclaringClass().getName());
        logDTO.setClassMethod(method.getName());
        logDTO.setParameter(getParameter(method.getParameters(), point.getArgs()));
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
     * @param parameters 参数名
     * @param args 参数值
     * @return
     */
    @SneakyThrows
    private String getParameter(Parameter[] parameters, Object[] args) {
//        List<Object> argList = new ArrayList<>();
//        for (int i = 0; i < parameters.length; i++) {
//            //将RequestBody注解修饰的参数作为请求参数
//            RequestBody requestBody = parameters[i].getAnnotation(RequestBody.class);
//            if (requestBody != null) {
//                argList.add(args[i]);
//            }
//            //将RequestParam注解修饰的参数作为请求参数
//            RequestParam requestParam = parameters[i].getAnnotation(RequestParam.class);
//            if (requestParam != null) {
//                Map<String, Object> map = new HashMap<>();
//                String key = parameters[i].getName();
//                if (!StringUtils.isEmpty(requestParam.value())) {
//                    key = requestParam.value();
//                }
//                map.put(key, args[i]);
//                argList.add(map);
//            }
//        }
//        if (argList.isEmpty()) {
//            return null;
//        } else if (argList.size() == 1) {
//            return argList.get(0).toString();
//        } else {
//            return argList.toString();
//        }
        if (ArrayUtils.isEmpty(parameters) || ArrayUtils.isEmpty(args)) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            //参数名
            String name = parameters[i].getName();
            //参数值
            Object value = args[i];
            builder.append(name).append("=");
            if (value instanceof String) {
                builder.append(value);
            } else {
                builder.append(objectMapper.writeValueAsString(value));
            }
            if (i < args.length -1) {
                builder.append(", ");
            }
        }

        return builder.toString();
    }

}
