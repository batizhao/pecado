package me.batizhao.ims.service;

import com.baomidou.mybatisplus.extension.service.IService;
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
     * 查询菜单
     * @param all 全部菜单
     * @param parentId 父节点ID
     * @return
     */
    List<MenuVO> filterMenu(Set<MenuVO> all, Integer parentId);

}
