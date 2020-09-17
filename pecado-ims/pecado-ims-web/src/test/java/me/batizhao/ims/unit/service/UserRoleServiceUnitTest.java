package me.batizhao.ims.unit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.ims.domain.UserRole;
import me.batizhao.ims.mapper.UserRoleMapper;
import me.batizhao.ims.service.UserRoleService;
import me.batizhao.ims.service.iml.UserRoleServiceIml;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

/**
 * @author batizhao
 * @since 2020-09-14
 */
@Slf4j
public class UserRoleServiceUnitTest extends BaseServiceUnitTest {

    /**
     * Spring Boot 提供了 @TestConfiguration 注释，可用于 src/test/java 中的类，以指示不应通过扫描获取它们。
     */
    @TestConfiguration
    static class TestContextConfiguration {
        @Bean
        public UserRoleService userRoleService() {
            return new UserRoleServiceIml();
        }
    }

    @MockBean
    private UserRoleMapper userRoleMapper;

    @Autowired
    private UserRoleService userRoleService;

    @SpyBean
    private IService service;

    /**
     * Prepare test data.
     */
    @BeforeEach
    public void setUp() {

    }

    @Test
    public void givenUserAndRoles_whenUpdate_thenSuccess() {
        when(userRoleMapper.deleteByUserId(anyLong()))
                .thenReturn(1);

        doReturn(true).when(service).saveBatch(anyCollection());

        Boolean b = userRoleService.updateUserRoles(1L, Collections.singletonList("1"));

        assertThat(b, equalTo(true));

        doReturn(false).when(service).saveBatch(anyCollection());

        b = userRoleService.updateUserRoles(1L, Collections.singletonList("1"));

        assertThat(b, equalTo(false));
    }
}