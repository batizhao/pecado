package me.batizhao.ims.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.util.ResponseInfo;
import me.batizhao.common.security.annotation.Inner;
import me.batizhao.common.security.util.SecurityUtils;
import me.batizhao.ims.api.domain.Role;
import me.batizhao.ims.api.domain.User;
import me.batizhao.ims.api.domain.UserRole;
import me.batizhao.ims.api.vo.UserInfoVO;
import me.batizhao.ims.service.RoleService;
import me.batizhao.ims.service.UserRoleService;
import me.batizhao.ims.service.UserService;
import me.batizhao.system.api.annotation.SystemLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 用户管理
 * 这里是用户管理接口的描述
 *
 * @module pecado-ims
 *
 * @author batizhao
 * @since 2016/9/28
 */
@Api(tags = "用户管理")
@RestController
@Slf4j
@Validated
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserRoleService userRoleService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param user 用户
     * @return ResponseInfo
     */
    @ApiOperation(value = "查询所有用户")
    @GetMapping("users")
    @PreAuthorize("hasRole('ADMIN')")
    @SystemLog
    public ResponseInfo<IPage<User>> handleUsers(Page<User> page, User user) {
        return ResponseInfo.ok(userService.findUsers(page, user));
    }

    /**
     * 通过id查询用户
     * @param id id
     * @return ResponseInfo
     */
    @ApiOperation(value = "通过id查询")
    @GetMapping("/user/{id}")
    @SystemLog
    public ResponseInfo<User> handleId(@ApiParam(value = "ID" , required = true) @PathVariable("id") @Min(1) Long id) {
        return ResponseInfo.ok(userService.findById(id));
    }

    /**
     * 根据用户名查询用户
     * 用户名不重复，返回单个用户详情（包括其角色）
     *
     * @param username 用户名
     * @return 用户详情
     */
    @ApiOperation(value = "根据用户名查询用户")
    @GetMapping(value = "user", params = "username")
    @Inner
    @SystemLog
    public ResponseInfo<User> handleUsername(@ApiParam(value = "用户名", required = true) @RequestParam @Size(min = 3) String username) {
        User user = userService.findByUsername(username);

        List<Role> roles = roleService.findRolesByUserId(user.getId());
        user.setRoleList(roles);

        return ResponseInfo.ok(user);
    }

    /**
     * 根据姓名查询用户
     * 有可能重复，所以返回用户列表
     *
     * @param name 用户姓名
     * @return 返回用户列表
     */
    @ApiOperation(value = "根据姓名查询用户")
    @GetMapping(value = "user", params = "name")
    @PreAuthorize("hasRole('ADMIN')")
    @SystemLog
    public ResponseInfo<List<User>> handleName(@ApiParam(value = "用户姓名", required = true) @RequestParam("name") @Size(min = 2) String name) {
        return ResponseInfo.ok(userService.findByName(name));
    }

    /**
     * 添加或编辑用户
     * @param user 用户
     * @return ResponseInfo
     */
    @ApiOperation(value = "添加或编辑用户")
    @PostMapping("/user")
    @PreAuthorize("hasRole('ADMIN')")
    @SystemLog
    public ResponseInfo<User> handleSaveOrUpdate(@Valid @ApiParam(value = "用户" , required = true) @RequestBody User user) {
        return ResponseInfo.ok(userService.saveOrUpdateUser(user));
    }

    /**
     * 删除用户
     * 根据用户ID删除用户
     *
     * @return 成功或者失败
     */
    @ApiOperation(value = "删除用户")
    @DeleteMapping("user")
    @PreAuthorize("hasRole('ADMIN')")
    @SystemLog
    public ResponseInfo<Boolean> handleDelete(@ApiParam(value = "用户ID串", required = true) @RequestParam List<Long> ids) {
        Boolean b = userService.deleteByIds(ids);
        return ResponseInfo.ok(b);
    }

    /**
     * 更新用户状态
     *
     * @param user 用户
     * @return ResponseInfo
     */
    @ApiOperation(value = "更新用户状态")
    @PostMapping("/user/status")
    @PreAuthorize("hasRole('ADMIN')")
    @SystemLog
    public ResponseInfo<Boolean> handleUpdateStatus(@ApiParam(value = "用户" , required = true) @RequestBody User user) {
        return ResponseInfo.ok(userService.updateStatus(user));
    }

    /**
     * 我的信息
     *
     * @return 当前用户基本信息、角色、权限清单
     */
    @ApiOperation(value = "我的信息")
    @GetMapping("/user/me")
    @SystemLog
    public ResponseInfo<UserInfoVO> handleUserInfo() {
        Long userId = SecurityUtils.getUser().getUserId();
        UserInfoVO userInfoVO = userService.getUserInfo(userId);
        return ResponseInfo.ok(userInfoVO);
    }

    /**
     * 分配用户角色
     * 返回 true or false
     *
     * @param userRoleList 角色清单
     * @return true or false
     */
    @ApiOperation(value = "分配用户角色")
    @PostMapping(value = "/user/role")
    @PreAuthorize("hasRole('ADMIN')")
    @SystemLog
    public ResponseInfo<Boolean> handleAddUserRoles(@ApiParam(value = "关联角色", required = true) @RequestBody List<UserRole> userRoleList) {
        return ResponseInfo.ok(userRoleService.updateUserRoles(userRoleList));
    }

}
