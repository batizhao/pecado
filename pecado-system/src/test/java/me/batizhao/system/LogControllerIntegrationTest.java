package me.batizhao.system;

import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.exception.WebExceptionHandler;
import me.batizhao.common.core.util.ResultEnum;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author batizhao
 * @since 2020-02-29
 */
@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = PecadoSystemApplication.class)
@AutoConfigureMockMvc
@Import(WebExceptionHandler.class)
@Slf4j
public class LogControllerIntegrationTest {

    public static final String USERNAME = "admin";
    public static final String PASSWORD = "123456";
    public static final String CLIENT_ID = "client_app2";
    public static final String CLIENT_SECRET = "12345678";
    public static final String GRANT_TYPE = "password";
    static String access_token;

    @Autowired
    MockMvc mvc;

    @BeforeClass
    public static void setUp() {
        access_token = obtainAccessToken(USERNAME, PASSWORD);
    }

    static String obtainAccessToken(String username, String password) {
        String url = "http://localhost:4000/oauth/token";
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(CLIENT_ID, CLIENT_SECRET);

        MultiValueMap<String, Object> parammap = new LinkedMultiValueMap<>();
        parammap.add("grant_type", GRANT_TYPE);
        parammap.add("username", username);
        parammap.add("password", password);
        HttpEntity<Map> entity = new HttpEntity<>(parammap, headers);

        String result = new RestTemplate().postForObject(url, entity, String.class);

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(result).get("access_token").toString();
    }

    @Test
    public void givenUserId_whenFindRoles_thenSuccess() throws Exception {
        mvc.perform(get("/log")
                .header("Authorization", "Bearer " + access_token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data", hasSize(2)));
    }
}