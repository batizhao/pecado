package me.batizhao.dp.unit.service;

import com.baomidou.dynamic.datasource.creator.DataSourceCreator;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.batizhao.dp.domain.Ds;
import me.batizhao.dp.mapper.DsMapper;
import me.batizhao.dp.service.DsService;
import me.batizhao.dp.service.impl.DsServiceImpl;
import org.jasypt.encryption.StringEncryptor;
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
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;

/**
 * 数据源
 *
 * @author batizhao
 * @since 2020-10-19
 */
public class DsServiceUnitTest extends BaseServiceUnitTest {

    /**
     * Spring Boot 提供了 @TestConfiguration 注释，可用于 src/test/java 中的类，以指示不应通过扫描获取它们。
     */
    @TestConfiguration
    static class TestContextConfiguration {
        @Bean
        public DsService dsService() {
            return new DsServiceImpl();
        }
    }

    @MockBean
    private DsMapper dsMapper;
    @MockBean
    private DataSourceCreator dataSourceCreator;
    @MockBean
    private StringEncryptor stringEncryptor;

    @Autowired
    private DsService dsService;

    private List<Ds> dsList;
    private IPage<Ds> dsPageList;

    /**
     * Prepare test data.
     */
    @BeforeEach
    public void setUp() {
        dsList = new ArrayList<>();
        dsList.add(new Ds().setId(1).setUsername("zhangsan"));
        dsList.add(new Ds().setId(2).setUsername("lisi"));
        dsList.add(new Ds().setId(3).setUsername("wangwu"));

        dsPageList = new Page<>();
        dsPageList.setRecords(dsList);
    }

    @Test
    public void givenNothing_whenFindAllDs_thenSuccess() {
        when(dsMapper.selectDsPage(any(Page.class), any(Ds.class)))
                .thenReturn(dsPageList);

        IPage<Ds> dss = dsService.findDss(new Page<>(), new Ds());

        assertThat(dss.getRecords(), iterableWithSize(3));
        assertThat(dss.getRecords(), hasItems(hasProperty("username", equalTo("zhangsan")),
                hasProperty("username", equalTo("lisi")),
                hasProperty("username", equalTo("wangwu"))));
    }

    @Test
    public void givenDsId_whenFindDs_thenSuccess() {
        when(dsMapper.selectById(1))
                .thenReturn(dsList.get(0));

        Ds ds = dsService.findById(1);

        assertThat(ds.getUsername(), equalTo("zhangsan"));
    }

//    @Test
//    public void givenDsJson_whenSaveOrUpdateDs_thenSuccess() {
//        Ds ds_test_data = new Ds().setUsername("zhaoliu");
//
//        // insert 不带 id
//        doReturn(true).when(dsService).checkDataSource(any(Ds.class));
//        doReturn(1).when(dsMapper).insert(any(Ds.class));
//        doReturn("vxth7ibr6hlxbE362qiQGYtiGWOotkYp").when(stringEncryptor).encrypt(anyString());
//
//        Ds ds = dsService.saveOrUpdateDs(ds_test_data);
//
//        verify(dsMapper).insert(any(Ds.class));
//
//        // update 需要带 id
//        doReturn(1).when(dsMapper).updateById(any(Ds.class));
//
//        ds = dsService.saveOrUpdateDs(dsList.get(0));
//
//        verify(dsMapper).updateById(any(Ds.class));
//    }


}