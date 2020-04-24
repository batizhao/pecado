package me.batizhao.gateway.api;

import me.baitzhao.gateway.PecadoGatewayApplication;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.SocketUtils;

import java.time.Duration;

/**
 * @author batizhao
 * @since 2020-02-07
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = PecadoGatewayApplication.class,
        properties = "management.server.port=${test.port}")
@ActiveProfiles("test")
@Tag("api")
public abstract class BaseApiTest {

    protected static int managementPort;

    @LocalServerPort
    protected int port = 0;

    protected WebTestClient webClient;

    protected String baseUri;

    @BeforeAll
    public static void before() {
        managementPort = SocketUtils.findAvailableTcpPort();
        System.setProperty("test.port", String.valueOf(managementPort));
    }

    @BeforeEach
    public void setUp() {
        baseUri = "http://localhost:" + port;
        this.webClient = WebTestClient.bindToServer()
                .responseTimeout(Duration.ofSeconds(10)).baseUrl(baseUri).build();
    }

    @AfterAll
    public static void after()
    {
        System.clearProperty("test.port");
    }
}
