package me.batizhao.ims.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.batizhao.ims.api.domain.UserRole;

import java.util.List;

/**
 * @author batizhao
 * @since 2020-09-14
 **/
public interface UserRoleService extends IService<UserRole> {

    /**
     * 更新用户角色
     * @param userRoles
     * @return
     */
    Boolean updateUserRoles(List<UserRole> userRoles);
}
