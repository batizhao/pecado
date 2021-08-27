package me.batizhao.uaa.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.util.ResultEnum;
import me.batizhao.ims.api.domain.LoginDTO;
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

    @Autowired
    MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void givenNoPassword_whenGetAccessToken_thenError() throws Exception {
        LoginDTO loginDTO = new LoginDTO().setUsername("admin");
        mvc.perform(post("/token")
                .content(objectMapper.writeValueAsString(loginDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.PARAMETER_INVALID.getCode()))
                .andExpect(jsonPath("$.data[0]", containsString("password is not blank")));
    }

//    @Test
//    public void givenNoGrantType_whenGetAccessToken_thenOAuthException() throws Exception {
//        mvc.perform(post("/token")
//                .param("username", USERNAME).param("password", PASSWORD))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.code").value(ResultEnum.OAUTH2_TOKEN_ERROR.getCode()))
//                .andExpect(jsonPath("$.data", containsString("Missing grant type")));
//    }
//
//    @Test
//    public void givenInvalidRefreshToken_whenGetAccessToken_thenOAuthException() throws Exception {
//        String invalidRefreshToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6";
//        mvc.perform(post("/token")
//                .param("grant_type", "refresh_token").param("refresh_token", invalidRefreshToken))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.code").value(ResultEnum.OAUTH2_TOKEN_ERROR.getCode()))
//                .andExpect(jsonPath("$.data", containsString("Cannot convert access token to JSON")));
//    }
}
