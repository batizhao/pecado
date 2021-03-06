package me.batizhao.ims.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.batizhao.common.core.exception.NotFoundException;
import me.batizhao.common.core.util.BeanCopyUtil;
import me.batizhao.ims.api.vo.UserInfoVO;
import me.batizhao.ims.api.vo.UserVO;
import me.batizhao.ims.domain.User;
import me.batizhao.ims.mapper.UserMapper;
import me.batizhao.ims.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author batizhao
 * @since 2016/9/28
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

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
    public UserVO findById(Long id) {
        User user = userMapper.selectById(id);

        if(user == null) {
            throw new NotFoundException(String.format("没有该用户 '%s'。", id));
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
            user.setCreatedTime(LocalDateTime.now());
            userMapper.insert(user);
        } else {
            userMapper.updateById(user);
        }

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);

        return userVO;
    }

    @Override
    public UserInfoVO getUserInfo(String username) {
        User user = userMapper.selectOne(Wrappers.<User>query().lambda().eq(User::getUsername, username));

        if(user == null) {
            throw new NotFoundException(String.format("没有该用户 '%s'。", username));
        }

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);

        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setUserVO(userVO);
        return userInfoVO;
    }

    @Override
    @Transactional
    public Boolean updateUserStatusById(Long id, Integer locked) {
        return userMapper.updateUserStatusById(id, locked) == 1;
    }
}
