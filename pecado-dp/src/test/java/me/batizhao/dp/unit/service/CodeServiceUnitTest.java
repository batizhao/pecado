package me.batizhao.dp.unit.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.dp.domain.GenConfig;
import me.batizhao.dp.mapper.CodeMapper;
import me.batizhao.dp.service.CodeService;
import me.batizhao.dp.service.impl.CodeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @author batizhao
 * @since 2020-02-08
 */
@Slf4j
public class CodeServiceUnitTest extends BaseServiceUnitTest {

    /**
     * Spring Boot 提供了 @TestConfiguration 注释，可用于 src/test/java 中的类，以指示不应通过扫描获取它们。
     */
    @TestConfiguration
    static class TestContextConfiguration {
        @Bean
        public CodeService generatorService() {
            return new CodeServiceImpl();
        }
    }

    @MockBean
    private CodeMapper codeMapper;

    @Autowired
    private CodeService codeService;

    private List<Map<String, String>> list;
    private IPage<Map<String, String>> codePageList;

    /**
     * Prepare test data.
     */
    @BeforeEach
    public void setUp() {
        list = new ArrayList<>();
        list.add(Map.of("tableName","user", "tableCollation", "utf8"));
        list.add(Map.of("tableName","role", "tableCollation", "uft8mb4"));

        codePageList = new Page<>();
        codePageList.setRecords(list);
    }

    @Test
    public void givenNothing_whenFindTables_thenSuccess() {
        when(codeMapper.selectTableByDs(any(Page.class), anyString()))
                .thenReturn(codePageList);

        IPage<Map<String, String>> page = codeService.findTables(new Page<>(), "", "ims");

        log.info("result: {}", page.getRecords());

        assertThat(page.getRecords().size(), is(2));
        assertThat(page.getRecords().get(0).get("dsName"), equalTo("ims"));
    }

    @Test
    public void givenTableName_whenSelectColumns_thenSuccess() {
        when(codeMapper.selectColumnsByTableName(anyString(), anyString()))
                .thenReturn(list);

        when(codeMapper.selectMetaByTableName(anyString(), anyString()))
                .thenReturn(Map.of("tableComment", "日志表", "tableName", "log"));

        byte[] data = codeService.generateCode(new GenConfig().setTableName("log").setAuthor("batizhao").setComments("comment")
                .setModuleName("system").setPackageName("me.batizhao").setTablePrefix("ims_").setDsName("ims"));

        log.info("data: {}", data);

        assertThat(data.length, greaterThan(0));
    }

}