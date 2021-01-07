package me.batizhao.common.sentinel.feign;

import com.alibaba.cloud.sentinel.feign.SentinelFeignAutoConfiguration;
import feign.Feign;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * 暂时使用 PecadoSentinelFeign 覆盖 Sentinel 中的配置，等待 Alibaba 升级
 * @author batizhao
 * @date 2021/1/6
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(SentinelFeignAutoConfiguration.class)
public class PecadoSentinelFeignAutoConfiguration {

    @Bean
    @Scope("prototype")
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "feign.sentinel.enabled")
    public Feign.Builder feignSentinelBuilder() {
        return PecadoSentinelFeign.builder();
    }

}
