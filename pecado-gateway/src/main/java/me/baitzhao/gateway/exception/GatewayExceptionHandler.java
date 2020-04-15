package me.baitzhao.gateway.exception;

import me.batizhao.common.core.util.ResultEnum;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * 自定义网关异常格式
 *
 * @author batizhao
 * @since 2020-04-14
 **/
public class GatewayExceptionHandler extends DefaultErrorWebExceptionHandler {

    public GatewayExceptionHandler(ErrorAttributes errorAttributes, ResourceProperties resourceProperties, ErrorProperties errorProperties, ApplicationContext applicationContext) {
        super(errorAttributes, resourceProperties, errorProperties, applicationContext);
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return route(RequestPredicates.all(), this::renderErrorResponse);
    }

    /**
     * 状态码从 errorAttributes data 属性中获取
     *
     * @param errorAttributes
     * @return
     */
    @Override
    protected int getHttpStatus(Map<String, Object> errorAttributes) {
        return (int) errorAttributes.get("data");
    }

    @Override
    protected Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {
        int data = 500;
        Integer code = ResultEnum.UNKNOWN_ERROR.getCode();
        String message = ResultEnum.UNKNOWN_ERROR.getMessage();

        ResponseStatusException error = (ResponseStatusException) super.getError(request);
        if (error.getStatus().is4xxClientError()) {
            data = 404;
            code = ResultEnum.RESOURCE_NOT_FOUND.getCode();
            message = ResultEnum.RESOURCE_NOT_FOUND.getMessage();
        }

        Map<String, Object> errorAttributes = new LinkedHashMap<>();

        errorAttributes.put("code", code);
        errorAttributes.put("data", data);
        errorAttributes.put("message", message);

        return errorAttributes;
    }
}
