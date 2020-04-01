package me.batizhao.system.api.feign.factory;

import feign.hystrix.FallbackFactory;
import me.batizhao.system.api.feign.SystemLogFeignService;
import me.batizhao.system.api.feign.fallback.SystemLogServiceFallbackImpl;
import org.springframework.stereotype.Component;

/**
 * @author batizhao
 * @since 2020-03-13
 **/
@Component
public class SystemLogServiceFallbackFactory implements FallbackFactory<SystemLogFeignService> {

	@Override
	public SystemLogFeignService create(Throwable throwable) {
		SystemLogServiceFallbackImpl logServiceFallback = new SystemLogServiceFallbackImpl();
		logServiceFallback.setCause(throwable);
		return logServiceFallback;
	}
}
