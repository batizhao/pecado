package me.batizhao.common.feign;

import feign.Feign;
import feign.codec.ErrorDecoder;
import me.batizhao.common.feign.exception.PecadoFeignErrorDecoder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author batizhao
 * @since 2020-04-17
 **/
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(Feign.class)
public class PecadoFeignAutoConfiguration {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new PecadoFeignErrorDecoder();
    }

}
