package me.batizhao.ims.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.batizhao.common.core.exception.NotFoundException;
import me.batizhao.common.core.exception.PecadoException;
import me.batizhao.common.core.util.TreeUtil;
import me.batizhao.ims.api.annotation.DataScope;
import me.batizhao.ims.domain.Department;
import me.batizhao.ims.domain.DepartmentLeader;
import me.batizhao.ims.domain.DepartmentRelation;
import me.batizhao.ims.domain.UserDepartment;
import me.batizhao.ims.mapper.DepartmentMapper;
import me.batizhao.ims.service.DepartmentLeaderService;
import me.batizhao.ims.service.DepartmentRelationService;
import me.batizhao.ims.service.DepartmentService;
import me.batizhao.ims.service.UserDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 部门接口实现类
 *
 * @author batizhao
 * @since 2021-04-25
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    private UserDepartmentService userDepartmentService;
    @Autowired
    private DepartmentRelationService departmentRelationService;
    @Autowired
    private DepartmentLeaderService departmentLeaderService;

    @Override
    @DataScope(deptAlias = "d")
    public List<Department> findDepartmentTree(Department department) {
        List<Department> departments = departmentMapper.selectDepartments(department);
        int min = departments.size() > 0 ? Collections.min(departments.stream().map(Department::getPid).collect(Collectors.toList())) : 0;
        return TreeUtil.build(departments, min);
    }

    @Override
    public Department findById(Long id) {
        Department department = departmentMapper.selectById(id);

        if(department == null) {
            throw new NotFoundException(String.format("Record not found '%s'。", id));
        }

        return department;
    }

    @Override
    @Transactional
    public Department saveOrUpdateDepartment(Department department) {
        if (department.getId() == null) {
            department.setCreateTime(LocalDateTime.now());
            department.setUpdateTime(LocalDateTime.now());
            department.setUuid(UUID.randomUUID().toString());
            departmentMapper.insert(department);
            departmentRelationService.saveDepartmentRelation(department);
        } else {
            department.setUpdateTime(LocalDateTime.now());
            departmentMapper.updateById(department);
            departmentRelationService.updateDepartmentRelation(department);
        }

        return department;
    }

    @Override
    @Transactional
    public Boolean deleteById(Integer id) {
        if (checkHasChildren(id)) return false;
        checkDepartmentIsRoot(id);
        this.removeById(id);
        userDepartmentService.remove(Wrappers.<UserDepartment>lambdaQuery().eq(UserDepartment::getDepartmentId, id));
        departmentRelationService.remove(Wrappers.<DepartmentRelation>lambdaQuery().eq(DepartmentRelation::getDescendant, id));
        departmentLeaderService.remove(Wrappers.<DepartmentLeader>lambdaQuery().eq(DepartmentLeader::getDepartmentId, id));
        return true;
    }

    private void checkDepartmentIsRoot(Integer id) {
        if (id.equals(1)) {
            throw new PecadoException("Operation not allowed!");
        }
    }

    @Override
    @Transactional
    public Boolean updateStatus(Department department) {
        LambdaUpdateWrapper<Department> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(Department::getId, department.getId()).set(Department::getStatus, department.getStatus());
        return departmentMapper.update(null, wrapper) == 1;
    }

    @Override
    public Boolean checkHasChildren(Integer id) {
        return departmentMapper.selectList(Wrappers.<Department>lambdaQuery().eq(Department::getPid, id)).size() > 0;
    }

    @Override
    public List<Department> findDepartmentsByUserId(Long userId) {
        return departmentMapper.findDepartmentsByUserId(userId);
    }

    @Override
    public List<Department> findDepartmentsByRoleId(Long roleId) {
        return departmentMapper.findDepartmentsByRoleId(roleId);
    }

}
