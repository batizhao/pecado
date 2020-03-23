package me.batizhao.ims.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.util.ResponseInfo;
import me.batizhao.common.security.annotation.Inner;
import me.batizhao.ims.api.vo.RoleVO;
import me.batizhao.ims.api.vo.UserVO;
import me.batizhao.ims.domain.User;
import me.batizhao.ims.service.RoleService;
import me.batizhao.ims.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
 * @module Pecado
 *
 * @author batizhao
 * @since 2016/9/28
 */
@Api(tags = "用户管理")
@RestController
@RequestMapping("user")
@Slf4j
@Validated
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    /**
     * 根据用户名查询用户
     * 用户名不重复，返回单个用户详情（包括其角色）
     *
     * @param username 用户名
     * @return 用户详情
     */
    @ApiOperation(value = "根据用户名查询用户")
    @GetMapping(params = "username")
    @Inner
    public ResponseInfo<UserVO> loadUserByUsername(@ApiParam(value = "用户名", required = true) @RequestParam @Size(min = 3) String username) {
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
    @GetMapping(params = "name")
    public ResponseInfo<List<UserVO>> findByName(@ApiParam(value = "用户姓名", required = true) @RequestParam("name") @Size(min = 2) String name) {
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
    @GetMapping("{id}")
    public ResponseInfo<UserVO> findById(@ApiParam(value = "用户ID", required = true) @PathVariable("id") @Min(1) Long id) {
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
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseInfo<List<UserVO>> findAll() {
        List<UserVO> users = userService.findAll();
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
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseInfo<UserVO> doSaveOrUpdate(@Valid @ApiParam(value = "用户", required = true) @RequestBody User request_user) {
        UserVO user = userService.saveOrUpdate4me(request_user);
        return ResponseInfo.ok(user);
    }

    /**
     * 删除用户
     * 根据用户ID删除用户
     *
     * @param id 用户id
     * @return 成功或者失败
     */
    @ApiOperation(value = "删除用户")
    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseInfo<Boolean> doDelete(@ApiParam(value = "用户ID", required = true) @Min(1) @PathVariable Long id) {
        Boolean b = userService.removeById(id);
        return ResponseInfo.ok(b);
    }

    /**
     * 我是谁
     * 从认证信息中提取当前用户
     *
     * @param authentication Spring Security Authentication
     * @return 用户名
     */
    @ApiOperation(value = "我是谁")
    @GetMapping("/whoiam")
    public ResponseInfo<String> getCurrentUser(Authentication authentication,
                                               @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authHeader) {
        log.info("authHeader: {}", authHeader);
        String access_token = authHeader.substring("Bearer ".length());
        log.info("access token: {}", access_token);

        String username = authentication.getName();
        return ResponseInfo.ok(username);
    }
}
