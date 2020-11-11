package me.batizhao.ims.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.batizhao.ims.domain.UserRole;
import me.batizhao.ims.mapper.UserRoleMapper;
import me.batizhao.ims.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author batizhao
 * @since 2020-09-14
 **/
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    @Transactional
    public Boolean updateUserRoles(Long id, List<String> roles) {
        userRoleMapper.deleteByUserId(id);

        List<UserRole> userRoles = new ArrayList<>();
        roles.forEach(item -> {
            userRoles.add(new UserRole().setUserId(id).setRoleId(Long.parseLong(item)));
        });
        return saveBatch(userRoles);
    }
}
