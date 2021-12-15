package me.batizhao.uaa.service;

import me.batizhao.uaa.domain.TokenVO;

import java.io.IOException;
import java.util.Map;

/**
 * @author batizhao
 * @date 2021/8/19
 */
public interface AuthService {

    /**
     * 登录
     * @param username
     * @param password
     * @param code
     * @param uuid
     * @return
     */
    TokenVO login(String username, String password, String code, String uuid);

    /**
     * 获取验证码
     * @return
     * @throws IOException
     */
    Map<String, String> getCaptchaImage() throws IOException;
}
