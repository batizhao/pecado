package me.batizhao.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import me.batizhao.system.domain.Log;

/**
 * @author batizhao
 * @since 2020-03-14
 **/
public interface LogService extends IService<Log> {

    IPage<Log> findLogs(Page<Log> page, Log log);

}
