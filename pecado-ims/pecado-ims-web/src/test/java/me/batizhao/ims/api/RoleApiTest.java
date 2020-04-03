package me.batizhao.ims.api;

import me.batizhao.common.core.util.ResultEnum;
import me.batizhao.system.api.annotation.SystemLog;
import me.batizhao.system.api.aspect.SystemLogAspect;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author batizhao
 * @since 2020-03-18
 **/
public class RoleApiTest extends BaseApiTest {

    @SpyBean
    private SystemLogAspect systemLogAspect;

    @Test
    public void givenUserId_whenFindRoles_thenSuccess() throws Exception {
//        assertTrue(AopUtils.isAopProxy(roleController));
//        assertTrue(AopUtils.isCglibProxy(roleController));

//        assertEquals(AopProxyUtils.ultimateTargetClass(fooService), FooServiceImpl.class);
//
//        assertEquals(AopTestUtils.getTargetObject(fooService).getClass(), FooServiceImpl.class);
//        assertEquals(AopTestUtils.getUltimateTargetObject(fooService).getClass(), FooServiceImpl.class);

        mvc.perform(get("/role").param("userId", "1")
                .header("Authorization", adminAccessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data", hasSize(2)));

        //主线程等待 Async 子线程完成日志写入，否则 @SystemLog 注解会被忽略。这个建议集成测试中使用。
//        Thread.sleep(10000L);

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
