package me.batizhao.system.util.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.batizhao.common.core.util.ResultEnum;
import me.batizhao.ims.api.feign.UserFeignService;
import me.batizhao.ims.api.vo.RoleVO;
import me.batizhao.system.api.annotation.SystemLog;
import me.batizhao.system.api.dto.LogDTO;
import me.batizhao.system.service.LogService;
import me.batizhao.system.web.LogController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author batizhao
 * @since 2020-02-10
 */
@WebMvcTest(LogController.class)
public class LogControllerUnitTest extends BaseControllerUnitTest {

    @Autowired
    private MockMvc mvc;

    /**
     * 所有实现的接口都要 Mock
     */
    @MockBean
    private LogService logService;
    @MockBean
    private UserFeignService userFeignService;

    @Test
    @WithMockUser
    public void givenUserId_whenFindRole_thenRoleJsonArray() throws Exception {
        LogDTO logDTO = new LogDTO().setDescription("根据用户ID查询角色").setSpend(20).setClassMethod("findRolesByUserId")
                .setClassName("me.batizhao.ims.web.RoleController").setClientId("client_app").setHttpRequestMethod("POST")
                .setIp("127.0.0.1").setTime(new Date()).setUrl("http://localhost:5000/role").setUsername("test");

        when(logService.save(any())).thenReturn(true);

        mvc.perform(post("/log").with(csrf())
                .content(new ObjectMapper().writeValueAsString(logDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data", equalTo(true)));

        verify(logService).save(any());
    }
}