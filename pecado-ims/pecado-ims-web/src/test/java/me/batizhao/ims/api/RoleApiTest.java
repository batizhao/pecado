package me.batizhao.ims.api;

import me.batizhao.common.core.util.ResultEnum;
import me.batizhao.system.api.annotation.SystemLog;
import me.batizhao.system.api.aspect.SystemLogAspect;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author batizhao
 * @since 2020-03-18
 **/
@DirtiesContext
public class RoleApiTest extends BaseApiTest {

    @SpyBean
    private SystemLogAspect systemLogAspect;

    @Test
    public void givenUserId_whenFindRoles_thenSuccess() throws Exception {
        mvc.perform(get("/role").param("userId", "1")
                .header("Authorization", adminAccessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data", hasSize(2)));

        verify(systemLogAspect).around(any(ProceedingJoinPoint.class), any(SystemLog.class));
    }

    @Test
    public void givenUserId_whenFindRoles_thenZero() throws Exception {
        mvc.perform(get("/role").param("userId", "3")
                .header("Authorization", adminAccessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data", hasSize(0)));
    }
}
