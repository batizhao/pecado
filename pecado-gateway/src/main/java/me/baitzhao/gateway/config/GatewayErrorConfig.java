package me.baitzhao.gateway.config;

import me.baitzhao.gateway.exception.GatewayExceptionHandler;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;

import java.util.Collections;
import java.util.List;

/**
 * 自定义网关异常
 * {@link org.springframework.boot.autoconfigure.web.reactive.error.ErrorWebFluxAutoConfiguration}.
 *
 * @author batizhao
 * @since 2020-04-15
 **/
//@Configuration(proxyBeanMethods = false)
//@EnableConfigurationProperties({ServerProperties.class})
public class GatewayErrorConfig {

//    private final ServerProperties serverProperties;
//    private final ApplicationContext applicationContext;
//    private final WebProperties.Resources resources;
//    private final List<ViewResolver> viewResolvers;
//    private final ServerCodecConfigurer serverCodecConfigurer;
//
//    public GatewayErrorConfig(ServerProperties serverProperties,
//                              WebProperties.Resources resources,
//                              ObjectProvider<List<ViewResolver>> viewResolversProvider,
//                              ServerCodecConfigurer serverCodecConfigurer,
//                              ApplicationContext applicationContext) {
//        this.serverProperties = serverProperties;
//        this.applicationContext = applicationContext;
//        this.resources = resources;
//        this.viewResolvers = viewResolversProvider.getIfAvailable(Collections::emptyList);
//        this.serverCodecConfigurer = serverCodecConfigurer;
//    }
//
//    @Bean
//    @Order(Ordered.HIGHEST_PRECEDENCE)
//    public ErrorWebExceptionHandler errorWebExceptionHandler(ErrorAttributes errorAttributes) {
//        GatewayExceptionHandler exceptionHandler = new GatewayExceptionHandler(errorAttributes,
//                this.resources,
//                this.serverProperties.getError(),
//                this.applicationContext);
//        exceptionHandler.setViewResolvers(this.viewResolvers);
//        exceptionHandler.setMessageWriters(this.serverCodecConfigurer.getWriters());
//        exceptionHandler.setMessageReaders(this.serverCodecConfigurer.getReaders());
//        return exceptionHandler;
//    }

}
