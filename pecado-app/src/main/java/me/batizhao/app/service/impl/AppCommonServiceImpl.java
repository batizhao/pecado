package me.batizhao.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.batizhao.app.domain.AppCommon;
import me.batizhao.app.mapper.AppCommonMapper;
import me.batizhao.app.service.AppCommonService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p> 常用应用服务实现层 </p>
 *
 * @author wws
 * @since 2022-05-16 18:59
 */
@Service
public class AppCommonServiceImpl extends ServiceImpl<AppCommonMapper, AppCommon> implements AppCommonService {

    /**
     * 创建查询表达式
     * @param appCommon 常用应用查询参数
     * @return LambdaQueryWrapper<AppCommon>
     */
    private LambdaQueryWrapper<AppCommon> createAppCommonLambda(AppCommon appCommon){
        LambdaQueryWrapper<AppCommon> wrapper = Wrappers.lambdaQuery();

        if (StringUtils.isNotBlank(appCommon.getName())) {
            wrapper.like(AppCommon::getName, appCommon.getName());
        }

        return wrapper;
    }
    
    @Override
    public IPage<AppCommon> findAppCommon(Page<AppCommon> page, AppCommon appCommon) {
        LambdaQueryWrapper<AppCommon> wrapper  = createAppCommonLambda(appCommon);
        return baseMapper.selectPage(page, wrapper);
    }

    @Override
    public List<AppCommon> findAppCommon(AppCommon appCommon) {
        LambdaQueryWrapper<AppCommon> wrapper  = createAppCommonLambda(appCommon);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public AppCommon saveOrUpdateAppCommon(AppCommon appCommon) {
        if (appCommon.getId() == null) {
            appCommon.setCreateTime(LocalDateTime.now());
            appCommon.setUpdateTime(LocalDateTime.now());
            baseMapper.insert(appCommon);
        } else {
            appCommon.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(appCommon);
        }
        return appCommon;
    }
}
