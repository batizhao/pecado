package me.batizhao.ims.api;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author batizhao
 * @since 2020-04-01
 **/
@Configuration(proxyBeanMethods = false)
@ComponentScan("me.batizhao.ims.api")
public class ImsAutoConfiguration {
}
