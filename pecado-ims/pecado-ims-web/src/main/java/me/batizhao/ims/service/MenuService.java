package me.batizhao.ims.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.batizhao.ims.domain.Menu;

import java.util.List;

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
    List<Menu> findMenusByRoleId(Long roleId);

}
