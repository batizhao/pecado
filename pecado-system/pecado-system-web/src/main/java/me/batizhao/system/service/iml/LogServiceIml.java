package me.batizhao.system.service.iml;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.batizhao.system.domain.Log;
import me.batizhao.system.mapper.LogMapper;
import me.batizhao.system.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author batizhao
 * @since 2020-03-14
 **/
@Service
public class LogServiceIml extends ServiceImpl<LogMapper, Log> implements LogService {

    @Autowired
    private LogMapper logMapper;


}