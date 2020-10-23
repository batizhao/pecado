package me.batizhao.dp.unit.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.dp.domain.Ds;
import me.batizhao.dp.mapper.DsMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * 数据源
 *
 * @author batizhao
 * @since 2020-10-19
 */
@Slf4j
public class DsMapperUnitTest extends BaseMapperUnitTest {

    @Autowired
    DsMapper dsMapper;

    @Test
    public void testSelectDsPage() {
        IPage<Ds> dss = dsMapper.selectDsPage(new Page<>(), new Ds());

        log.info("dss: {}", dss);

        assertThat(dss.getRecords().size(), greaterThan(0));
    }

}
