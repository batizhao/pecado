package me.batizhao.dp.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import me.batizhao.dp.domain.CodeMeta;

import java.util.List;

/**
 * 生成代码元数据表
 *
 * @author batizhao
 * @since 2021-02-01
 */
public interface CodeMetaService extends IService<CodeMeta> {

//    /**
//     * 分页查询
//     * @param page 分页对象
//     * @param codeMeta 生成代码元数据表
//     * @return IPage<CodeMeta>
//     */
//    IPage<CodeMeta> findCodeMetas(Page<CodeMeta> page, CodeMeta codeMeta);

//    /**
//     * 通过id查询生成代码元数据表
//     * @param id id
//     * @return CodeMeta
//     */
//    CodeMeta findById(Long id);

    /**
     * 通过 codeId 查询表信息
     * @param codeId
     * @return
     */
    List<CodeMeta> findByCodeId(Long codeId);

//    /**
//     * 添加或修改生成代码元数据表
//     * @param codeMeta 生成代码元数据表
//     * @return ResponseInfo
//     */
//    CodeMeta saveOrUpdateCodeMeta(CodeMeta codeMeta);

    /**
     * 查询表原始信息
     * @param tableName 表名
     * @param dsName 动态数据源名
     * @return
     */
    List<CodeMeta> findColumnsByTableName(String tableName, String dsName);

}
