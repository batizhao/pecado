package me.batizhao.system.unit.web;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.batizhao.common.core.util.ResultEnum;
import me.batizhao.system.api.dto.LogDTO;
import me.batizhao.system.domain.Log;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.text.StringContainsInOrder.stringContainsInOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 所有实现的接口都要 Mock
     */
    @MockBean
    private LogService logService;

    private List<Log> logList;
    private IPage<Log> logPageList;

    /**
     * Prepare test data.
     */
    @BeforeEach
    public void setUp() {
        logList = new ArrayList<>();
        logList.add(new Log().setId(1L).setClassMethod("handleMenuTree4Me").setClassName("me.batizhao.ims.web.MenuController"));
        logList.add(new Log().setId(2L).setClassMethod("handleUserInfo").setClassName("me.batizhao.ims.web.UserController"));

        logPageList = new Page<>();
        logPageList.setRecords(logList);
    }

    @Test
    @WithMockUser
    public void givenUserId_whenFindRole_thenRoleJsonArray() throws Exception {
        LogDTO logDTO = new LogDTO().setDescription("根据用户ID查询角色").setSpend(20).setClassMethod("findRolesByUserId")
                .setClassName("me.batizhao.ims.web.RoleController").setClientId("client_app").setHttpRequestMethod("POST")
                .setIp("127.0.0.1").setCreatedTime(LocalDateTime.now()).setUrl("http://localhost:5000/role").setUsername("test");

        when(logService.save(any(Log.class))).thenReturn(true);

        mvc.perform(post("/log").with(csrf())
                .content(objectMapper.writeValueAsString(logDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data", equalTo(true)));

        verify(logService).save(any(Log.class));
    }

    @Test
    @WithMockUser
    public void givenNothing_whenFindAllLogs_thenListJson() throws Exception {
        when(logService.findLogs(any(Page.class), any(Log.class))).thenReturn(logPageList);

        mvc.perform(get("/logs"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(content().string(stringContainsInOrder("handleMenuTree4Me", "handleUserInfo")))
                .andExpect(jsonPath("$.data.records", hasSize(2)))
                .andExpect(jsonPath("$.data.records[1].classMethod", equalTo("handleUserInfo")));

        verify(logService).findLogs(any(Page.class), any(Log.class));
    }
}