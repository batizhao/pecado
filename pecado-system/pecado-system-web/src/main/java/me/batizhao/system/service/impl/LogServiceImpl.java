package me.batizhao.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.batizhao.common.core.exception.NotFoundException;
import me.batizhao.system.api.domain.Log;
import me.batizhao.system.mapper.LogMapper;
import me.batizhao.system.service.LogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author batizhao
 * @since 2020-03-14
 **/
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements LogService {

    @Autowired
    private LogMapper logMapper;

    @Override
    public IPage<Log> findLogs(Page<Log> page, Log log) {
        LambdaQueryWrapper<Log> wrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(log.getClassName())) {
            wrapper.like(Log::getClassName, log.getClassName());
        }
        if (StringUtils.isNotBlank(log.getDescription())) {
            wrapper.like(Log::getDescription, log.getDescription());
        }
        return logMapper.selectPage(page, wrapper);
    }

    @Override
    public Log findById(Long id) {
        Log log = logMapper.selectById(id);

        if(log == null) {
            throw new NotFoundException(String.format("没有该记录 '%s'。", id));
        }

        return log;
    }
}
