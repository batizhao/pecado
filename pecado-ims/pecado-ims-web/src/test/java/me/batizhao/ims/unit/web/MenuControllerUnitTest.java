package me.batizhao.ims.unit.web;

import me.batizhao.common.core.util.ResultEnum;
import me.batizhao.ims.domain.Menu;
import me.batizhao.ims.service.MenuService;
import me.batizhao.ims.web.MenuController;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author batizhao
 * @since 2020-02-10
 */
@WebMvcTest(MenuController.class)
public class MenuControllerUnitTest extends BaseControllerUnitTest {

    @Autowired
    private MockMvc mvc;

    /**
     * 所有实现的接口都要 Mock
     */
    @MockBean
    private MenuService menuService;

    private List<Menu> menuList;

    /**
     * Prepare test data.
     */
    @BeforeEach
    public void setUp() {
        menuList = new ArrayList<>();
        menuList.add(new Menu().setId(1L).setName("工作台").setPath("/dashboard"));
        menuList.add(new Menu().setId(1000L).setName("权限管理").setPath("/ims"));
        menuList.add(new Menu().setId(1100L).setName("用户管理").setPath("/ims/user"));
        menuList.add(new Menu().setId(1101L).setName("添加用户").setPath("/ims/user/add"));
        menuList.add(new Menu().setId(1102L).setName("编辑用户").setPath("/ims/user/edit"));
    }

//    @Test
//    @WithMockUser
//    public void givenUserId_whenFindRole_thenRoleJsonArray() throws Exception {
//        when(menuService.findMenusByRoleId(anyLong())).thenReturn(menuList);
//
//        mvc.perform(get("/menu"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
//                .andExpect(jsonPath("$.data", hasSize(5)))
//                .andExpect(jsonPath("$.data[0].name", equalTo("工作台")));
//
//        verify(menuService).findMenusByRoleId(anyLong());
//    }

}