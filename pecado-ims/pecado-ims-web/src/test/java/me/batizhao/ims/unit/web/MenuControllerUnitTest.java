package me.batizhao.ims.unit.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.batizhao.common.core.constant.MenuTypeEnum;
import me.batizhao.common.core.util.BeanCopyUtil;
import me.batizhao.common.core.util.ResultEnum;
import me.batizhao.common.security.component.PecadoUser;
import me.batizhao.common.security.util.SecurityUtils;
import me.batizhao.ims.api.dto.TreeNode;
import me.batizhao.ims.api.vo.MenuTree;
import me.batizhao.ims.api.vo.MenuVO;
import me.batizhao.ims.api.vo.RoleVO;
import me.batizhao.ims.domain.Menu;
import me.batizhao.ims.service.MenuService;
import me.batizhao.ims.service.RoleService;
import me.batizhao.ims.web.MenuController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author batizhao
 * @since 2020-02-10
 */
@WebMvcTest(MenuController.class)
public class MenuControllerUnitTest extends BaseControllerUnitTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    /**
     * 所有实现的接口都要 Mock
     */
    @MockBean
    private MenuService menuService;
    @MockBean
    private RoleService roleService;

    private List<Menu> menuList;
    private List<RoleVO> roleList;
    private List<MenuVO> menuVOList;

    /**
     * Prepare test data.
     */
    @BeforeEach
    public void setUp() {
        menuList = new ArrayList<>();
        menuList.add(new Menu().setId(1).setName("工作台").setPermission("user_dashboard").setPid(0).setSort(1).setType(MenuTypeEnum.LEFT_MENU.getType()).setPath("/aaa"));
        menuList.add(new Menu().setId(2).setName("权限管理").setPermission("ims_root").setPid(1).setSort(1).setType(MenuTypeEnum.LEFT_MENU.getType()));
        menuList.add(new Menu().setId(3).setName("用户管理").setPermission("ims_user_admin").setPid(2).setSort(2).setType(MenuTypeEnum.LEFT_MENU.getType()));
        menuList.add(new Menu().setId(4).setName("角色管理").setPermission("ims_role_admin").setPid(2).setSort(1).setType(MenuTypeEnum.LEFT_MENU.getType()));

        menuVOList = BeanCopyUtil.copyListProperties(menuList, MenuVO::new);

        roleList = new ArrayList<>();
        roleList.add(new RoleVO().setId(1L));
        roleList.add(new RoleVO().setId(2L));
    }

    @Test
    @WithMockUser
    public void givenNothing_whenFindMenuTree4Me_thenSuccess() throws Exception {
        PecadoUser pecadoUser = new PecadoUser(1L, 2L, "zhangsan", "N_A", true, true, true, true, AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));

        List<MenuVO> trees = new ArrayList<>();
        MenuVO menuVO = menuVOList.get(0);

        List<TreeNode> children = new ArrayList<>();
        children.add(menuVOList.get(1));
        menuVO.setChildren(children);
        trees.add(menuVO);

        try (MockedStatic<SecurityUtils> mockStatic = mockStatic(SecurityUtils.class)) {
            mockStatic.when(SecurityUtils::getUser).thenReturn(pecadoUser);
            SecurityUtils.getUser();
            mockStatic.verify(times(1), SecurityUtils::getUser);

            when(roleService.findRolesByUserId(anyLong())).thenReturn(roleList);
            when(menuService.findMenusByRoleId(anyLong())).thenReturn(menuVOList);
            when(menuService.filterMenu((new HashSet<>(menuVOList)), null)).thenReturn(trees);

            mvc.perform(get("/menu/me"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                    .andExpect(jsonPath("$.data", hasSize(1)))
                    .andExpect(jsonPath("$.data[0].children[0].name", equalTo("权限管理")));
        }
    }

    @Test
    @WithMockUser
    public void givenRoleId_whenFindMenus_thenSuccess() throws Exception {
        doReturn(menuVOList).when(menuService).findMenusByRoleId(anyLong());

        mvc.perform(get("/menu").param("roleId", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data", hasSize(4)))
                .andExpect(jsonPath("$.data[0].name", equalTo("工作台")));
    }

    @Test
    @WithMockUser
    void givenNothing_whenFindMenuTree_thenSuccess() throws Exception {
        List<MenuTree> menuTrees = new ArrayList<>();

        MenuTree menuTree = new MenuTree();
        menuTree.setTitle(menuList.get(0).getName());
        menuTree.setKey(menuList.get(0).getPermission());
        menuTree.setPid(menuList.get(0).getPid());
        menuTree.setId(menuList.get(0).getId());

        MenuTree menuTree2 = new MenuTree();
        menuTree2.setTitle(menuList.get(1).getName());
        menuTree2.setKey(menuList.get(1).getPermission());
        menuTree2.setPid(menuList.get(1).getPid());
        menuTree2.setId(menuList.get(1).getId());

        List<TreeNode> children = new ArrayList<>();
        children.add(menuTree2);
        menuTree.setChildren(children);
        menuTrees.add(menuTree);

        doReturn(menuTrees).when(menuService).findMenuTree();

        mvc.perform(get("/menus"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].children[0].key", equalTo("ims_root")));
    }

    @Test
    @WithMockUser
    public void givenId_whenFindMenu_thenSuccess() throws Exception {
        doReturn(menuVOList.get(0)).when(menuService).findMenuById(anyInt());

        mvc.perform(get("/menu/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.name").value("工作台"));
    }

    @Test
    @WithMockUser
    public void givenMenu_whenSaveMenu_thenSuccess() throws Exception {
        Menu menu = menuList.get(0);
        menu.setId(null);

        doReturn(menuVOList.get(1)).when(menuService).saveOrUpdateMenu(any(Menu.class));

        mvc.perform(post("/menu").with(csrf())
                .content(objectMapper.writeValueAsString(menu))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.name").value("权限管理"));

        verify(menuService).saveOrUpdateMenu(any(Menu.class));
    }

    @Test
    @WithMockUser
    public void givenMenu_whenUpdateMenu_thenSuccess() throws Exception {
        doReturn(menuVOList.get(0)).when(menuService).saveOrUpdateMenu(any(Menu.class));

        mvc.perform(post("/menu").with(csrf())
                .content(objectMapper.writeValueAsString(menuList.get(0)))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.name").value("工作台"));

        verify(menuService).saveOrUpdateMenu(any(Menu.class));
    }
}