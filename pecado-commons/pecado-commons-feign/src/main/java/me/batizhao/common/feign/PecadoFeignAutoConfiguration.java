package me.batizhao.common.feign;

import feign.Feign;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import me.batizhao.common.core.constant.SecurityConstants;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
//import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.security.oauth2.client.OAuth2ClientContext;
//import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

/**
 * @author batizhao
 * @since 2020-04-17
 **/
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(Feign.class)
public class PecadoFeignAutoConfiguration {

//    @Bean
//    @ConditionalOnBean(OAuth2ClientContext.class)
//    public RequestInterceptor oauth2FeignRequestInterceptor(OAuth2ClientContext oAuth2ClientContext,
//                                                            OAuth2ProtectedResourceDetails resource) {
//        return new OAuth2FeignRequestInterceptor(oAuth2ClientContext, resource){
//            @Override
//            public void apply(RequestTemplate template) {
//                Collection<String> fromHeader = template.headers().get(SecurityConstants.FROM);
//                if (!CollectionUtils.isEmpty(fromHeader) && fromHeader.contains(SecurityConstants.FROM_IN)) {
//                    return;
//                }
//
//                super.apply(template);
//            }
//        };
//    }

}
