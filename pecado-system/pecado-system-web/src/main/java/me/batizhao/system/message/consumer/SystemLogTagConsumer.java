package me.batizhao.system.message.consumer;

import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.constant.MQConstants;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author batizhao
 * @date 2020/11/9
 */
@Component
@RocketMQMessageListener(
        topic = MQConstants.TOPIC_SYSTEM_LOG,
        consumerGroup = "pecado-system-tags-consumer-group",
        selectorExpression = "tagsA"
)
@Slf4j
public class SystemLogTagConsumer implements RocketMQListener<String> {

    @Override
    public void onMessage(String msg) {
        log.info("SystemLogTagConsumer message: {}", msg);
    }
}
