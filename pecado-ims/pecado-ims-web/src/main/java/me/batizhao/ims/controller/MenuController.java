package me.batizhao.ims.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.util.ResponseInfo;
import me.batizhao.common.security.util.SecurityUtils;
import me.batizhao.ims.api.vo.MenuTree;
import me.batizhao.ims.api.vo.MenuVO;
import me.batizhao.ims.domain.Menu;
import me.batizhao.ims.service.MenuService;
import me.batizhao.system.api.annotation.SystemLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

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

    /**
     * 查询当前用户菜单
     * 返回菜单树
     *
     * @return 菜单树
     */
    @ApiOperation(value = "查询当前用户菜单")
    @SystemLog
    @GetMapping("/menu/me")
    public ResponseInfo<List<MenuVO>> handleMenuTree4Me() {
        Long userId = SecurityUtils.getUser().getUserId();
        return ResponseInfo.ok(menuService.findMenuTreeByUserId(userId));
    }

    /**
     * 查询角色菜单
     * 返回菜单树
     *
     * @return 菜单树
     */
    @ApiOperation(value = "查询角色菜单")
    @SystemLog
    @GetMapping(value = "menu", params = "roleId")
    public ResponseInfo<List<MenuVO>> handleMenusByRoleId(@ApiParam(value = "角色ID", required = true) @RequestParam("roleId") @Min(1) Long roleId) {
        return ResponseInfo.ok(menuService.findMenusByRoleId(roleId));
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
    public ResponseInfo<List<MenuVO>> handleMenuTree() {
        return ResponseInfo.ok(menuService.findMenuTree());
    }

    /**
     * 根据菜单 ID 查询
     * @param id 菜单 ID
     * @return 菜单对象
     */
    @ApiOperation(value = "根据菜单 ID 查询")
    @SystemLog
    @GetMapping("/menu/{id}")
    public ResponseInfo<MenuVO> handleMenu(@ApiParam(value = "菜单ID", required = true) @PathVariable("id") @Min(1) Integer id) {
        return ResponseInfo.ok(menuService.findMenuById(id));
    }

    /**
     * 添加或修改菜单
     * 根据是否有ID判断是添加还是修改
     *
     * @param menu 菜单属性
     * @return 菜单对象
     */
    @ApiOperation(value = "添加或修改菜单")
    @PostMapping("menu")
    @PreAuthorize("hasRole('ADMIN')")
    @SystemLog
    public ResponseInfo<MenuVO> handleSaveOrUpdate(@Valid @ApiParam(value = "菜单", required = true) @RequestBody Menu menu) {
        MenuVO menuVO = menuService.saveOrUpdateMenu(menu);
        return ResponseInfo.ok(menuVO);
    }

    /**
     * 更新菜单状态
     *
     * @param ds 菜单
     * @return ResponseInfo
     */
    @ApiOperation(value = "更新菜单状态")
    @PostMapping("/menu/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseInfo<Boolean> handleUpdateStatus(@ApiParam(value = "菜单" , required = true) @RequestBody Menu menu) {
        return ResponseInfo.ok(menuService.updateMenuStatus(menu));
    }

}
