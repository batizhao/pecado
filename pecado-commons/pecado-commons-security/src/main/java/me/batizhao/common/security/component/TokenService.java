package me.batizhao.common.security.component;

import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.cache.api.CacheService;
import me.batizhao.common.core.constant.CacheConstants;
import me.batizhao.common.core.constant.SecurityConstants;
import me.batizhao.common.core.domain.PecadoUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.time.Duration;

/**
 * @author batizhao
 * @date 2021/12/17
 */
@Component
@Slf4j
public class TokenService {

    @Autowired
    private CacheService cacheService;

    @Value("${pecado.jwt.expire}")
    private Duration expire;

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public PecadoUser getUser(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isNotEmpty(header) && header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            String token = header.replace(SecurityConstants.TOKEN_PREFIX, "");
            String uid = null;
            try {
                SignedJWT signed = SignedJWT.parse(token);
                uid = signed.getJWTClaimsSet().getStringClaim(SecurityConstants.LOGIN_KEY_UID);
            } catch (ParseException e) {
                log.error("Error in parsing token：->", e);
            }
            return cacheService.get(CacheConstants.LOGIN_KEY_UID + uid, PecadoUser.class);
        }
        return null;
    }

    /**
     * 刷新令牌有效期
     *
     * @param pecadoUser 登录信息
     */
    private void refreshToken(PecadoUser pecadoUser) {
        pecadoUser.setLoginTime(System.currentTimeMillis());
        pecadoUser.setExpireTime(pecadoUser.getLoginTime() + expire.toMillis());
        cacheService.set(CacheConstants.LOGIN_KEY_UID + pecadoUser.getUid(), pecadoUser, (int)expire.toMinutes());
    }

    public void verifyToken(PecadoUser pecadoUser) {
        long expireTime = pecadoUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= 15 * 60 * 1000L)
        {
            refreshToken(pecadoUser);
        }
    }

}
