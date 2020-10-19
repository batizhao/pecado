package me.batizhao.dp.unit.mapper;

import lombok.extern.slf4j.Slf4j;
import me.batizhao.dp.mapper.CodeGeneratorMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * @author batizhao
 * @date 2020/10/10
 */
@Slf4j
public class CodeGeneratorMapperUnitTest extends BaseMapperUnitTest {

    @Autowired
    CodeGeneratorMapper codeGeneratorMapper;

    @Test
    public void testSelectColumns() {
        List<Map<String, String>> result = codeGeneratorMapper.selectColumns("ds");

        log.info("table: {}", result);

        assertThat(result.get(0).get("columnName"), equalTo("id"));
    }

    @Test
    public void testSelectTable() {
        Map<String, String> result = codeGeneratorMapper.selectTable("ds");

        log.info("table: {}", result);

        assertThat(result.get("tableComment"), equalTo("数据源"));
    }
}
