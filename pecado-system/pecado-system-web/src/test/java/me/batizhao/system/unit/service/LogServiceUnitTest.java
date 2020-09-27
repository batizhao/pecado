package me.batizhao.system.unit.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.batizhao.system.domain.Log;
import me.batizhao.system.mapper.LogMapper;
import me.batizhao.system.service.LogService;
import me.batizhao.system.service.iml.LogServiceIml;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @author batizhao
 * @date 2020/9/27
 */
public class LogServiceUnitTest extends BaseServiceUnitTest {

    /**
     * Spring Boot 提供了 @TestConfiguration 注释，可用于 src/test/java 中的类，以指示不应通过扫描获取它们。
     */
    @TestConfiguration
    static class TestContextConfiguration {
        @Bean
        public LogService logService() {
            return new LogServiceIml();
        }
    }

    @MockBean
    private LogMapper logMapper;

    @Autowired
    private LogService logService;

    private List<Log> logList;
    private IPage<Log> logPageList;

    /**
     * Prepare test data.
     */
    @BeforeEach
    public void setUp() {
        logList = new ArrayList<>();
        logList.add(new Log().setId(1L).setClassMethod("handleMenuTree4Me").setClassName("me.batizhao.ims.web.MenuController"));
        logList.add(new Log().setId(2L).setClassMethod("handleUserInfo").setClassName("me.batizhao.ims.web.UserController"));

        logPageList = new Page<>();
        logPageList.setRecords(logList);
    }

    @Test
    void givenNothing_whenFindPageLogs_thenSuccess() {
        when(logMapper.selectLogPage(any(Page.class), any(Log.class)))
                .thenReturn(logPageList);

        IPage<Log> logs = logService.findLogs(new Page<>(), new Log().setUsername("tom"));

        assertThat(logs.getRecords(), iterableWithSize(2));
        assertThat(logs.getRecords(), hasItems(hasProperty("classMethod", is("handleMenuTree4Me")),
                hasProperty("className", is("me.batizhao.ims.web.MenuController"))));

        assertThat(logs.getRecords(), containsInAnyOrder(allOf(hasProperty("classMethod", is("handleMenuTree4Me")),
                hasProperty("className", is("me.batizhao.ims.web.MenuController"))),
                allOf(hasProperty("classMethod", is("handleUserInfo")),
                        hasProperty("className", is("me.batizhao.ims.web.UserController")))));

    }
}
