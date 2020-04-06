package me.batizhao.common.security.component;

import com.alibaba.cloud.sentinel.annotation.SentinelRestTemplate;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

/**
 * @author batizhao
 * @since 2020-03-20
 */
@ConfigurationPropertiesScan
@ComponentScan("me.batizhao.common.security")
public class PecadoResourceServerAutoConfiguration {
	@Bean
	@Primary
	@LoadBalanced
	@SentinelRestTemplate
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
