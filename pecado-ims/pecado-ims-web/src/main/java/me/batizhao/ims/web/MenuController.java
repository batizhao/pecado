package me.batizhao.ims.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.constant.SecurityConstants;
import me.batizhao.common.core.util.ResponseInfo;
import me.batizhao.common.security.util.SecurityUtils;
import me.batizhao.ims.domain.Menu;
import me.batizhao.ims.service.MenuService;
import me.batizhao.ims.service.RoleService;
import me.batizhao.system.api.annotation.SystemLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Map;
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
@RequestMapping("menu")
@Slf4j
@Validated
public class MenuController {

    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleService roleService;

    /**
     * 查询用户菜单
     * 返回菜单集合
     *
     * @return 菜单集合
     */
    @ApiOperation(value = "查询用户菜单")
    @SystemLog
    @GetMapping
    public ResponseInfo<Set<Menu>> getUserMenus() {
        Map<String, Object> additionalInfo = SecurityUtils.getAdditionalInfo();
        Integer userId = (Integer) additionalInfo.get(SecurityConstants.DETAILS_USER_ID);

        Set<Menu> all = new HashSet<>();
        roleService.findRolesByUserId(userId.longValue()).forEach(roleVO -> all.addAll(menuService.findMenusByRoleId(roleVO.getId())));
        return ResponseInfo.ok(all);
    }

//    @Autowired
//    private TokenStore tokenStore;
//
//    /**
//     * 获取 token 中的附加信息
//     * @return
//     */
//	private Map<String, Object> getAdditionalInfo() {
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		if (authentication == null) {
//			return null;
//		}
//        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
//        OAuth2AccessToken accessToken = tokenStore.readAccessToken(details.getTokenValue());
//        return accessToken.getAdditionalInformation();
//    }


}
