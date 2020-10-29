package me.batizhao.dp.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.batizhao.dp.domain.GenConfig;

import java.util.Map;

/**
 * @author batizhao
 * @date 2020/10/10
 */
public interface CodeService {

    /**
     * 分页查询
     * @param page 分页对象
     * @param dsName 数据源
     * @return IPage<GenConfig>
     */
    IPage<Map<String, String>> findTables(Page<Map<String, String>> page, String tableName, String dsName);

    /**
     * 生成代码
     * @param genConfig
     * @return byte[]
     */
    byte[] generateCode(GenConfig genConfig);

}
