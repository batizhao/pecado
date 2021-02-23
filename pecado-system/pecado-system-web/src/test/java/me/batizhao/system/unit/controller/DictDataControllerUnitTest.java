package me.batizhao.system.unit.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.batizhao.common.core.util.ResultEnum;
import me.batizhao.system.controller.DictDataController;
import me.batizhao.system.domain.DictData;
import me.batizhao.system.service.DictDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.text.StringContainsInOrder.stringContainsInOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 字典
 *
 * @author batizhao
 * @since 2021-02-08
 */
@WebMvcTest(DictDataController.class)
public class DictDataControllerUnitTest extends BaseControllerUnitTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    DictDataService dictDataService;

    private List<DictData> dictDataList;

    /**
     * Prepare test data.
     */
    @BeforeEach
    public void setUp() {
        dictDataList = new ArrayList<>();
        dictDataList.add(new DictData().setId(1L).setLabel("zhangsan"));
        dictDataList.add(new DictData().setId(2L).setLabel("lisi"));
        dictDataList.add(new DictData().setId(3L).setLabel("wangwu"));
    }

    @Test
    @WithMockUser
    public void givenNothing_whenFindAllDictData_thenSuccess() throws Exception {
        when(dictDataService.list()).thenReturn(dictDataList);

        mvc.perform(get("/dict/data"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(content().string(stringContainsInOrder("zhangsan", "lisi", "wangwu")))
                .andExpect(jsonPath("$.data", hasSize(3)))
                .andExpect(jsonPath("$.data[0].label", equalTo("zhangsan")));

        verify(dictDataService).list();
    }

    @Test
    @WithMockUser
    public void givenId_whenFindDictData_thenSuccess() throws Exception {
        Long id = 1L;

        when(dictDataService.findById(id)).thenReturn(dictDataList.get(0));

        mvc.perform(get("/dict/data/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.label").value("zhangsan"));

        verify(dictDataService).findById(anyLong());
    }

    @Test
    @WithMockUser
    public void givenJson_whenSaveDictData_thenSuccess() throws Exception {
        DictData requestBody = new DictData().setLabel("zhaoliu");

        when(dictDataService.saveOrUpdateDictData(any(DictData.class)))
                .thenReturn(dictDataList.get(0));

        mvc.perform(post("/dict/data").with(csrf())
                .content(objectMapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.id", equalTo(1)));

        verify(dictDataService).saveOrUpdateDictData(any(DictData.class));
    }

    @Test
    @WithMockUser
    public void givenJson_whenUpdateDictData_thenSuccess() throws Exception {
        DictData requestBody = new DictData().setId(2L).setLabel("zhaoliu");

        when(dictDataService.saveOrUpdateDictData(any(DictData.class)))
                .thenReturn(dictDataList.get(1));

        mvc.perform(post("/dict/data").with(csrf())
                .content(objectMapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.id", equalTo(2)));

        verify(dictDataService).saveOrUpdateDictData(any(DictData.class));
    }

    @Test
    @WithMockUser
    public void givenId_whenDeleteDictData_thenSuccess() throws Exception {
        when(dictDataService.removeByIds(anyList())).thenReturn(true);

        mvc.perform(delete("/dict/data").param("ids", "1,2").with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data").value(true));

        verify(dictDataService).removeByIds(anyList());
    }
}
