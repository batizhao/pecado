package me.batizhao.system.api;

import lombok.AllArgsConstructor;
import me.batizhao.system.api.aspect.SystemLogAspect;
import me.batizhao.system.api.event.SystemLogListener;
import me.batizhao.system.api.feign.SystemLogFeignService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author batizhao
 * @since 2020-04-01
 **/
@EnableAsync
@AllArgsConstructor
@ConditionalOnWebApplication
@Configuration(proxyBeanMethods = false)
//@ComponentScan("me.batizhao.system.api")
public class LogAutoConfiguration {

    private final SystemLogFeignService systemLogFeignService;

//	@Bean
//	public SystemLogListener systemLogListener() {
//		return new SystemLogListener(systemLogFeignService);
//	}

	@Bean
	public SystemLogAspect logAspect() {
		return new SystemLogAspect(systemLogFeignService);
	}
}
