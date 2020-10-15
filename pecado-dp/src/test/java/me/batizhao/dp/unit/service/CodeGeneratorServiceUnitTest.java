package me.batizhao.dp.unit.service;

import lombok.extern.slf4j.Slf4j;
import me.batizhao.dp.domain.GenConfig;
import me.batizhao.dp.mapper.CodeGeneratorMapper;
import me.batizhao.dp.service.CodeGeneratorService;
import me.batizhao.dp.service.impl.CodeGeneratorServiceIml;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @author batizhao
 * @since 2020-02-08
 */
@Slf4j
public class CodeGeneratorServiceUnitTest extends BaseServiceUnitTest {

    /**
     * Spring Boot 提供了 @TestConfiguration 注释，可用于 src/test/java 中的类，以指示不应通过扫描获取它们。
     */
    @TestConfiguration
    static class TestContextConfiguration {
        @Bean
        public CodeGeneratorService generatorService() {
            return new CodeGeneratorServiceIml();
        }
    }

    @MockBean
    private CodeGeneratorMapper codeGeneratorMapper;

    @Autowired
    private CodeGeneratorService codeGeneratorService;

    private List<Map<String, String>> result;

    /**
     * Prepare test data.
     */
    @BeforeEach
    public void setUp() {
        result = new ArrayList<>();
        result.add(Map.of("columnName","id", "columnType", "bigint"));
        result.add(Map.of("columnName","httpRequestMethod", "columnType", "varchar(255)"));
    }

    @Test
    public void givenTableName_whenSelectColumns_thenSuccess() {
        when(codeGeneratorMapper.selectColumns(anyString()))
                .thenReturn(result);

        when(codeGeneratorMapper.selectTable(anyString()))
                .thenReturn(Map.of("tableComment", "日志表", "tableName", "log"));

        byte[] result = codeGeneratorService.generateCode(new GenConfig().setTableName("log"));

        log.info("result: {}", result);

        assertThat(result.length, greaterThan(0));
    }

}