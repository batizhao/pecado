package me.batizhao.gateway.api;

import me.batizhao.common.core.util.ResultEnum;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.containsString;

/**
 * @author batizhao
 * @since 2020-04-24
 **/
public class GatewayApiTest extends BaseApiTest {

    @Test
    void givenNoExistUrl_whenCallGateway_then404() {
        webClient.get().uri("/api/xxxx").exchange().expectStatus().isNotFound()
                .expectHeader()
                .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.code").isEqualTo(ResultEnum.GATEWAY_ERROR.getCode())
                .jsonPath("$.data").value(containsString("404 NOT_FOUND"))
                .jsonPath("$.message").isEqualTo("网关异常！");
    }

    @Test
    void givenExistServiceUrl_whenCallGateway_then503() {
        webClient.get().uri("/api/ims/users").exchange().expectStatus().is5xxServerError()
                .expectHeader()
                .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.code").isEqualTo(ResultEnum.GATEWAY_ERROR.getCode())
                .jsonPath("$.data").value(containsString("503 SERVICE_UNAVAILABLE"))
                .jsonPath("$.message").isEqualTo("网关异常！");
    }

    @Test
    public void actuatorManagementPort() {
        webClient.get()
                .uri("http://localhost:" + managementPort + "/actuator/gateway/routes")
                .exchange().expectStatus().isOk();
    }
}
