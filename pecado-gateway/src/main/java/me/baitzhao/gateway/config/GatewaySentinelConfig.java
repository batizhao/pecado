package me.baitzhao.gateway.config;

import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.util.ResponseInfo;
import me.batizhao.common.core.util.ResultEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author batizhao
 * @since 2020-04-13
 **/
@Configuration
@ConditionalOnProperty("spring.cloud.sentinel.transport.dashboard")
@Slf4j
public class GatewaySentinelConfig {

    @Bean
    public BlockRequestHandler blockRequestHandler() {
        return (serverWebExchange, throwable) -> {
            ResponseInfo<String> message = new ResponseInfo<String>().setCode(ResultEnum.TOO_MANY_REQUEST.getCode())
                    .setMessage(ResultEnum.TOO_MANY_REQUEST.getMessage())
                    .setData(throwable.getMessage());

            log.error("Sentinel Gateway Block Exception", throwable);

            return ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(message));
        };
    }

}
