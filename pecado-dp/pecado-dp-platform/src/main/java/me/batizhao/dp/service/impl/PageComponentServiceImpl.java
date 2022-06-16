package me.batizhao.dp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.batizhao.dp.domain.PageComponent;
import me.batizhao.dp.mapper.PageComponentMapper;
import me.batizhao.dp.service.PageComponentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p> 组件模板实现层 </p>
 *
 * @author wws
 * @since 2022-05-13 11:06
 */
@Service
public class PageComponentServiceImpl extends ServiceImpl<PageComponentMapper, PageComponent> implements PageComponentService {

    /**
     * 创建组件模板查询表达式
     * @param component 组件模板查询参数
     * @return LambdaQueryWrapper<Components>
     */
    private LambdaQueryWrapper<PageComponent> createComponentsLambda(PageComponent component){
        LambdaQueryWrapper<PageComponent> wrapper = Wrappers.lambdaQuery();

        if (StringUtils.isNotBlank(component.getName())) {
            wrapper.like(PageComponent::getName, component.getName());
        }

        if (StringUtils.isNotBlank(component.getType())) {
            wrapper.eq(PageComponent::getType, component.getType());
        }

        if(StringUtils.isNotBlank(component.getModel())){
            wrapper.eq(PageComponent::getModel, component.getModel());
        }
        return wrapper;
    }
    
    @Override
    public Page<PageComponent> findComponentsTables(Page<PageComponent> page, PageComponent pageComponent) {
        LambdaQueryWrapper<PageComponent> wrapper  = createComponentsLambda(pageComponent);
        return baseMapper.selectPage(page, wrapper);
    }

    @Override
    public List<PageComponent> findComponentsTable(PageComponent pageComponent) {
        return baseMapper.selectList(createComponentsLambda(pageComponent));
    }

    @Override
    public PageComponent saveOrUpdateComponentsTable(PageComponent pageComponent) {
        if (pageComponent.getId() == null) {
            pageComponent.setCreateTime(LocalDateTime.now());
            pageComponent.setUpdateTime(LocalDateTime.now());
            baseMapper.insert(pageComponent);
        } else {
            pageComponent.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(pageComponent);
        }
        return pageComponent;
    }
}
