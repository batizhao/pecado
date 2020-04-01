package me.batizhao.system.api.event;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.constant.SecurityConstants;
import me.batizhao.system.api.dto.LogDTO;
import me.batizhao.system.api.feign.SystemLogFeignService;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

/**
 * @author batizhao
 * @since 2020-04-01
 **/
@Slf4j
@AllArgsConstructor
public class SystemLogListener {

    private final SystemLogFeignService systemLogFeignService;

    @Async
    @Order
    @EventListener(SystemLogEvent.class)
    public void saveSysLog(SystemLogEvent event) {
        LogDTO log = (LogDTO) event.getSource();
        systemLogFeignService.saveLog(log, SecurityConstants.FROM_IN);
    }
}
