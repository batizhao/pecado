package me.batizhao.system.api.event;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.constant.SecurityConstants;
import me.batizhao.system.api.dto.LogDTO;
import me.batizhao.system.api.feign.SystemLogFeignService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author batizhao
 * @since 2020-04-01
 **/
@Slf4j
@AllArgsConstructor
@Component
public class SystemLogListener {

    private final SystemLogFeignService systemLogFeignService;

    @EventListener
    @Async
    public void saveLog(SystemLogEvent event) {
        LogDTO logDTO = (LogDTO) event.getSource();
        log.info("Feign async invoke start: {}", logDTO);
        systemLogFeignService.saveLog(logDTO, SecurityConstants.FROM_IN);
        log.info("Feign async invoke end.");
    }

}
