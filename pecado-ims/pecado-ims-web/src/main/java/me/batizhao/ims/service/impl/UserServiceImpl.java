package me.batizhao.ims.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.models.auth.In;
import me.batizhao.common.core.exception.NotFoundException;
import me.batizhao.common.core.exception.PecadoException;
import me.batizhao.common.security.util.SecurityUtils;
import me.batizhao.ims.api.annotation.DataScope;
import me.batizhao.ims.api.domain.Menu;
import me.batizhao.ims.api.domain.Role;
import me.batizhao.ims.api.domain.User;
import me.batizhao.ims.api.domain.UserRole;
import me.batizhao.ims.api.vo.UserInfoVO;
import me.batizhao.ims.domain.Department;
import me.batizhao.ims.mapper.UserMapper;
import me.batizhao.ims.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author batizhao
 * @since 2016/9/28
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleService roleService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private UserRoleService userRoleService;

    @Override
    @DataScope(deptAlias = "ud", userAlias = "u")
    public IPage<User> findUsers(User user, Page<User> page, Long departmentId) {
        if (departmentId == null) departmentId = 1L;
        return userMapper.selectUsers(page, user, departmentId);
    }

    @Override
    public List<User> findUsers(User user) {
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(user.getUsername())) {
            wrapper.like(User::getUsername, user.getUsername());
        }
        if (StringUtils.isNotBlank(user.getName())) {
            wrapper.like(User::getName, user.getName());
        }
        return userMapper.selectList(wrapper);
    }

    @Override
    public User findById(Long id) {
        User user = userMapper.selectById(id);

        if (user == null) {
            throw new NotFoundException(String.format("Record not found '%s'。", id));
        }

        return user;
    }

    @Override
    public User findByUsername(String username) {
        User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));

        if (user == null) {
            throw new NotFoundException(String.format("Record not found '%s'。", username));
        }

        return user;
    }

//    @Override
//    public List<User> findByName(String name) {
//        return userMapper.selectList(Wrappers.<User>lambdaQuery().eq(User::getName, name));
//    }

    @Override
    @Transactional
    public User saveOrUpdateUser(User user) {
        if (user.getId() == null) {
            BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
            String hashPass = bcryptPasswordEncoder.encode("123456");
            user.setPassword(hashPass);

            user.setUuid(UUID.randomUUID().toString());
            user.setCreateTime(LocalDateTime.now());
            user.setUpdateTime(LocalDateTime.now());
            userMapper.insert(user);
        } else {
            user.setUpdateTime(LocalDateTime.now());
            userMapper.updateById(user);
        }

        return user;
    }

    @Override
    @Transactional
    public Boolean updatePassword(Long userId, String oldPassword, String newPassword) {
        User user = this.findById(userId);

        BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
        if (!bcryptPasswordEncoder.matches(oldPassword, user.getPassword()))
            throw new PecadoException("The old password is incorrect!");

        if (bcryptPasswordEncoder.matches(newPassword, user.getPassword()))
            throw new PecadoException("Same old and new password!");

        String hashPass = bcryptPasswordEncoder.encode(newPassword);
        user.setPassword(hashPass);
        user.setUpdateTime(LocalDateTime.now());
        return userMapper.updateById(user) == 1;
    }

    @Override
    @Transactional
    public Boolean deleteByIds(List<Long> ids) {
        ids.forEach(i -> {
            checkUserIsAdmin(i);
            userRoleService.remove(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getUserId, i));
        });
        this.removeByIds(ids);
        return true;
    }

    private void checkUserIsAdmin(Long id) {
        if (id.equals(1L)) {
            throw new PecadoException("Operation not allowed!");
        }
    }

    @Override
    @Transactional
    public Boolean updateStatus(User user) {
        LambdaUpdateWrapper<User> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(User::getId, user.getId()).set(User::getStatus, user.getStatus());
        return userMapper.update(null, wrapper) == 1;
    }

    @Override
    public UserInfoVO getUserInfo(Long userId) {
        User user = userMapper.selectById(userId);

        if (user == null) {
            throw new NotFoundException(String.format("Record not found '%s'。", userId));
        }

        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setUser(user);
        userInfoVO.setDeptIds(departmentService.findDepartmentsByUserId(userId).stream().map(Department::getId).map(String::valueOf).collect(Collectors.toList()));
        userInfoVO.setRoleIds(roleService.findRolesByUserId(userId).stream().map(Role::getId).map(String::valueOf).collect(Collectors.toList()));
        userInfoVO.setRoles(roleService.findRolesByUserId(userId).stream().map(Role::getCode).collect(Collectors.toList()));
        userInfoVO.setPermissions(menuService.findMenusByUserId(userId).stream().map(Menu::getPermission).filter(org.springframework.util.StringUtils::hasText).collect(Collectors.toList()));
        return userInfoVO;
    }

    @Override
    public List<User> findLeadersByDepartmentId(Integer departmentId, String type) {
        return userMapper.selectLeadersByDepartmentId(departmentId, type);
    }

    @Override
    public List<User> findLeaders() {
        List<String> depts = SecurityUtils.getUser().getDeptIds();
        return this.findLeadersByDepartmentId(Integer.valueOf(depts.get(0)), null);
    }

    @Override
    public String importUsers(List<User> users, boolean updateSupport) {
        if (CollectionUtils.isEmpty(users)) {
            throw new PecadoException("Data cannot be empty.");
        }

        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
        for (User user : users) {
            try {
                // 验证是否存在这个用户
                User u = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, user.getUsername()));
                if (u == null) {
                    String hashPass = bcryptPasswordEncoder.encode("123456");
                    user.setPassword(hashPass);

                    user.setUuid(UUID.randomUUID().toString());
                    user.setCreateTime(LocalDateTime.now());
                    user.setUpdateTime(LocalDateTime.now());
                    this.save(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、User [" + user.getUsername() + "] Import succeeded.");
                } else if (updateSupport) {
                    user.setUpdateTime(LocalDateTime.now());
                    this.updateById(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、User [" + user.getUsername() + "] Update succeeded.");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、User [" + user.getUsername() + "] Already exists.");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、User [" + user.getUsername() + "] Import failed：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "Import failed! Total " + failureNum + " incorrect format of data, error as follows: ");
            throw new PecadoException(failureMsg.toString());
        } else {
            successMsg.insert(0, "Congratulations, all data have been imported successfully！Total " + successNum + " records, The data are as follows:");
        }
        return successMsg.toString();
    }
}
