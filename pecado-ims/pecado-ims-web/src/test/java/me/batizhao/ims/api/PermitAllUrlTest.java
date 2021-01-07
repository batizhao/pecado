package me.batizhao.ims.api;

import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.security.component.PermitAllUrlProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * 测试放行URL
 *
 * @author batizhao
 * @since 2020-04-20
 **/
@EnableConfigurationProperties(value = PermitAllUrlProperties.class)
@Slf4j
@DirtiesContext
public class PermitAllUrlTest extends BaseApiTest {

    @Autowired
    private PermitAllUrlProperties permitAllUrlProperties;

    @Test
    void whenGetNacosYamlAntUrls_thenHaveItems() {
        List<String> ants = permitAllUrlProperties.getAnt().getUrls();
        log.info("ant path: {}", ants);
        assertThat(ants.size(), is(6));
        assertThat(ants, hasItems("/actuator/**", "/v2/api-docs"));

        List<String> regex = permitAllUrlProperties.getRegex().getUrls();
        log.info("regex path: {}", regex);
        assertThat(regex.size(), is(1));
        assertThat(regex, hasItems("\\/user\\?username\\=\\w+"));
    }
}
