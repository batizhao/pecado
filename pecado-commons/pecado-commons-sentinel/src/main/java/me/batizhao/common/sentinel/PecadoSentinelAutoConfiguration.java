package me.batizhao.common.sentinel;

import me.batizhao.common.sentinel.exception.PecadoBlockExceptionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author batizhao
 * @since 2020-04-13
 **/
@ConditionalOnWebApplication
@ConditionalOnProperty("spring.cloud.sentinel.transport.dashboard")
public class PecadoSentinelAutoConfiguration {

    @Bean
    PecadoBlockExceptionHandler pecadoBlockExceptionHandler() {
        return new PecadoBlockExceptionHandler();
    }
}
