package me.batizhao.common.security.feign;

import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

/**
 * 业务异常包装类，显示原始异常
 * 包装成 HystrixBadRequestException，不进入熔断逻辑
 * 包装成 PecadoFeignException，进入熔断逻辑
 *
 * @author batizhao
 * @since 2020-03-25
 **/
@Slf4j
@Component
public class PecadoFeignErrorDecoder implements ErrorDecoder {

    @SneakyThrows
    @Override
    public Exception decode(String methodKey, Response response) {
        String json = Util.toString(response.body().asReader(Charset.defaultCharset()));
        Exception exception = new PecadoFeignException(json, response.status());
        log.error("Feign 调用异常，方法：{}, 原始异常：{}", methodKey, json);
        return exception;
    }
}