package me.batizhao.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.batizhao.common.core.exception.NotFoundException;
import me.batizhao.system.api.domain.Config;
import me.batizhao.system.mapper.ConfigMapper;
import me.batizhao.system.service.ConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 系统配置接口实现类
 *
 * @author batizhao
 * @since 2022-05-16
 */
@Service
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements ConfigService {

    @Autowired
    private ConfigMapper configMapper;

    @Override
    public IPage<Config> findConfigs(Page<Config> page, Config config) {
        LambdaQueryWrapper<Config> wrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(config.getName())) {
            wrapper.like(Config::getName, config.getName());
        }
        if (StringUtils.isNotBlank(config.getCode())) {
            wrapper.like(Config::getCode, config.getCode());
        }
        return configMapper.selectPage(page, wrapper);
    }

    @Override
    public List<Config> findConfigs(Config config) {
        LambdaQueryWrapper<Config> wrapper = Wrappers.lambdaQuery();
        return configMapper.selectList(wrapper);
    }

    @Override
    public Config findById(Integer id) {
        Config config = configMapper.selectById(id);

        if(config == null) {
            throw new NotFoundException(String.format("Record not found '%s'。", id));
        }

        return config;
    }

    @Override
    @Transactional
    public Config saveOrUpdateConfig(Config config) {
        if (config.getId() == null) {
            config.setCreateTime(LocalDateTime.now());
            config.setUpdateTime(LocalDateTime.now());
            configMapper.insert(config);
        } else {
            config.setUpdateTime(LocalDateTime.now());
            configMapper.updateById(config);
        }

        return config;
    }


    @Override
    @Transactional
    public Boolean updateStatus(Config config) {
        LambdaUpdateWrapper<Config> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(Config::getId, config.getId()).set(Config::getStatus, config.getStatus());
        return configMapper.update(null, wrapper) == 1;
    }

}
