package me.batizhao.uaa.unit;

import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.constant.SecurityConstants;
import me.batizhao.common.core.util.R;
import me.batizhao.ims.api.domain.Role;
import me.batizhao.ims.api.domain.User;
import me.batizhao.ims.api.feign.UserFeignService;
import me.batizhao.ims.api.vo.UserInfoVO;
import me.batizhao.uaa.security.PecadoUserDetailsService;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * @author batizhao
 * @since 2020-02-29
 */
@ExtendWith(SpringExtension.class)
@Slf4j
@Tag("unit")
public class PecadoUserDetailsServiceUnitTest {

    @MockBean
    private UserFeignService userFeignService;

    @TestConfiguration
    static class TestContextConfiguration {
        @Bean
        public UserDetailsService userDetailsService() {
            return new PecadoUserDetailsService();
        }
    }

    @Autowired
    private UserDetailsService userDetailsService;

    private List<Role> roleList;

    /**
     * Prepare test data.
     */
    @BeforeEach
    public void setUp() {
        roleList = new ArrayList<>();
        roleList.add(new Role().setId(1L).setCode("admin"));
        roleList.add(new Role().setId(2L).setCode("common"));
    }

    @Test
    public void givenUserName_whenFindUser_thenSuccess() {
        String username = "zhangsan";
        User user_test_data = new User().setId(1L).setUsername(username).setPassword("123456");

        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setUser(user_test_data);
        userInfoVO.setRoles(roleList.stream().map(Role::getCode).collect(Collectors.toList()));
        userInfoVO.setPermissions(roleList.stream().map(Role::getCode).collect(Collectors.toList()));

        R<UserInfoVO> userR = R.ok(userInfoVO);

        when(userFeignService.loadUserByUsername(username, SecurityConstants.FROM_IN))
                .thenReturn(userR);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        log.debug("userDetails: {}", userDetails);
        MatcherAssert.assertThat(userDetails.getUsername(), Matchers.equalTo(username));

        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        log.debug("authorities: {}", authorities);
        MatcherAssert.assertThat(authorities, Matchers.hasSize(2));

        List<String> list = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        MatcherAssert.assertThat(list, Matchers.hasItems("admin", "common"));
    }

    @Test
    public void givenUserName_whenFindUser_thenUsernameNotFoundException() {
        R<User> userR = R.ok();

        doReturn(userR).when(userFeignService).loadUserByUsername(anyString(), anyString());

        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("xxxx"));

        verify(userFeignService).loadUserByUsername(anyString(), anyString());
    }

    @Test
    public void givenUserName_whenFindUserRoles_thenRoleIsEmpty() {
        String username = "zhangsan";
        User user_test_data = new User().setId(1L).setUsername(username).setPassword("123456");

        roleList.clear();

        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setUser(user_test_data);

        R<UserInfoVO> userR = R.ok(userInfoVO);

        when(userFeignService.loadUserByUsername(username, SecurityConstants.FROM_IN))
                .thenReturn(userR);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        verify(userFeignService).loadUserByUsername(anyString(), anyString());

        log.debug("userDetails: {}", userDetails);
        MatcherAssert.assertThat(userDetails.getUsername(), Matchers.equalTo(username));

        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        log.debug("authorities: {}", authorities);
        MatcherAssert.assertThat(authorities, Matchers.hasSize(0));
    }
}