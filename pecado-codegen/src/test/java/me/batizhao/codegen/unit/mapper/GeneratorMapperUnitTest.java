package me.batizhao.codegen.unit.mapper;

import lombok.extern.slf4j.Slf4j;
import me.batizhao.codegen.mapper.GeneratorMapper;
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
public class GeneratorMapperUnitTest extends BaseMapperUnitTest {

    @Autowired
    GeneratorMapper generatorMapper;

    @Test
    public void testSelectColumns() {
        List<Map<String, String>> result = generatorMapper.selectColumns("log");

        log.info("table: {}", result);

        assertThat(result.get(0).get("columnName"), equalTo("id"));
    }
}
