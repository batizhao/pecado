package me.batizhao.gateway.integration;

import me.batizhao.common.core.constant.SecurityConstants;
import me.batizhao.common.core.util.ResultEnum;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;

/**
 * @author batizhao
 * @since 2020-04-24
 **/
public class GatewayIntegrationTest extends BaseIntegrationTest {

    @Test
    void givenExistServiceUrl_whenCallGateway_thenOk() {
        webClient.get().uri("/api/ims/users")
                .header("Authorization", adminAccessToken)
                .exchange().expectStatus().isOk()
                .expectHeader()
                .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.code").isEqualTo(ResultEnum.SUCCESS.getCode())
                .jsonPath("$.data.records").value(hasSize(6))
                .jsonPath("$.data.records[0].username").isEqualTo("admin");
    }

    @Test
    void givenExistServiceUrl_whenCallGateway_then401() {
        webClient.get().uri("/api/ims/user?username=bob")
                .header(SecurityConstants.FROM, SecurityConstants.FROM_IN)
                .exchange().expectStatus().isUnauthorized()
                .expectHeader()
                .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.code").isEqualTo(ResultEnum.OAUTH2_TOKEN_INVALID.getCode())
                .jsonPath("$.data").value(containsString("Full authentication is required"));
    }

}
