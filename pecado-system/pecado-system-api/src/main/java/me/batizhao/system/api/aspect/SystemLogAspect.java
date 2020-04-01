package me.batizhao.system.api.aspect;

import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.util.SpringContextHolder;
import me.batizhao.system.api.annotation.SystemLog;
import me.batizhao.system.api.dto.LogDTO;
import me.batizhao.system.api.event.SystemLogEvent;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @author batizhao
 * @since 2020-04-01
 **/
@Slf4j
@Aspect
//@Component
public class SystemLogAspect {

    @Around("@annotation(systemLog)")
    @SneakyThrows
    public Object around(ProceedingJoinPoint point, SystemLog systemLog) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        LogDTO log = new LogDTO();
        long startTime = System.currentTimeMillis();
        Object result = point.proceed();
        long endTime = System.currentTimeMillis();
        Signature signature = point.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (method.isAnnotationPresent(ApiOperation.class)) {
            ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
            log.setDescription(apiOperation.value());
        }

        log.setTime(new Date());
        log.setIp(request.getRemoteUser());
        log.setMethod(request.getMethod());
//        log.setParameter(getParameter(method, point.getArgs()).toString());
//        log.setUsername(getUsername());
        log.setResult(result.toString());
        log.setSpendTime((int) (endTime - startTime));
        log.setUrl(request.getRequestURL().toString());

        SpringContextHolder.publishEvent(new SystemLogEvent(log));
        return result;
    }

//    /**
//     * 获取客户端
//     *
//     * @return clientId
//     */
//    private String getClientId() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication instanceof OAuth2Authentication) {
//            OAuth2Authentication auth2Authentication = (OAuth2Authentication) authentication;
//            return auth2Authentication.getOAuth2Request().getClientId();
//        }
//        return null;
//    }
//
//    /**
//     * 获取用户名称
//     *
//     * @return username
//     */
//    private String getUsername() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null) {
//            return null;
//        }
//        return authentication.getName();
//    }
//
//    /**
//     * 获取方法参数
//     *
//     * @param method
//     * @param args
//     * @return
//     */
//    private Object getParameter(Method method, Object[] args) {
//        List<Object> argList = new ArrayList<>();
//        Parameter[] parameters = method.getParameters();
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
//        if (argList.size() == 0) {
//            return null;
//        } else if (argList.size() == 1) {
//            return argList.get(0);
//        } else {
//            return argList;
//        }
//    }

}
