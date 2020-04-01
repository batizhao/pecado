package me.batizhao.system.api.event;

import me.batizhao.system.api.dto.LogDTO;
import org.springframework.context.ApplicationEvent;

/**
 * @author batizhao
 * @since 2020-04-01
 **/
public class SystemLogEvent extends ApplicationEvent {

    public SystemLogEvent(LogDTO source) {
        super(source);
    }
}
