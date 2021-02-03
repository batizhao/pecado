package me.batizhao.ims.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.batizhao.common.core.exception.NotFoundException;
import me.batizhao.common.core.util.BeanCopyUtil;
import me.batizhao.ims.api.vo.MenuVO;
import me.batizhao.ims.api.vo.RoleVO;
import me.batizhao.ims.api.vo.UserInfoVO;
import me.batizhao.ims.api.vo.UserVO;
import me.batizhao.ims.domain.User;
import me.batizhao.ims.mapper.UserMapper;
import me.batizhao.ims.service.MenuService;
import me.batizhao.ims.service.RoleService;
import me.batizhao.ims.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
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

    @Override
    public UserVO findByUsername(String username) {
        User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));

        if(user == null) {
            throw new NotFoundException(String.format("没有该用户 '%s'。", username));
        }

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public List<UserVO> findByName(String name) {
        List<User> userList = userMapper.selectList(Wrappers.<User>lambdaQuery().eq(User::getName, name));
        return BeanCopyUtil.copyListProperties(userList, UserVO::new);
    }

    @Override
    public IPage<UserVO> findUsers(Page<UserVO> page, User user) {
        return userMapper.selectUserPage(page, user);
    }

    @Override
    public UserVO findById(Long userId) {
        User user = userMapper.selectById(userId);

        if(user == null) {
            throw new NotFoundException(String.format("没有该用户 '%s'。", userId));
        }

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    @Transactional
    public int deleteByUsername(String username) {
        return userMapper.delete(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
    }

    @Override
    @Transactional
    public UserVO saveOrUpdateUser(User user) {
        BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
        String hashPass = bcryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(hashPass);

        if (user.getId() == null) {
            user.setCreateTime(LocalDateTime.now());
            user.setUpdateTime(LocalDateTime.now());
            userMapper.insert(user);
        } else {
            user.setUpdateTime(LocalDateTime.now());
            userMapper.updateById(user);
        }

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);

        return userVO;
    }

    @Override
    public UserInfoVO getUserInfo(Long userId) {
        User user = userMapper.selectById(userId);

        if(user == null) {
            throw new NotFoundException(String.format("没有该用户 '%s'。", userId));
        }

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);

        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setUserVO(userVO);
        userInfoVO.setRoles(roleService.findRolesByUserId(userId).stream().map(RoleVO::getCode).collect(Collectors.toList()));
        userInfoVO.setPermissions(menuService.findMenusByUserId(userId).stream().map(MenuVO::getPermission).collect(Collectors.toList()));
        return userInfoVO;
    }

    @Override
    @Transactional
    public Boolean updateUserStatusById(Long userId, Integer locked) {
        return userMapper.updateUserStatusById(userId, locked) == 1;
    }
}
