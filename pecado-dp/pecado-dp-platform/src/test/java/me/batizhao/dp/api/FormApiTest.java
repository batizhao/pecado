package me.batizhao.dp.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.batizhao.common.core.util.ResultEnum;
import me.batizhao.dp.domain.Form;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
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
 * @since 2020-02-11
 */
public class FormApiTest extends BaseApiTest {

    @Autowired
    private ObjectMapper objectMapper;

//    @Test
//    public void givenId_whenFindForm_thenSuccess() throws Exception {
//        mvc.perform(get("/form/{id}", 1L)
//                .header("Authorization", adminAccessToken))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()));
//    }

    @Test
    public void givenNothing_whenFindAllForm_thenSuccess() throws Exception {
        mvc.perform(get("/forms")
                .header("Authorization", adminAccessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()));
    }

    @Test
    @Transactional
    public void givenJson_whenSaveForm_thenSuccess() throws Exception {
        Form requestBody = new Form()
                .setName("daxia");

        mvc.perform(post("/form")
                .content(objectMapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", adminAccessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.id", notNullValue()));
    }

    @Test
    @Transactional
    public void givenJson_whenUpdateForm_thenSuccess() throws Exception {
        Form requestBody = new Form()
                .setId(8L).setName("daxia").setFormKey("xxxxx123456");

        mvc.perform(post("/form")
                .content(objectMapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", adminAccessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()));
    }

//    @Test
//    @Transactional
//    public void givenId_whenDeleteForm_thenSuccess() throws Exception {
//        mvc.perform(delete("/form").param("ids", "1,2")
//                .header("Authorization", adminAccessToken))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
//                .andExpect(jsonPath("$.data").value(true));
//    }
}
