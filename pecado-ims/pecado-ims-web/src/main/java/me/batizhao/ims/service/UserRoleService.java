package me.batizhao.ims.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.batizhao.ims.domain.UserRole;

import java.util.List;

/**
 * @author batizhao
 * @since 2020-09-14
 **/
public interface UserRoleService extends IService<UserRole> {

    Boolean updateUserRoles(Long id, List<String> roles);
}
