package me.batizhao.ims.api.feign.fallback;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.util.ResponseInfo;
import me.batizhao.ims.api.domain.Role;
import me.batizhao.ims.api.domain.User;
import me.batizhao.ims.api.feign.UserFeignService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author batizhao
 * @since 2020-03-13
 **/
@Slf4j
@Component
public class UserServiceFallbackImpl implements UserFeignService {

    @Setter
    private Throwable throwable;

    @Override
    public ResponseInfo<User> loadUserByUsername(String username, String from) {
        log.error("feign 查询用户信息失败: {}", username, throwable);
        return null;
    }

    @Override
    public ResponseInfo<List<Role>> findRolesByUserId(Long userId) {
        log.error("feign 查询用户角色信息失败: {}", userId, throwable);
        return null;
    }
}
