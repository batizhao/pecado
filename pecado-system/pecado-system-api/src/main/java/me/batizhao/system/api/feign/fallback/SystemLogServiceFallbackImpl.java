package me.batizhao.system.api.feign.fallback;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.util.ResponseInfo;
import me.batizhao.system.api.domain.Log;
import me.batizhao.system.api.feign.SystemLogFeignService;
import org.springframework.stereotype.Component;

/**
 * @author batizhao
 * @since 2020-04-01
 **/
@Slf4j
@Component
public class SystemLogServiceFallbackImpl implements SystemLogFeignService {

    @Setter
    private Throwable throwable;

    @Override
    public ResponseInfo<Boolean> saveLog(Log logDTO, String from) {
        log.error("feign 写日志失败: {}", logDTO, throwable);
        return ResponseInfo.failed(false);
    }
}
