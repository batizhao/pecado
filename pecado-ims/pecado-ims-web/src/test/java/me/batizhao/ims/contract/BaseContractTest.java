package me.batizhao.ims.contract;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author batizhao
 * @since 2020-03-27
 **/
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
@RefreshScope
public abstract class BaseContractTest {

    @Value("${pecado.token.admin}")
    String adminAccessToken;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setUp(){
        RestAssuredMockMvc.webAppContextSetup(context);
    }

}
