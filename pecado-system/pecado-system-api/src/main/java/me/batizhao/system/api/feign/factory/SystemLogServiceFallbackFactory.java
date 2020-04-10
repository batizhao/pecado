package me.batizhao.system.api.feign.factory;

import feign.hystrix.FallbackFactory;
import me.batizhao.system.api.feign.fallback.SystemLogServiceFallbackImpl;
import org.springframework.stereotype.Component;

/**
 * @author batizhao
 * @since 2020-04-10
 **/
@Component
public class SystemLogServiceFallbackFactory implements FallbackFactory<SystemLogServiceFallbackImpl> {

    @Override
    public SystemLogServiceFallbackImpl create(Throwable throwable) {
        SystemLogServiceFallbackImpl systemLogServiceFallback = new SystemLogServiceFallbackImpl();
        systemLogServiceFallback.setThrowable(throwable);
        return systemLogServiceFallback;
    }
}
