package me.batizhao.common.security.component;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;

public class PecadoUser extends User {

    /**
     * 用户ID
     */
    @Getter
    private Long userId;

    /**
     * 部门ID
     */
    @Getter
    private List<String> deptIds;

    /**
     * 角色ID
     */
    @Getter
    private List<String> roleIds;

    public PecadoUser(Long userId, List<String> deptIds, List<String> roleIds, String username, String password, boolean enabled,
                      boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,
                      Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.userId = userId;
        this.deptIds = deptIds;
        this.roleIds = roleIds;
    }

}
