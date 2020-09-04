package me.batizhao.ims.unit.service;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.constant.MenuTypeEnum;
import me.batizhao.common.core.util.BeanCopyUtil;
import me.batizhao.ims.api.dto.TreeNode;
import me.batizhao.ims.api.vo.MenuTree;
import me.batizhao.ims.api.vo.MenuVO;
import me.batizhao.ims.domain.Menu;
import me.batizhao.ims.mapper.MenuMapper;
import me.batizhao.ims.service.MenuService;
import me.batizhao.ims.service.iml.MenuServiceIml;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * @author batizhao
 * @since 2020-02-08
 */
@Slf4j
public class MenuServiceUnitTest extends BaseServiceUnitTest {

    /**
     * Spring Boot 提供了 @TestConfiguration 注释，可用于 src/test/java 中的类，以指示不应通过扫描获取它们。
     */
    @TestConfiguration
    static class TestContextConfiguration {
        @Bean
        public MenuService menuService() {
            return new MenuServiceIml();
        }
    }

    @MockBean
    private MenuMapper menuMapper;

    @Autowired
    private MenuService menuService;

    private List<Menu> menuList;
    private List<MenuVO> menuVOList;

    /**
     * Prepare test data.
     */
    @BeforeEach
    public void setUp() {
        menuList = new ArrayList<>();
        menuList.add(new Menu().setId(1).setName("工作台").setPermission("user_dashboard").setPid(0).setSort(1).setType(MenuTypeEnum.LEFT_MENU.getType()));
        menuList.add(new Menu().setId(2).setName("权限管理").setPermission("ims_root").setPid(1).setSort(1).setType(MenuTypeEnum.LEFT_MENU.getType()));
        menuList.add(new Menu().setId(3).setName("用户管理").setPermission("ims_user_admin").setPid(2).setSort(2).setType(MenuTypeEnum.LEFT_MENU.getType()));
        menuList.add(new Menu().setId(4).setName("角色管理").setPermission("ims_role_admin").setPid(2).setSort(1).setType(MenuTypeEnum.LEFT_MENU.getType()));

        menuVOList = BeanCopyUtil.copyListProperties(menuList, MenuVO::new);
    }

    @Test
    public void givenRoleId_whenFindMenus_thenSuccess() {
        when(menuMapper.findMenusByRoleId(anyLong()))
                .thenReturn(menuList);

        List<MenuVO> menus = menuService.findMenusByRoleId(1L);

        log.info("roles: {}", menus);

        assertThat(menus, hasSize(4));
        assertThat(menus, hasItems(hasProperty("name", is("工作台"))));
    }

    @Test
    public void givenNothing_whenFindMenuTree_thenSuccess() {
        //Fix can not find lambda cache for this entity
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), Menu.class);

        doReturn(menuList).when(menuMapper).selectList(any());

        List<MenuTree> menuTree = menuService.findMenuTree();

        assertThat(menuTree, hasSize(1));
        assertThat(menuTree, hasItems(hasProperty("title", is("工作台")),
                hasProperty("children", hasSize(1))));

        List<TreeNode> treeNodes = menuTree.get(0).getChildren().get(0).getChildren();
        assertThat(treeNodes, hasSize(2));
    }

    @Test
    public void givenNonParent_whenFilterMenus_thenSuccess() {
        List<MenuVO> menus = menuService.filterMenu((new HashSet<>(menuVOList)), null);
        assertThat(menus, hasSize(1));
        assertThat(menus, hasItems(hasProperty("name", is("工作台")),
                hasProperty("children", hasSize(1))));

        List<TreeNode> treeNodes = menus.get(0).getChildren().get(0).getChildren();
        assertThat(treeNodes, hasSize(2));
        assertThat(treeNodes, containsInRelativeOrder(allOf(hasProperty("name", is("角色管理")),
                                                            hasProperty("permission", is("ims_role_admin"))),
                                                      allOf(hasProperty("name", is("用户管理")),
                                                            hasProperty("permission", is("ims_user_admin")))));
    }

    @Test
    public void givenParent_whenFilterMenus_thenSuccess() {
        List<MenuVO> menus = menuService.filterMenu((new HashSet<>(menuVOList)), 1);
        assertThat(menus, hasSize(1));
        assertThat(menus, hasItems(hasProperty("name", is("权限管理")),
                hasProperty("children", hasSize(2))));

        List<TreeNode> treeNodes = menus.get(0).getChildren();
        assertThat(treeNodes, hasSize(2));
        assertThat(treeNodes, containsInRelativeOrder(allOf(hasProperty("name", is("角色管理")),
                hasProperty("permission", is("ims_role_admin"))),
                allOf(hasProperty("name", is("用户管理")),
                        hasProperty("permission", is("ims_user_admin")))));
    }

    @Test
    public void givenId_whenFindMenu_thenSuccess() {
        doReturn(menuList.get(0)).when(menuMapper).selectById(anyInt());

        MenuVO menuVO = menuService.findMenuById(1);

        assertThat(menuVO, hasProperty("name", equalTo("工作台")));
    }

    @Test
    public void givenMenu_whenSaveOrUpdate_thenSuccess() {
        Menu menu = new Menu().setName("工作台").setPermission("user_dashboard").setPid(0).setSort(1).setType(MenuTypeEnum.LEFT_MENU.getType());

        // insert 不带 id
        doReturn(1).when(menuMapper).insert(any(Menu.class));

        MenuVO menuVO = menuService.saveOrUpdateMenu(menu);
        log.info("menuVO: {}", menuVO);

        verify(menuMapper).insert(any());

        // update 需要带 id
        doReturn(1).when(menuMapper).updateById(any(Menu.class));

        menuVO = menuService.saveOrUpdateMenu(menuList.get(0));
        log.info("menuVO: {}", menuVO);

        verify(menuMapper).updateById(any());
    }
}