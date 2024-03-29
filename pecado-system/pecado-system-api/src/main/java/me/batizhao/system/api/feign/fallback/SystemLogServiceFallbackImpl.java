package me.batizhao.system.api.feign.fallback;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.util.R;
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
    public R<Boolean> saveLog(Log logDTO, String from) {
        log.error("feign 写日志失败: {}", logDTO, throwable);
        return R.failed(false);
    }
}
