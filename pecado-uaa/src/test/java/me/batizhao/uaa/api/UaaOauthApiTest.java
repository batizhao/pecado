package me.batizhao.uaa.api;

import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.util.ResultEnum;
import me.batizhao.uaa.PecadoUaaApplication;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * OAuth 安全框架集成测试
 * 这些测试只有 @EnableAuthorizationServer 的情况下有意义
 * 这个类不需要预先获取 access_token，所以不要继承基类
 *
 * @author batizhao
 * @since 2020-03-02
 **/
@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = PecadoUaaApplication.class)
@AutoConfigureMockMvc
@Tag("api")
public class UaaOauthApiTest {

    public static final String USERNAME = "admin";
    public static final String PASSWORD = "123456";
    public static final String CLIENT_ID = "client_app";
    public static final String CLIENT_SECRET = "123456";
    public static final String GRANT_TYPE = "password";

    @Autowired
    MockMvc mvc;

    /**
     * MockMvc 不是一个真正的 servlet server，所以，有时会出现和实际运行不一致的情况。
     * 可以看这个 issues：https://github.com/spring-projects/spring-boot/issues/5574
     *
     * curl -i -X POST --user 'client_app:xxxx' localhost:4000/oauth/token\?grant_type=password\&username=admin\&password=123456
     *
     * {"code":100004,"message":"认证失败！","data":"Full authentication is required to access this resource"}
     *
     * @throws Exception
     */
    @Test
    public void givenErrorSecret_whenGetAccessToken_thenUnauthorized() throws Exception {
        mvc.perform(post("/oauth/token")
                .param("grant_type", GRANT_TYPE).param("username", USERNAME).param("password", PASSWORD)
                .with(httpBasic(CLIENT_ID, "xxxx")))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void givenNoGrantType_whenGetAccessToken_thenOAuthException() throws Exception {
        mvc.perform(post("/oauth/token")
                .param("username", USERNAME).param("password", PASSWORD)
                .with(httpBasic(CLIENT_ID, CLIENT_SECRET)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.OAUTH2_TOKEN_ERROR.getCode()))
                .andExpect(jsonPath("$.data", containsString("Missing grant type")));
    }

    @Test
    public void givenInvalidRefreshToken_whenGetAccessToken_thenOAuthException() throws Exception {
        String invalidRefreshToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6";
        mvc.perform(post("/oauth/token")
                .param("grant_type", "refresh_token").param("refresh_token", invalidRefreshToken)
                .with(httpBasic(CLIENT_ID, CLIENT_SECRET)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.OAUTH2_TOKEN_ERROR.getCode()))
                .andExpect(jsonPath("$.data", containsString("Cannot convert access token to JSON")));
    }
}
