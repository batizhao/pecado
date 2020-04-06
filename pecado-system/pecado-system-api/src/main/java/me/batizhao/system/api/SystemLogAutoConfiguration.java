package me.batizhao.system.api;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author batizhao
 * @since 2020-04-01
 **/
@ConditionalOnWebApplication
@Configuration(proxyBeanMethods = false)
@ComponentScan("me.batizhao.system.api")
@EnableAsync
public class SystemLogAutoConfiguration {
}
