package me.batizhao.common.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @author batizhao
 * @since 2020-03-20
 *
 * <p>
 * 自定义线程池属性
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = "pecado.thread")
public class ThreadPoolProperties {

	private int corePoolSize = 10;

	private int maxPoolSize = 20;

	private int queueCapacity = 200;

	private int keepAliveSeconds = 60;

	private String threadNamePrefix = "PecadoTask-";

	private boolean waitForTasksToCompleteOnShutdown = true;

	private int awaitTerminationSeconds = 60;
}
