package me.batizhao.ims.service.iml;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.batizhao.ims.domain.UserRole;
import me.batizhao.ims.mapper.UserRoleMapper;
import me.batizhao.ims.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author batizhao
 * @since 2020-09-14
 **/
@Service
public class UserRoleServiceIml extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public Boolean updateUserRoles(Long id, List<String> roles) {
        userRoleMapper.deleteByUserId(id);

        List<UserRole> userRoles = new ArrayList<>();
        roles.forEach(item -> {
            userRoles.add(new UserRole().setUserId(id).setRoleId(Long.parseLong(item)));
        });
        return saveBatch(userRoles);
    }
}
