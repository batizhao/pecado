package me.batizhao.system.message.consumer;

import me.batizhao.common.core.constant.MQConstants;
import me.batizhao.system.api.dto.LogDTO;
import me.batizhao.system.domain.Log;
import me.batizhao.system.service.LogService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author batizhao
 * @date 2020/11/9
 */
//@Component
//@RocketMQMessageListener(
//        topic = MQConstants.TOPIC_SYSTEM_LOG,
//        consumerGroup = "pecado-system-reply-consumer-group",
//        selectorExpression = "reply"
//)
public class SystemLogReplyConsumer {

//    @Autowired
//    private LogService logService;
//
//    @Override
//    public String onMessage(LogDTO logDto) {
//        Log log = new Log();
//        BeanUtils.copyProperties(logDto, log);
//        logService.save(log);
//        return "hello";
//    }
}
