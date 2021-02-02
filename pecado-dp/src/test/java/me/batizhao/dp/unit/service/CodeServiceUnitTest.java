package me.batizhao.dp.unit.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.dp.domain.Code;
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
import static org.mockito.Mockito.*;

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

    private List<Code> codeList;
    private Page<Code> codePageList;

//    private List<Map<String, String>> list;
//    private Page<Map<String, String>> codePageList;

    /**
     * Prepare test data.
     */
    @BeforeEach
    public void setUp() {
        codeList = new ArrayList<>();
        codeList.add(new Code().setId(1L).setTableName("zhangsan"));
        codeList.add(new Code().setId(2L).setTableName("lisi"));
        codeList.add(new Code().setId(3L).setTableName("wangwu"));

        codePageList = new Page<>();
        codePageList.setRecords(codeList);

//        list = new ArrayList<>();
//        list.add(Map.of("tableName","user", "tableCollation", "utf8"));
//        list.add(Map.of("tableName","role", "tableCollation", "uft8mb4"));
//
//        codePageList = new Page<>();
//        codePageList.setRecords(list);
    }

    @Test
    public void givenNothing_whenFindAllCode_thenSuccess() {
        when(codeMapper.selectPage(any(Page.class), any(Wrapper.class)))
                .thenReturn(codePageList);

        IPage<Code> codes = codeService.findCodes(new Page<>(), new Code());

        assertThat(codes.getRecords(), iterableWithSize(3));
        assertThat(codes.getRecords(), hasItems(hasProperty("username", equalTo("zhangsan")),
                hasProperty("username", equalTo("lisi")),
                hasProperty("username", equalTo("wangwu"))));
    }

    @Test
    public void givenCodeId_whenFindCode_thenSuccess() {
        when(codeMapper.selectById(1L))
                .thenReturn(codeList.get(0));

        Code code = codeService.findById(1L);

        assertThat(code.getTableName(), equalTo("zhangsan"));
    }

    @Test
    public void givenCodeJson_whenSaveOrUpdateCode_thenSuccess() {
        Code code_test_data = new Code().setTableName("zhaoliu");

        // insert 不带 id
        doReturn(1).when(codeMapper).insert(any(Code.class));

        Code code = codeService.saveOrUpdateCode(code_test_data);

        verify(codeMapper).insert(any(Code.class));

        // update 需要带 id
        doReturn(1).when(codeMapper).updateById(any(Code.class));

        code = codeService.saveOrUpdateCode(codeList.get(0));

        verify(codeMapper).updateById(any(Code.class));
    }

//    @Test
//    public void givenNothing_whenFindTables_thenSuccess() {
//        when(codeMapper.selectTableByDs(any(Page.class), anyString()))
//                .thenReturn(codePageList);
//
//        IPage<Map<String, String>> page = codeService.findTables(new Page<>(), "", "ims");
//
//        log.info("result: {}", page.getRecords());
//
//        assertThat(page.getRecords().size(), is(2));
//        assertThat(page.getRecords().get(0).get("dsName"), equalTo("ims"));
//    }
//
//    @Test
//    public void givenTableName_whenSelectColumns_thenSuccess() {
//        when(codeMapper.selectColumnsByTableName(anyString(), anyString()))
//                .thenReturn(list);
//
//        when(codeMapper.selectMetaByTableName(anyString(), anyString()))
//                .thenReturn(Map.of("tableComment", "日志表", "tableName", "log"));
//
//        byte[] data = codeService.generateCode(new GenConfig().setTableName("log").setAuthor("batizhao").setComments("comment")
//                .setModuleName("system").setPackageName("me.batizhao").setTablePrefix("ims_").setDsName("ims"));
//
//        log.info("data: {}", data);
//
//        assertThat(data.length, greaterThan(0));
//    }

}