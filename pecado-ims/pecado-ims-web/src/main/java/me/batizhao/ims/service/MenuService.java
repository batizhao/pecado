package me.batizhao.ims.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.batizhao.ims.api.vo.MenuTree;
import me.batizhao.ims.api.vo.MenuVO;
import me.batizhao.ims.domain.Menu;

import java.util.List;
import java.util.Set;

/**
 * @author batizhao
 * @since 2020-03-14
 **/
public interface MenuService extends IService<Menu> {

    /**
     * 通过角色查询权限
     * @param roleId 角色ID
     * @return 菜单列表
     */
    List<MenuVO> findMenusByRoleId(Long roleId);

    /**
     * 查询当前用户菜单
     * 返回菜单树
     *
     * @return 菜单树
     */
    List<MenuVO> findMenusByUserId(Long userId);

    /**
     * 查询所有菜单
     * @return 菜单树
     */
    List<MenuTree> findMenuTree();

    /**
     * 构造菜单树
     * @param all 全部菜单
     * @param parentId 父节点ID
     * @return 菜单树
     */
    List<MenuVO> filterMenu(Set<MenuVO> all, Integer parentId);

    /**
     * 查询菜单
     * @param menuId
     * @return 菜单对象
     */
    MenuVO findMenuById(int menuId);

    /**
     * 添加或者更新菜单
     * @param menu
     * @return
     */
    MenuVO saveOrUpdateMenu(Menu menu);
}
