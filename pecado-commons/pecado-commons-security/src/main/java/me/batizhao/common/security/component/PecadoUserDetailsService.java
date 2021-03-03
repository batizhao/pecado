package me.batizhao.common.security.component;

import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.constant.SecurityConstants;
import me.batizhao.common.core.util.ResponseInfo;
import me.batizhao.ims.api.domain.User;
import me.batizhao.ims.api.feign.UserFeignService;
import me.batizhao.ims.api.vo.UserInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author batizhao
 * @since 2020-02-29
 */
@Component
@Slf4j
public class PecadoUserDetailsService implements UserDetailsService {

    @Autowired
    private UserFeignService userFeignService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        ResponseInfo<UserInfoVO> userData = userFeignService.loadUserByUsername(username, SecurityConstants.FROM_IN);

        if (userData == null || null == userData.getData()) {
            throw new UsernameNotFoundException(String.format("没有该用户 '%s'。", username));
        }

        UserInfoVO userInfoVO = userData.getData();
        log.info("UserDetails: {}", userInfoVO);

        User user = userInfoVO.getUser();
        Set<String> authSet = new HashSet<>();
        if (!CollectionUtils.isEmpty(userInfoVO.getRoles())) {
            // 获取角色
            authSet.addAll(userInfoVO.getRoles());
            // 获取资源
            authSet.addAll(userInfoVO.getPermissions());
        }

        Collection<? extends GrantedAuthority> authorities = AuthorityUtils
                .createAuthorityList(authSet.toArray(new String[0]));

        //TODO: The second param to user.getDeptId
        return new PecadoUser(user.getId(), user.getId(), user.getUsername(), user.getPassword(),
                true,
                true,
                true,
                true,
                authorities);
    }
}
