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
import me.batizhao.ims.api.vo.RoleVO;
import me.batizhao.ims.api.vo.UserInfoVO;
import me.batizhao.ims.api.vo.UserVO;
import me.batizhao.ims.domain.User;
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
    public ResponseInfo<UserVO> handleUsername(@ApiParam(value = "用户名", required = true) @RequestParam @Size(min = 3) String username) {
        UserVO user = userService.findByUsername(username);

        List<RoleVO> roles = roleService.findRolesByUserId(user.getId());
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
    @SystemLog
    public ResponseInfo<List<UserVO>> handleName(@ApiParam(value = "用户姓名", required = true) @RequestParam("name") @Size(min = 2) String name) {
        List<UserVO> users = userService.findByName(name);
        return ResponseInfo.ok(users);
    }

    /**
     * 根据ID查询用户
     * 返回用户详情
     *
     * @param id 用户id
     * @return 用户详情
     */
    @ApiOperation(value = "根据ID查询用户")
    @GetMapping("/user/{id}")
    @SystemLog
    public ResponseInfo<UserVO> handleId(@ApiParam(value = "用户ID", required = true) @PathVariable("id") @Min(1) Long id) {
        UserVO user = userService.findById(id);
        return ResponseInfo.ok(user);
    }

    /**
     * 查询所有用户
     * 返回所有的用户
     *
     * @return 所有用户列表
     */
    @ApiOperation(value = "查询所有用户")
    @GetMapping("users")
    @PreAuthorize("hasRole('ADMIN')")
    @SystemLog
    public ResponseInfo<IPage<UserVO>> handleUsers(Page<UserVO> page, User user) {
        IPage<UserVO> users = userService.findUsers(page, user);
        return ResponseInfo.ok(users);
    }

    /**
     * 添加或修改用户
     * 根据是否有ID判断是添加还是修改
     *
     * @param request_user 用户属性
     * @return 用户对象
     */
    @ApiOperation(value = "添加或修改用户")
    @PostMapping("user")
    @PreAuthorize("hasRole('ADMIN')")
    @SystemLog
    public ResponseInfo<UserVO> handleSaveOrUpdate(@Valid @ApiParam(value = "用户", required = true) @RequestBody User request_user) {
        UserVO user = userService.saveOrUpdateUser(request_user);
        return ResponseInfo.ok(user);
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
        Boolean b = userService.removeByIds(ids);
        return ResponseInfo.ok(b);
    }

    /**
     * 禁用
     *
     * @param id 用户ID
     * @return
     */
    @ApiOperation(value = "禁用")
    @PostMapping("/user/lock")
    @PreAuthorize("hasRole('ADMIN')")
    @SystemLog
    public ResponseInfo<Boolean> handleLockUser(@ApiParam(value = "用户ID", required = true) @RequestParam Long id) {
        Boolean b = userService.updateUserStatusById(id, 1);
        return ResponseInfo.ok(b);
    }

    /**
     * 启用
     *
     * @param id 用户ID
     * @return
     */
    @ApiOperation(value = "启用")
    @PostMapping("/user/unlock")
    @PreAuthorize("hasRole('ADMIN')")
    @SystemLog
    public ResponseInfo<Boolean> handleUnLockUser(@ApiParam(value = "用户ID", required = true) @RequestParam Long id) {
        Boolean b = userService.updateUserStatusById(id, 0);
        return ResponseInfo.ok(b);
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
     * @return true or false
     */
    @ApiOperation(value = "分配用户角色")
    @PostMapping(value = "/user/role")
    @PreAuthorize("hasRole('ADMIN')")
    @SystemLog
    public ResponseInfo<Boolean> handleAddUserRoles(@ApiParam(value = "用户ID", required = true) @RequestParam @Min(1) Long id,
                                                    @ApiParam(value = "关联角色ID串", required = true) @RequestParam List<String> roles) {
        return ResponseInfo.ok(userRoleService.updateUserRoles(id, roles));
    }

}
