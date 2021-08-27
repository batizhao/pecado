package me.batizhao.uaa.integration;

import me.batizhao.common.core.util.ResultEnum;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author batizhao
 * @since 2020-03-31
 **/
public class UaaOauthIntegrationTest extends BaseIntegrationTest {

    /**
     * 1. 当用一个过期的 refresh token 去请求 accesss token 时，抛出 InvalidTokenException: Invalid refresh token (expired)
     * 2. 当用一个非法的 refresh token 去请求 accesss token 时，抛出 InvalidGrantException: Invalid refresh token
     *
     * 客户端只要处理 100002 状态码，并匹配 Invalid refresh token 消息，重新使用 grant_type=password 进行请求 accesss token
     *
     * @throws Exception
     */
//    @Test
//    public void givenExpiredRefreshToken_whenGetAccessToken_thenOAuthException() throws Exception {
//        String expiredRefreshToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdLCJhdGkiOiIxODE2ZDE1My05M2E3LTQyMGEtYmQ2Mi04ZmE2NzJkYTc4OGUiLCJleHAiOjE1ODU2MTk5MjQsImF1dGhvcml0aWVzIjpbIlJPTEVfQURNSU4iLCJST0xFX1VTRVIiXSwianRpIjoiMGJjMDJlMGQtN2IxMS00MTA2LWFlMzMtNzA5Y2UyMTI1YTVhIiwiY2xpZW50X2lkIjoiY2xpZW50X2FwcCIsInVzZXJuYW1lIjoiYWRtaW4ifQ.o0KS8et7lGvxd93o2hogfQBcW8zY97k-_jdV5k6uJ64";
//        mvc.perform(post("/oauth/token")
//                .param("grant_type", "refresh_token").param("refresh_token", expiredRefreshToken)
//                .with(httpBasic("client_app", "123456")))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.code").value(ResultEnum.OAUTH2_TOKEN_ERROR.getCode()))
//                .andExpect(jsonPath("$.data", containsString("Invalid refresh token (expired)")));
//    }

//    @Test
//    public void givenRefreshToken_whenGetAccessToken_thenWrongClient() throws Exception {
//        mvc.perform(post("/oauth/token")
//                .param("grant_type", "refresh_token").param("refresh_token", adminRefreshToken)
//                .with(httpBasic("client_app", "123456")))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(ResultEnum.OAUTH2_TOKEN_ERROR.getCode()))
//                .andExpect(jsonPath("$.data", containsString("Wrong client for this refresh token")));
//    }
//
//    @Test
//    public void givenRefreshToken_whenGetAccessToken_thenSuccess() throws Exception {
//        mvc.perform(post("/oauth/token")
//                .param("grant_type", "refresh_token").param("refresh_token", adminRefreshToken)
//                .with(httpBasic("test", "passw0rd")))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.token_type", equalTo("bearer")));
//    }

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
    public void givenSecret_whenGetAccessToken_thenSuccess() throws Exception {
        mvc.perform(post("/token")
                .param("username", "admin").param("password", "123456").param("code", "1234"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data", containsString("ey")));
    }
}
