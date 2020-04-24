package me.batizhao.gateway.integration;

import me.batizhao.common.core.util.ResultEnum;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.hasSize;

/**
 * @author batizhao
 * @since 2020-04-24
 **/
public class GatewayIntegrationTest extends BaseIntegrationTest {

    @Test
    void givenExistServiceUrl_whenCallGateway_thenOk() {
        webClient.get().uri("/api/ims/user")
                .header("Authorization", adminAccessToken)
                .exchange().expectStatus().isOk()
                .expectHeader()
                .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.code").isEqualTo(ResultEnum.SUCCESS.getCode())
                .jsonPath("$.data").value(hasSize(6))
                .jsonPath("$.data[0].username").isEqualTo("admin");
    }

}
