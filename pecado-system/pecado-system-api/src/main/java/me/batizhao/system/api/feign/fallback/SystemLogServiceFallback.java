package me.batizhao.system.api.feign.fallback;

import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.util.ResponseInfo;
import me.batizhao.system.api.dto.LogDTO;
import me.batizhao.system.api.feign.SystemLogFeignService;
import org.springframework.stereotype.Component;

/**
 * @author batizhao
 * @since 2020-04-01
 **/
@Slf4j
@Component
public class SystemLogServiceFallback implements SystemLogFeignService {

    @Override
    public ResponseInfo<Boolean> saveLog(LogDTO logDTO, String from) {
        log.error("feign 写日志失败: {}", logDTO);
        return ResponseInfo.failed(false);
    }
}
