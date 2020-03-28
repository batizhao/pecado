package me.batizhao.uaa;

import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.exception.WebExceptionHandler;
import me.batizhao.common.security.feign.PecadoFeignErrorDecoder;
import me.batizhao.ims.api.feign.UserFeignService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

/**
 * Will work only when you set spring.profiles.active as a JVM property, as: -Dspring.profiles.active="test"
 * @IfProfileValue just ignores spring.profiles.active from application.properties/yml.
 *
 * @author batizhao
 * @since 2020-02-29
 */
@ActiveProfiles("test")
@IfProfileValue(name = "spring.profiles.active", value = "test")
@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = PecadoUaaApplication.class)
@AutoConfigureMockMvc
@Import({WebExceptionHandler.class, PecadoFeignErrorDecoder.class})
@Slf4j
public class MyUserDetailsServiceImplIntegrationTest {

    @Autowired
    private UserFeignService userFeignService;
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

    @Test(expected = UsernameNotFoundException.class)
    public void givenUserName_whenFindUser_thenUsernameNotFoundException() {
        userDetailsService.loadUserByUsername("xxxx");
        verify(userFeignService).loadUserByUsername(any(), any());
    }
}