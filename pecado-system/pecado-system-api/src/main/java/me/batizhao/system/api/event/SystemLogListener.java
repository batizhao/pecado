package me.batizhao.system.api.event;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.constant.SecurityConstants;
import me.batizhao.system.api.dto.LogDTO;
import me.batizhao.system.api.feign.SystemLogFeignService;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author batizhao
 * @since 2020-04-01
 **/
@Slf4j
@AllArgsConstructor
@Component
public class SystemLogListener implements ApplicationListener<SystemLogEvent> {

    private final SystemLogFeignService systemLogFeignService;

    @Override
    @Async
    public void onApplicationEvent(SystemLogEvent systemLogEvent) {
        LogDTO log = (LogDTO) systemLogEvent.getSource();
        systemLogFeignService.saveLog(log, SecurityConstants.FROM_IN);
    }

}
