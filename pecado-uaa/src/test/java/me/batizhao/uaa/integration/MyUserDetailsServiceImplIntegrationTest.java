package me.batizhao.uaa.integration;

import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.exception.WebExceptionHandler;
import me.batizhao.common.security.feign.PecadoFeignErrorDecoder;
import me.batizhao.uaa.PecadoUaaApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author batizhao
 * @since 2020-02-29
 */
//@EnabledIf(expression = "#{environment['spring.profiles.active'] == 'integration'}", loadContext = true)
@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = PecadoUaaApplication.class)
@AutoConfigureMockMvc
@Import({WebExceptionHandler.class, PecadoFeignErrorDecoder.class})
@Slf4j
@Tag("integration")
public class MyUserDetailsServiceImplIntegrationTest {

    @Autowired
    private UserDetailsService userDetailsService;

    @Test
    public void givenUserName_whenFindUser_thenSuccess() {
        String username = "admin";
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        log.debug("userDetails: {}", userDetails);
        assertThat(userDetails.getUsername(), equalTo(username));

        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        log.debug("authorities: {}", authorities);
        assertThat(authorities, hasSize(2));

        List<String> list = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        assertThat(list, hasItems("ROLE_ADMIN", "ROLE_USER"));
    }

    @Test
    public void givenUserName_whenFindUser_thenUsernameNotFoundException() {
        Assertions.assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("xxxx"));
    }
}