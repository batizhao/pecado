package me.batizhao.ims.api.feign.fallback;

import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.util.ResponseInfo;
import me.batizhao.ims.api.feign.UserFeignService;
import me.batizhao.ims.api.vo.RoleVO;
import me.batizhao.ims.api.vo.UserVO;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author batizhao
 * @since 2020-03-13
 **/
@Slf4j
@Component
public class UserServiceFallback implements UserFeignService {

    @Override
    public ResponseInfo<UserVO> loadUserByUsername(String username, String from) {
        log.error("feign 查询用户信息失败: {}", username);
        return null;
    }

    @Override
    public ResponseInfo<List<RoleVO>> findRolesByUserId(Long userId) {
        log.error("feign 查询用户角色信息失败: {}", userId);
        return null;
    }
}
