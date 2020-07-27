package me.batizhao.ims.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.batizhao.ims.domain.Menu;
import me.batizhao.ims.domain.Role;
import me.batizhao.ims.domain.RoleMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author batizhao
 * @since 2020-02-26
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 通过角色ID查询菜单
     * @param roleId 角色ID
     * @return
     */
    @Select("SELECT A.* FROM menu A LEFT JOIN role_menu B ON A.id = B.menu_id WHERE B.role_id = #{id}")
    List<Menu> findMenusByRoleId(Long roleId);
}