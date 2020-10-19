package me.batizhao.dp.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import me.batizhao.dp.domain.Ds;

/**
 * 数据源
 *
 * @author batizhao
 * @since 2020-10-19
 */
public interface DsService extends IService<Ds> {

    /**
     * 分页查询
     * @param page 分页对象
     * @param ds 数据源
     * @return IPage<Ds>
     */
    IPage<Ds> findDss(Page<Ds> page, Ds ds);

    /**
     * 通过id查询数据源
     * @param id id
     * @return Ds
     */
    Ds findById(Integer id);

    /**
     * 添加或修改数据源
     * @param ds 数据源
     * @return ResponseInfo
     */
     Ds saveOrUpdateDs(Ds ds);

}
