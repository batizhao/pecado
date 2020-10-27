package me.batizhao.dp.unit.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.dp.mapper.CodeMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

/**
 * @author batizhao
 * @date 2020/10/10
 */
@Slf4j
public class CodeMapperUnitTest extends BaseMapperUnitTest {

    @Autowired
    CodeMapper codeMapper;

    @Test
    public void testSelectTableByDs() {
        IPage<List<Map<String, String>>> result = codeMapper.selectTableByDs(new Page<>(), "");

        log.info("table: {}", result.getRecords());

        assertThat(result.getRecords().size(), greaterThan(0));
    }

    @Test
    public void testSelectColumnsByTableName() {
        List<Map<String, String>> result = codeMapper.selectColumnsByTableName("ds");

        log.info("table: {}", result);

        assertThat(result.get(0).get("columnName"), equalTo("id"));
    }

    @Test
    public void testSelectMetaByTableName() {
        Map<String, String> result = codeMapper.selectMetaByTableName("ds");

        log.info("table: {}", result);

        assertThat(result.get("tableComment"), equalTo("数据源"));
    }
}
