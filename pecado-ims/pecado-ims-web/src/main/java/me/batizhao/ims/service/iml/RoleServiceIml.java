package me.batizhao.ims.service.iml;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.batizhao.common.core.util.BeanCopyUtil;
import me.batizhao.ims.api.vo.RoleVO;
import me.batizhao.ims.domain.Role;
import me.batizhao.ims.mapper.RoleMapper;
import me.batizhao.ims.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author batizhao
 * @since 2020-03-14
 **/
@Service
public class RoleServiceIml extends ServiceImpl<RoleMapper, Role> implements RoleService {

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
}
