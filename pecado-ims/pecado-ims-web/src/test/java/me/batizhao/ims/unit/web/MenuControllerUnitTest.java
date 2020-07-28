package me.batizhao.ims.unit.web;

import me.batizhao.common.core.util.ResultEnum;
import me.batizhao.common.security.component.PecadoUser;
import me.batizhao.common.security.util.SecurityUtils;
import me.batizhao.ims.api.vo.MenuVO;
import me.batizhao.ims.api.vo.RoleVO;
import me.batizhao.ims.domain.Menu;
import me.batizhao.ims.domain.User;
import me.batizhao.ims.service.MenuService;
import me.batizhao.ims.service.RoleService;
import me.batizhao.ims.web.MenuController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.AuthorityUtils;
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
    @MockBean
    private RoleService roleService;

    private List<MenuVO> menuList;
    private List<RoleVO> roleList;
    private PecadoUser user;

    /**
     * Prepare test data.
     */
    @BeforeEach
    public void setUp() {
        menuList = new ArrayList<>();
        menuList.add(new MenuVO(1, "工作台","/dashboard"));
        menuList.add(new MenuVO(1000, "权限管理", "/ims"));
        menuList.add(new MenuVO(1100, "用户管理", "/ims/user"));
        menuList.add(new MenuVO(1101, "添加用户", "/ims/user/add"));
        menuList.add(new MenuVO(1102, "编辑用户", "/ims/user/edit"));

        roleList = new ArrayList<>();
        roleList.add(new RoleVO().setId(1L));
        roleList.add(new RoleVO().setId(2L));

        user = new PecadoUser(1L, 2L, "admin", "N_A", true, true, true, true, AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
    }

//    @Test
//    @WithMockUser
//    public void givenUserId_whenFindRole_thenRoleJsonArray() throws Exception {
//        when(SecurityUtils.getUser()).thenReturn(user);
//        when(roleService.findRolesByUserId(anyLong())).thenReturn(roleList);
//        when(menuService.findMenusByRoleId(anyLong())).thenReturn(menuList);
//        when(menuService.filterMenu(menuList, null))
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