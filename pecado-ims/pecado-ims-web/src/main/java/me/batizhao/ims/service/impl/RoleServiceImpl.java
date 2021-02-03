package me.batizhao.ims.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.batizhao.common.core.util.BeanCopyUtil;
import me.batizhao.ims.api.vo.RoleVO;
import me.batizhao.ims.domain.Role;
import me.batizhao.ims.mapper.RoleMapper;
import me.batizhao.ims.service.RoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author batizhao
 * @since 2020-03-14
 **/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<RoleVO> findRolesByUserId(Long userId) {
        List<Role> roleList = roleMapper.findRolesByUserId(userId);
        return BeanCopyUtil.copyListProperties(roleList, RoleVO::new);
    }

    @Override
    public List<RoleVO> findRoles() {
        List<Role> roles = baseMapper.selectList(null);
        return BeanCopyUtil.copyListProperties(roles, RoleVO::new);
    }

    @Override
    @Transactional
    public RoleVO saveOrUpdateRole(Role role) {
        role.setCreateTime(LocalDateTime.now());

        if (role.getId() == null) {
            role.setCreateTime(LocalDateTime.now());
            role.setUpdateTime(LocalDateTime.now());
            roleMapper.insert(role);
        } else {
            role.setUpdateTime(LocalDateTime.now());
            roleMapper.updateById(role);
        }

        RoleVO roleVO = new RoleVO();
        BeanUtils.copyProperties(role, roleVO);

        return roleVO;
    }
}
