package me.batizhao.ims.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 查用户角色
     * @param id
     * @return
     */
    @Select("SELECT A.id, A.name FROM role A LEFT JOIN user_role B ON A.id = B.role_id WHERE B.user_id = #{id}")
    List<Role> findRolesByUserId(@Param("id") Long id);

    /**
     * 查询角色权限关系
     *
     * @return
     */
    @Select("SELECT A.NAME AS roleName,C.path FROM role AS A LEFT JOIN role_menu B ON A.id=B.role_id LEFT JOIN menu AS C ON B.menu_id=C.id")
    List<RoleMenu> findRoleMenus();
}
