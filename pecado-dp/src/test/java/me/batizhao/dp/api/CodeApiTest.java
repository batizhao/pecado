package me.batizhao.dp.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.batizhao.common.core.util.ResultEnum;
import me.batizhao.dp.domain.Ds;
import me.batizhao.dp.domain.GenConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * <p>
 * 1. 在 OAuth 开启的情况下，不再需要 @WithMockUser 来模拟用户
 * 2. 这里要注意一个问题，当使用 Spring Security regexMatchers 时，例如 /user?username=admin 这种情况
 * get("/user").param("username", "xx") 会存在匹配不到的情况，要使用 get("/user?username=xx") 来代替
 * 具体可以看这个 Issues：https://github.com/spring-projects/spring-framework/issues/20040
 * </p>
 *
 * @author batizhao
 * @since 2021-01-07
 */
public class CodeApiTest extends BaseApiTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void givenConfig_whenGenerateCode_thenSuccess() throws Exception {
        GenConfig requestBody = new GenConfig().setTableName("ds").setAuthor("batizhao").setComments("comment")
                .setModuleName("system").setPackageName("me.batizhao");

        mvc.perform(post("/code").with(csrf())
                .header("Authorization", adminAccessToken)
                .content(objectMapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Length", "9153"));
    }

}
