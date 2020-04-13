package me.batizhao.system.api;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author batizhao
 * @since 2020-04-01
 **/
@ConditionalOnWebApplication
@Configuration(proxyBeanMethods = false)
@ComponentScan("me.batizhao.system.api")
@EnableAsync
public class SystemAutoConfiguration {
}
