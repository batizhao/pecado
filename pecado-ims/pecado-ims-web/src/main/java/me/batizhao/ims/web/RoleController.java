package me.batizhao.ims.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.util.ResponseInfo;
import me.batizhao.ims.api.vo.RoleVO;
import me.batizhao.ims.api.vo.UserVO;
import me.batizhao.ims.domain.Role;
import me.batizhao.ims.domain.User;
import me.batizhao.ims.service.RoleMenuService;
import me.batizhao.ims.service.RoleService;
import me.batizhao.system.api.annotation.SystemLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * 角色管理
 * 这里是角色管理接口的描述
 *
 * @module pecado-ims
 *
 * @author batizhao
 * @since 2020-03-14
 **/
@Api(tags = "角色管理")
@RestController
@Slf4j
@Validated
public class RoleController {

    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleMenuService roleMenuService;

    /**
     * 根据用户ID查询角色
     * 返回角色集合
     *
     * @param userId 用户id
     * @return 角色集合
     */
    @ApiOperation(value = "根据用户ID查询角色")
    @GetMapping(value = "role", params = "userId")
    @PreAuthorize("hasRole('ADMIN') and #oauth2.hasScope('write')")
    @SystemLog
    public ResponseInfo<List<RoleVO>> handleRolesByUserId(@ApiParam(value = "用户ID", required = true) @RequestParam("userId") @Min(1) Long userId) {
        List<RoleVO> roles = roleService.findRolesByUserId(userId);
        return ResponseInfo.ok(roles);
    }

    /**
     * 查询所有角色
     * 返回角色集合
     *
     * @return 角色集合
     */
    @ApiOperation(value = "查询所有角色")
    @GetMapping(value = "roles")
    @PreAuthorize("hasRole('ADMIN') and #oauth2.hasScope('write')")
    @SystemLog
    public ResponseInfo<List<RoleVO>> handleRoles() {
        return ResponseInfo.ok(roleService.findRoles());
    }

    /**
     * 分配角色权限
     * 返回 true or false
     *
     * @param id 角色ID
     * @param menus 关联权限ID串
     * @return true or false
     */
    @ApiOperation(value = "分配角色权限")
    @PostMapping(value = "/role/menu")
    @PreAuthorize("hasRole('ADMIN')")
    @SystemLog
    public ResponseInfo<Boolean> handleAddUserRoles(@ApiParam(value = "角色ID", required = true) @RequestParam @Min(1) Long id,
                                                    @ApiParam(value = "关联权限ID串", required = true) @RequestParam List<String> menus) {
        return ResponseInfo.ok(roleMenuService.updateRoleMenus(id, menus));
    }

    /**
     * 添加或修改角色
     * 根据是否有ID判断是添加还是修改
     *
     * @param request_role 角色属性
     * @return 角色对象
     */
    @ApiOperation(value = "添加或修改角色")
    @PostMapping("role")
    @PreAuthorize("hasRole('ADMIN')")
    @SystemLog
    public ResponseInfo<RoleVO> handleSaveOrUpdate(@Valid @ApiParam(value = "用户", required = true) @RequestBody Role request_role) {
        RoleVO roleVO = roleService.saveOrUpdateUser(request_role);
        return ResponseInfo.ok(roleVO);
    }

    /**
     * 删除角色
     * 根据角色ID删除角色
     *
     * @return 成功或者失败
     */
    @ApiOperation(value = "删除角色")
    @DeleteMapping("role")
    @PreAuthorize("hasRole('ADMIN')")
    @SystemLog
    public ResponseInfo<Boolean> handleDelete(@ApiParam(value = "角色ID串", required = true) @RequestParam List<Long> ids) {
        Boolean b = roleService.removeByIds(ids);
        return ResponseInfo.ok(b);
    }
}
