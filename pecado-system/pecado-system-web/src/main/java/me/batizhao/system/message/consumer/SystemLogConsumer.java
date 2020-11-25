package me.batizhao.system.message.consumer;

import me.batizhao.common.core.constant.MQConstants;
import me.batizhao.system.api.dto.LogDTO;
import me.batizhao.system.domain.Log;
import me.batizhao.system.service.LogService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author batizhao
 * @date 2020/11/9
 */
@Component
@RocketMQMessageListener(
        topic = MQConstants.TOPIC_SYSTEM_LOG,
        consumerGroup = "pecado-system-consumer-group",
        selectorExpression = "common"
)
public class SystemLogConsumer implements RocketMQListener<LogDTO> {

    @Autowired
    private LogService logService;

    @Override
    public void onMessage(LogDTO logDto) {
        Log log = new Log();
        BeanUtils.copyProperties(logDto, log);
        logService.save(log);
    }
}
