package me.batizhao.uaa.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import me.batizhao.common.core.util.R;
import me.batizhao.common.security.util.SecurityUtils;
import me.batizhao.ims.api.domain.LoginDTO;
import me.batizhao.uaa.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

/**
 * 岗位 API
 *
 * @module uaa
 *
 * @author batizhao
 * @since 2021-04-22
 */
@Tag(name = "Token管理")
@RestController
@Validated
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/token")
    public R<String> handleLogin(@Valid @RequestBody LoginDTO loginDTO) {
        return R.ok(authService.login(loginDTO.getUsername(), loginDTO.getPassword(),
                loginDTO.getCode(), loginDTO.getUuid()));
    }

    /**
     * 生成验证码
     */
    @GetMapping("/captcha")
    public R<Map<String, String>> handleCaptcha() throws IOException {
        return R.ok(authService.getCaptchaImage());
    }

    /**
     * logout
     */
    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public R<Boolean> handleLogout() {
        return R.ok(authService.logout(SecurityUtils.getUser().getUid()));
    }
}
