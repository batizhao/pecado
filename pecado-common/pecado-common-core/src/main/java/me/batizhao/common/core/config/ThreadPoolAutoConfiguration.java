package me.batizhao.common.core.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;

/**
 * 有时候，主线程退出后，会导致子线程任务也提前退出
 * 这里使用 WaitForTasksToCompleteOnShutdown 和 AwaitTerminationSeconds 实现优雅关闭线程
 *
 * @author batizhao
 * @since 2020-04-04
 **/
@EnableConfigurationProperties(ThreadPoolProperties.class)
@Configuration
@Slf4j
@ConditionalOnProperty(prefix = "pecado.thread", name = "enabled", havingValue = "true")
public class ThreadPoolAutoConfiguration implements AsyncConfigurer {

    @Autowired
    private ThreadPoolProperties threadPoolProperties;

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        BeanUtils.copyProperties(threadPoolProperties, executor);
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new MyAsyncExceptionHandler();
    }

    /**
     * 自定义异常处理类
     */
    static class MyAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

        @Override
        public void handleUncaughtException(Throwable throwable, Method method, Object... obj) {
            log.error("Async Exception message - {}", throwable.getMessage());
            log.error("Async Method name - {}", method.getName());
            for (Object param : obj) {
                log.error("Async Parameter value - {}", param);
            }
        }

    }
}
