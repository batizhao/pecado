package me.batizhao.common.security.component;

import me.batizhao.common.core.constant.SecurityConstants;
import me.batizhao.common.core.util.ResponseInfo;
import me.batizhao.ims.api.domain.Role;
import me.batizhao.ims.api.domain.User;
import me.batizhao.ims.api.feign.UserFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author batizhao
 * @since 2020-02-29
 */
@Component
public class PecadoUserDetailsService implements UserDetailsService {

    @Autowired
    private UserFeignService userFeignService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        ResponseInfo<User> userData = userFeignService.loadUserByUsername(username, SecurityConstants.FROM_IN);

        if (userData == null || null == userData.getData()) {
            throw new UsernameNotFoundException(String.format("没有该用户 '%s'。", username));
        }

        User user = userData.getData();
        List<Role> roles = user.getRoleList();
        Set<GrantedAuthority> authorities = new HashSet<>();
        roles.forEach(r -> authorities.add(new SimpleGrantedAuthority(r.getCode())));

        //TODO: The second param to user.getDeptId
        return new PecadoUser(user.getId(), user.getId(), user.getUsername(), user.getPassword(),
                true,
                true,
                true,
                true,
                authorities);
    }
}
