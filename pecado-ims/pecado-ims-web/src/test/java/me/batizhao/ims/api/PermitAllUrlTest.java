package me.batizhao.ims.api;

import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.security.annotation.Inner;
import me.batizhao.common.security.component.PermitAllUrlProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.NameValueExpression;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

/**
 * 测试放行URL
 *
 * @author batizhao
 * @since 2020-04-20
 **/
@EnableConfigurationProperties(value = PermitAllUrlProperties.class)
@Slf4j
public class PermitAllUrlTest extends BaseApiTest {

    @Autowired
    private PermitAllUrlProperties permitAllUrlProperties;

    @Test
    void whenGetNacosYamlAntUrls_thenHaveItems() {
        List<String> ants = permitAllUrlProperties.getAnt().getUrls();
        log.info("ant path: {}", ants);
        assertThat(ants.size(), is(2));
        assertThat(ants, hasItems("/actuator/**", "/v2/api-docs"));

        List<String> regex = permitAllUrlProperties.getRegex().getUrls();
        log.info("regex path: {}", regex);
        assertThat(regex.size(), is(1));
        assertThat(regex, hasItems("\\/user\\?username\\=\\w+"));
    }
}
