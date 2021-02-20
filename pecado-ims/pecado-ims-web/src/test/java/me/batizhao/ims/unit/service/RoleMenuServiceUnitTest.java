package me.batizhao.ims.unit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.ims.mapper.RoleMenuMapper;
import me.batizhao.ims.service.RoleMenuService;
import me.batizhao.ims.service.impl.RoleMenuServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;

import java.util.Collections;

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
public class RoleMenuServiceUnitTest extends BaseServiceUnitTest {

    /**
     * Spring Boot 提供了 @TestConfiguration 注释，可用于 src/test/java 中的类，以指示不应通过扫描获取它们。
     */
    @TestConfiguration
    static class TestContextConfiguration {
        @Bean
        public RoleMenuService roleMenuService() {
            return new RoleMenuServiceImpl();
        }
    }

    @MockBean
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private RoleMenuService roleMenuService;

    @SpyBean
    private IService service;

    /**
     * Prepare test data.
     */
    @BeforeEach
    public void setUp() {

    }

//    @Test
//    public void givenUserAndRoles_whenUpdate_thenSuccess() {
//        when(roleMenuMapper.deleteByRoleId(anyLong()))
//                .thenReturn(1);
//
//        doReturn(true).when(service).saveBatch(anyCollection());
//
//        Boolean b = roleMenuService.updateRoleMenus(1L, Collections.singletonList("1"));
//
//        assertThat(b, equalTo(true));
//
//        doReturn(false).when(service).saveBatch(anyCollection());
//
//        b = roleMenuService.updateRoleMenus(1L, Collections.singletonList("1"));
//
//        assertThat(b, equalTo(false));
//    }
}