package me.batizhao.dp.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import me.batizhao.dp.domain.Code;
import me.batizhao.dp.domain.GenConfig;

import java.util.List;
import java.util.Map;

/**
 * @author batizhao
 * @date 2020/10/10
 */
public interface CodeService extends IService<Code> {

    /**
     * 分页查询
     * @param page 分页对象
     * @param code 生成代码
     * @return IPage<Code>
     */
    IPage<Code> findCodes(Page<Code> page, Code code);

    /**
     * 查询所有
     * @return List<Code>
     */
    List<Code> findCodes();

    /**
     * 通过id查询生成代码
     * @param id id
     * @return Code
     */
    Code findById(Long id);

    /**
     * 添加或修改生成代码
     * @param code 生成代码
     * @return ResponseInfo
     */
    Code saveOrUpdateCode(Code code);

    /**
     * 删除
     * @param ids
     * @return
     */
    Boolean deleteByIds(List<Long> ids);

    /**
     * 查询数据源下的所有表
     * @param page 分页对象
     * @param dsName 数据源
     * @return IPage<Code>
     */
    IPage<Code> findTables(Page<Code> page, Code code, String dsName);

    /**
     * 导入选中的表
     * @param codes
     * @return
     */
    Boolean importTables(List<Code> codes);

    /**
     * 生成代码
     * @param ids
     * @return byte[]
     */
    byte[] downloadCode(List<Long> ids);

    Boolean generateCode(Long id);

    /**
     * 预览代码
     * @param id Code Id
     * @return
     */
    Map<String, String> previewCode(Long id);

    /**
     * 同步表元数据
     * @param id
     * @return
     */
    Boolean syncCodeMeta(Long id);

}
