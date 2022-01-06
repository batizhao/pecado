package me.baitzhao.gateway.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.baitzhao.gateway.exception.GatewayExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自定义网关异常
 * {@link org.springframework.boot.autoconfigure.web.reactive.error.ErrorWebFluxAutoConfiguration}.
 *
 * @author batizhao
 * @since 2020-04-15
 **/
@Configuration(proxyBeanMethods = false)
public class GatewayErrorConfig {

//    @Bean
//    public GatewayExceptionHandler gatewayExceptionHandler(ObjectMapper objectMapper) {
//        return new GatewayExceptionHandler(objectMapper);
//    }

}
