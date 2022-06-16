package me.batizhao.dp.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import me.batizhao.dp.domain.PageComponent;

import java.util.List;

/**
 * <p> 组件模板服务 </p>
 *
 * @author wws
 * @since 2022-05-13 11:04
 */
public interface PageComponentService extends IService<PageComponent> {

    /**
     * 分页查询组件模板表
     * @param page 分页对象
     * @param pageComponent 组件模板表对象
     * @return IPage<Components>
     */
    Page<PageComponent> findComponentsTables(Page<PageComponent> page, PageComponent pageComponent);

    /**
     * 查询组件模板表列表
     * @param pageComponent 组件模板表对象
     * @return List<Components>
     */
    List<PageComponent> findComponentsTable(PageComponent pageComponent);

    /**
     * 添加或编辑组件模板表
     * @param pageComponent 组件模板表对象
     * @return components
     */
    PageComponent saveOrUpdateComponentsTable(PageComponent pageComponent);
}
