package me.batizhao.ims.web.contract;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import me.batizhao.ims.PecadoImsApplication;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author batizhao
 * @since 2020-03-27
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = PecadoImsApplication.class)
@ActiveProfiles("test")
@RefreshScope
public abstract class BaseContractTest {

    @Value("${pecado.token.admin}")
    String adminAccessToken;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setUp(){
        RestAssuredMockMvc.webAppContextSetup(context);
    }

}
