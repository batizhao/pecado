package me.batizhao.ims.api.feign.factory;

import me.batizhao.ims.api.feign.fallback.UserServiceFallbackImpl;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author batizhao
 * @since 2020-04-10
 **/
@Component
public class UserServiceFallbackFactory implements FallbackFactory<UserServiceFallbackImpl> {

    @Override
    public UserServiceFallbackImpl create(Throwable throwable) {
        UserServiceFallbackImpl systemLogServiceFallback = new UserServiceFallbackImpl();
        systemLogServiceFallback.setThrowable(throwable);
        return systemLogServiceFallback;
    }
}
