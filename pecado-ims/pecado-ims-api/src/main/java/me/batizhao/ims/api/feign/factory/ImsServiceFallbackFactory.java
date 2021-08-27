package me.batizhao.ims.api.feign.factory;

import me.batizhao.ims.api.feign.fallback.ImsServiceFallbackImpl;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author batizhao
 * @since 2020-04-10
 **/
@Component
public class ImsServiceFallbackFactory implements FallbackFactory<ImsServiceFallbackImpl> {

    @Override
    public ImsServiceFallbackImpl create(Throwable throwable) {
        ImsServiceFallbackImpl imsServiceFallback = new ImsServiceFallbackImpl();
        imsServiceFallback.setThrowable(throwable);
        return imsServiceFallback;
    }
}
