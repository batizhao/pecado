package me.batizhao.common.feign.sentinel;

import com.alibaba.cloud.sentinel.SentinelProperties;
import com.alibaba.cloud.sentinel.feign.SentinelFeignAutoConfiguration;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.SentinelWebInterceptor;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.DefaultBlockExceptionHandler;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.UrlCleaner;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.config.SentinelWebMvcConfig;
import feign.Feign;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.util.R;
import me.batizhao.common.feign.sentinel.ext.PecadoSentinelFeign;
import me.batizhao.common.feign.sentinel.handle.PecadoBlockExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * <p>Description: 自定义Sentinel配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/12/4 15:36
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(SentinelFeignAutoConfiguration.class)
@Slf4j
public class SentinelConfiguration {

    @PostConstruct
    public void postConstruct() {
        log.info("[Herodotus] |- Plugin [Herodotus Sentinel] Auto Configure.");
    }

    @Bean
    @Scope("prototype")
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "feign.sentinel.enabled")
    public Feign.Builder feignSentinelBuilder() {
        return PecadoSentinelFeign.builder();
    }

    @Bean
    @ConditionalOnMissingBean
    public BlockExceptionHandler blockExceptionHandler() {
        return new PecadoBlockExceptionHandler();
    }

    /**
     * 限流、熔断统一处理类
     */
    @Configuration
    @ConditionalOnClass(HttpServletRequest.class)
    public static class WebmvcHandler {
        @Bean
        public BlockExceptionHandler webmvcBlockExceptionHandler() {
            return (request, response, e) -> {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                R<String> result = R.failed("Too many request, please retry later.");
                response.getWriter().print(result);
            };
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnProperty(name = "spring.cloud.sentinel.enabled", matchIfMissing = true)
    @EnableConfigurationProperties(SentinelProperties.class)
    public static class SentinelWebConfiguration {

        @Autowired
        private SentinelProperties properties;

        @Autowired
        private Optional<UrlCleaner> urlCleanerOptional;

        @Autowired
        private Optional<BlockExceptionHandler> blockExceptionHandlerOptional;

        @Autowired
        private Optional<RequestOriginParser> requestOriginParserOptional;

        @Bean
        @ConditionalOnProperty(name = "spring.cloud.sentinel.filter.enabled",
                matchIfMissing = true)
        public SentinelWebInterceptor sentinelWebInterceptor(
                SentinelWebMvcConfig sentinelWebMvcConfig) {
            return new SentinelWebInterceptor(sentinelWebMvcConfig);
        }

        @Bean
        @ConditionalOnProperty(name = "spring.cloud.sentinel.filter.enabled",
                matchIfMissing = true)
        public SentinelWebMvcConfig sentinelWebMvcConfig() {
            SentinelWebMvcConfig sentinelWebMvcConfig = new SentinelWebMvcConfig();
            sentinelWebMvcConfig.setHttpMethodSpecify(properties.getHttpMethodSpecify());
            sentinelWebMvcConfig.setWebContextUnify(properties.getWebContextUnify());

            if (blockExceptionHandlerOptional.isPresent()) {
                blockExceptionHandlerOptional
                        .ifPresent(sentinelWebMvcConfig::setBlockExceptionHandler);
            } else {
                if (StringUtils.hasText(properties.getBlockPage())) {
                    sentinelWebMvcConfig.setBlockExceptionHandler(((request, response,
                                                                    e) -> response.sendRedirect(properties.getBlockPage())));
                } else {
                    sentinelWebMvcConfig
                            .setBlockExceptionHandler(new DefaultBlockExceptionHandler());
                }
            }

            urlCleanerOptional.ifPresent(sentinelWebMvcConfig::setUrlCleaner);
            requestOriginParserOptional.ifPresent(sentinelWebMvcConfig::setOriginParser);
            return sentinelWebMvcConfig;
        }
    }
}
