package me.batizhao.common.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.batizhao.common.core.jackson.PecadoJavaTimeModule;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.time.ZoneId;
import java.util.Locale;
import java.util.TimeZone;

import static me.batizhao.common.core.constant.PecadoConstants.NORM_DATETIME_PATTERN;

/**
 * @author batizhao
 * @since 2020-04-13
 **/
@Configuration(proxyBeanMethods = false)
@ComponentScan("me.batizhao.common.core")
public class PecadoCoreAutoConfiguration {

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(ObjectMapper.class)
    @AutoConfigureBefore(JacksonAutoConfiguration.class)
    public static class JacksonConfig {

        @Bean
        public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
            return builder -> {
                builder.locale(Locale.CHINA);
                builder.timeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
                builder.simpleDateFormat(NORM_DATETIME_PATTERN);
                builder.modules(new PecadoJavaTimeModule());
            };
        }
    }

}
