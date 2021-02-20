package me.batizhao.ims.unit.controller;

import me.batizhao.common.core.util.ResultEnum;
import me.batizhao.ims.api.vo.RoleVO;
import me.batizhao.ims.controller.RoleController;
import me.batizhao.ims.service.RoleMenuService;
import me.batizhao.ims.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author batizhao
 * @since 2020-02-10
 */
@WebMvcTest(RoleController.class)
public class RoleControllerUnitTest extends BaseControllerUnitTest {

    @Autowired
    private MockMvc mvc;

    /**
     * 所有实现的接口都要 Mock
     */
    @MockBean
    private RoleService roleService;
    @MockBean
    private RoleMenuService roleMenuService;

    private List<RoleVO> roleList;
//    private Page<RoleVO> rolePageList;

    /**
     * Prepare test data.
     */
    @BeforeEach
    public void setUp() {
        roleList = new ArrayList<>();
        roleList.add(new RoleVO().setId(1L).setName("admin"));
        roleList.add(new RoleVO().setId(2L).setName("common"));
    }

    @Test
    @WithMockUser
    public void givenUserId_whenFindRole_thenRoleJsonArray() throws Exception {
        when(roleService.findRolesByUserId(anyLong())).thenReturn(roleList);

        mvc.perform(get("/role").param("userId", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].name", equalTo("admin")));

        verify(roleService).findRolesByUserId(anyLong());
    }

    @Test
    @WithMockUser
    public void givenUserId_whenFindRole_thenFail() throws Exception {
        roleList.clear();
        when(roleService.findRolesByUserId(anyLong())).thenReturn(roleList);

        mvc.perform(get("/role").param("userId", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data", hasSize(0)));

        verify(roleService).findRolesByUserId(anyLong());
    }

//    @Test
//    @WithMockUser
//    void givenNothing_whenFindRoles_thenSuccess() throws Exception {
//        doReturn(roleList).when(roleService).findRoles();
//
//        mvc.perform(get("/roles"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
//                .andExpect(jsonPath("$.data", hasSize(2)))
//                .andExpect(jsonPath("$.data[0].name", equalTo("admin")));
//    }

    @Test
    @WithMockUser
    public void givenMenus_whenAddRoleMenus_thenSuccess() throws Exception {
        doReturn(true).when(roleMenuService).updateRoleMenus(any(List.class));

        mvc.perform(post("/role/menu").param("id", "1").param("menus", "2,3,4").with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data").value(true));
    }

    /**
     * 删除成功的情况
     *
     * @throws Exception
     */
    @Test
    @WithMockUser
    public void givenId_whenDeleteRole_thenSuccess() throws Exception {
        when(roleService.removeByIds(anyList())).thenReturn(true);

        mvc.perform(delete("/role").param("ids", "1,2").with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data").value(true));

        verify(roleService).removeByIds(anyList());
    }
}