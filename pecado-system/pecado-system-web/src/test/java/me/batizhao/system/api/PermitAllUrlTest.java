package me.batizhao.system.api;

import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.security.component.PermitAllUrlProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

/**
 * @author batizhao
 * @since 2020-04-06
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
        assertThat(ants.size(), is(7));
        assertThat(ants, hasItems("/log", "/actuator/**", "/v2/api-docs"));
    }
}
