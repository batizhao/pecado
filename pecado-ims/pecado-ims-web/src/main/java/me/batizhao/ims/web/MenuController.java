package me.batizhao.ims.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.util.ResponseInfo;
import me.batizhao.common.security.util.SecurityUtils;
import me.batizhao.ims.api.vo.MenuTree;
import me.batizhao.ims.api.vo.MenuVO;
import me.batizhao.ims.service.MenuService;
import me.batizhao.ims.service.RoleService;
import me.batizhao.system.api.annotation.SystemLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 菜单管理
 * 这里是菜单管理接口的描述
 *
 * @module pecado-ims
 *
 * @author batizhao
 * @since 2020-03-14
 **/
@Api(tags = "菜单管理")
@RestController
@Slf4j
@Validated
public class MenuController {

    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleService roleService;

    /**
     * 查询用户菜单
     * 返回菜单树
     *
     * @return 菜单树
     */
    @ApiOperation(value = "查询用户菜单")
    @SystemLog
    @GetMapping("/menu/me")
    public ResponseInfo<List<MenuVO>> handleUserMenuTree() {
        Long userId = SecurityUtils.getUser().getUserId();

        Set<MenuVO> all = new HashSet<>();
        roleService.findRolesByUserId(userId).forEach(roleVO -> all.addAll(menuService.findMenusByRoleId(roleVO.getId())));
        return ResponseInfo.ok(menuService.filterMenu(all, null));
    }

    /**
     * 查询所有菜单
     * 返回菜单树
     *
     * @return 菜单树
     */
    @ApiOperation(value = "查询所有菜单")
    @SystemLog
    @GetMapping("/menus")
    public ResponseInfo<List<MenuTree>> handleMenuTree() {
        return ResponseInfo.ok(menuService.findMenuTree());
    }

}
