package me.batizhao.gateway.integration;

import me.baitzhao.gateway.PecadoGatewayApplication;
import me.batizhao.common.core.exception.WebExceptionHandler;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.SocketUtils;

import java.time.Duration;

/**
 * @author batizhao
 * @since 2020-03-31
 **/
@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = PecadoGatewayApplication.class)
@Import(WebExceptionHandler.class)
@ActiveProfiles("integration")
@Tag("integration")
public abstract class BaseIntegrationTest {

    @LocalServerPort
    private int port;

    protected WebTestClient webClient;

    protected String baseUri;

    @Value("${pecado.token.admin}")
    String adminAccessToken;

    @BeforeEach
    public void setUp() {
        baseUri = "http://localhost:" + port;
        this.webClient = WebTestClient.bindToServer()
                .responseTimeout(Duration.ofSeconds(10)).baseUrl(baseUri).build();
    }

}
