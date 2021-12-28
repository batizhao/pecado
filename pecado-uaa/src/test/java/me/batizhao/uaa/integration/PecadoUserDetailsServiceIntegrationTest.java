package me.batizhao.uaa.integration;

import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.domain.PecadoUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author batizhao
 * @since 2020-02-29
 */
@Slf4j
public class PecadoUserDetailsServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private UserDetailsService userDetailsService;

    @Test
    public void givenUserName_whenFindUser_thenSuccess() {
        String username = "admin";
        PecadoUser user = (PecadoUser) userDetailsService.loadUserByUsername(username);

        log.info("user: {}", user);
        assertThat(user.getUsername(), equalTo(username));

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        log.info("authorities: {}", authorities);
        assertThat(authorities.size(), greaterThan(2));

        List<String> list = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        assertThat(list, hasItems("ROLE_ADMIN", "ROLE_USER"));
    }

    @Test
    public void givenUserName_whenFindUser_thenUsernameNotFoundException() {
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("xxxx"));
    }
}