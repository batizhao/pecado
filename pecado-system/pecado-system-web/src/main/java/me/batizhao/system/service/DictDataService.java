package me.batizhao.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.batizhao.system.domain.DictData;

/**
 * 字典接口类
 *
 * @author batizhao
 * @since 2021-02-08
 */
public interface DictDataService extends IService<DictData> {

    /**
     * 通过id查询字典
     * @param id id
     * @return DictData
     */
    DictData findById(Long id);

    /**
     * 添加或编辑字典
     * @param dictData 字典
     * @return DictData
     */
    DictData saveOrUpdateDictData(DictData dictData);

}
