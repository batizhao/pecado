package me.baitzhao.gateway.exception;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.util.R;
import me.batizhao.common.core.util.ResultEnum;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.WebProperties.Resources;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
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
@Slf4j
public class GatewayExceptionHandler extends DefaultErrorWebExceptionHandler {

    public GatewayExceptionHandler(ErrorAttributes errorAttributes, Resources resources, ErrorProperties errorProperties, ApplicationContext applicationContext) {
        super(errorAttributes, resources, errorProperties, applicationContext);
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
    protected Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Throwable throwable = super.getError(request);

        //所有网关异常都返回这个 code
        Integer code = ResultEnum.GATEWAY_ERROR.getCode();
        //这里 data 是 HTTP 状态码
        int data = HttpStatus.INTERNAL_SERVER_ERROR.value();
        String message = new R<String>(ResultEnum.GATEWAY_ERROR.getCode()).getMessage();

        if(throwable instanceof BlockException) {
            data = HttpStatus.TOO_MANY_REQUESTS.value();
            message = new R<String>(ResultEnum.TOO_MANY_REQUEST.getCode()).getMessage();
        } else if (throwable instanceof ResponseStatusException) {
            ResponseStatusException responseStatusException = (ResponseStatusException) throwable;
            data = responseStatusException.getStatus().value();
            message = responseStatusException.getMessage();
        }

        log.error("Gateway Exception", throwable);

        Map<String, Object> errorAttributes = new LinkedHashMap<>();
        errorAttributes.put("code", code);
        errorAttributes.put("data", data);
        errorAttributes.put("message", message);

        return errorAttributes;
    }
}
