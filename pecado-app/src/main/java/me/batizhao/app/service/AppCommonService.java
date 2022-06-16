package me.batizhao.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import me.batizhao.app.domain.AppCommon;

import java.util.List;

/**
 * <p> 常用应用服务 </p>
 *
 * @author wws
 * @since 2022-05-16 18:59
 */
public interface AppCommonService extends IService<AppCommon> {

    /**
     * 分页查询常用应用表
     * @param page 分页对象
     * @param appCommon 常用应用表
     * @return IPage<AppCommon>
     */
    IPage<AppCommon> findAppCommon(Page<AppCommon> page, AppCommon appCommon);

    /**
     * 查询常用应用表
     * @param appCommon 常用应用表
     * @return List<AppTable>
     */
    List<AppCommon> findAppCommon(AppCommon appCommon);


    /**
     * 添加或编辑常用应用表
     * @param appCommon 常用应用表
     * @return AppCommon
     */
    AppCommon saveOrUpdateAppCommon(AppCommon appCommon);
}
