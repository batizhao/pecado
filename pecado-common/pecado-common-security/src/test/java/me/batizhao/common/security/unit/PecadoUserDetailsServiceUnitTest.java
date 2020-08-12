package me.batizhao.common.security.unit;

import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.constant.SecurityConstants;
import me.batizhao.common.core.util.ResponseInfo;
import me.batizhao.ims.api.feign.UserFeignService;
import me.batizhao.ims.api.vo.RoleVO;
import me.batizhao.ims.api.vo.UserVO;
import me.batizhao.common.security.component.PecadoUserDetailsService;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
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

    private List<RoleVO> roleList;

    /**
     * Prepare test data.
     */
    @BeforeEach
    public void setUp() {
        roleList = new ArrayList<>();
        roleList.add(new RoleVO().setId(1L).setCode("admin"));
        roleList.add(new RoleVO().setId(2L).setCode("common"));
    }

    @Test
    public void givenUserName_whenFindUser_thenSuccess() {
        String username = "zhangsan";
        UserVO user_test_data = new UserVO().setId(1L).setUsername(username).setPassword("123456");
        user_test_data.setRoleList(roleList);

        ResponseInfo<UserVO> userResponseInfo = ResponseInfo.ok(user_test_data);

        Mockito.when(userFeignService.loadUserByUsername(username, SecurityConstants.FROM_IN))
                .thenReturn(userResponseInfo);

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
        ResponseInfo<UserVO> userResponseInfo = ResponseInfo.ok();

        Mockito.doReturn(userResponseInfo).when(userFeignService).loadUserByUsername(ArgumentMatchers.anyString(), ArgumentMatchers.anyString());

        Assertions.assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("xxxx"));

        Mockito.verify(userFeignService).loadUserByUsername(ArgumentMatchers.anyString(), ArgumentMatchers.anyString());
    }

    @Test
    public void givenUserName_whenFindUserRoles_thenRoleIsEmpty() {
        String username = "zhangsan";
        UserVO user_test_data = new UserVO().setId(1L).setUsername(username).setPassword("123456");

        roleList.clear();
        user_test_data.setRoleList(roleList);

        ResponseInfo<UserVO> userResponseInfo = ResponseInfo.ok(user_test_data);

        Mockito.when(userFeignService.loadUserByUsername(username, SecurityConstants.FROM_IN))
                .thenReturn(userResponseInfo);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        Mockito.verify(userFeignService).loadUserByUsername(ArgumentMatchers.anyString(), ArgumentMatchers.anyString());

        log.debug("userDetails: {}", userDetails);
        MatcherAssert.assertThat(userDetails.getUsername(), Matchers.equalTo(username));

        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        log.debug("authorities: {}", authorities);
        MatcherAssert.assertThat(authorities, Matchers.hasSize(0));
    }
}