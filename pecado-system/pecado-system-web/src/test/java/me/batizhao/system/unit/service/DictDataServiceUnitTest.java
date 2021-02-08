package me.batizhao.system.unit.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.batizhao.system.domain.DictData;
import me.batizhao.system.mapper.DictDataMapper;
import me.batizhao.system.service.DictDataService;
import me.batizhao.system.service.impl.DictDataServiceImpl;
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
 * 字典
 *
 * @author batizhao
 * @since 2021-02-08
 */
public class DictDataServiceUnitTest extends BaseServiceUnitTest {

    /**
     * Spring Boot 提供了 @TestConfiguration 注释，可用于 src/test/java 中的类，以指示不应通过扫描获取它们。
     */
    @TestConfiguration
    static class TestContextConfiguration {
        @Bean
        public DictDataService dictDataService() {
            return new DictDataServiceImpl();
        }
    }

    @MockBean
    private DictDataMapper dictDataMapper;

    @Autowired
    private DictDataService dictDataService;

    private List<DictData> dictDataList;
    private Page<DictData> dictDataPageList;

    /**
     * Prepare test data.
     */
    @BeforeEach
    public void setUp() {
        dictDataList = new ArrayList<>();
        dictDataList.add(new DictData().setId(1L).setLabel("zhangsan"));
        dictDataList.add(new DictData().setId(2L).setLabel("lisi"));
        dictDataList.add(new DictData().setId(3L).setLabel("wangwu"));

        dictDataPageList = new Page<>();
        dictDataPageList.setRecords(dictDataList);
    }

    @Test
    public void givenDictDataId_whenFindDictData_thenSuccess() {
        when(dictDataMapper.selectById(1L))
                .thenReturn(dictDataList.get(0));

        DictData dictData = dictDataService.findById(1L);

        assertThat(dictData.getLabel(), equalTo("zhangsan"));
    }

    @Test
    public void givenDictDataJson_whenSaveOrUpdateDictData_thenSuccess() {
        DictData dictData_test_data = new DictData().setLabel("zhaoliu");

        // insert 不带 id
        doReturn(1).when(dictDataMapper).insert(any(DictData.class));

        DictData dictData = dictDataService.saveOrUpdateDictData(dictData_test_data);

        verify(dictDataMapper).insert(any(DictData.class));

        // update 需要带 id
        doReturn(1).when(dictDataMapper).updateById(any(DictData.class));

        dictData = dictDataService.saveOrUpdateDictData(dictDataList.get(0));

        verify(dictDataMapper).updateById(any(DictData.class));
    }


}