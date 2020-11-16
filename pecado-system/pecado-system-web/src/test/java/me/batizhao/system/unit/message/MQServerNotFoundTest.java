package me.batizhao.system.unit.message;

import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.constant.MQConstants;
import me.batizhao.common.core.exception.WebExceptionHandler;
import me.batizhao.system.api.dto.LogDTO;
import org.apache.rocketmq.client.AccessChannel;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.autoconfigure.RocketMQAutoConfiguration;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.MessagingException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * 测试 RocketMQ 不存在的情况
 *
 * @author batizhao
 * @date 2020/11/12
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = RocketMQAutoConfiguration.class)
@ActiveProfiles("test")
@Tag("unit")
@Slf4j
public class MQServerNotFoundTest {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    private LogDTO logDTO;

    @BeforeEach
    public void setUp() {
        logDTO = new LogDTO().setDescription("MQ message test case2").setSpend(20).setClassMethod("setUp")
                .setClassName("me.batizhao.system.unit.message.MessageNotRunConsumerTest").setClientId("client_app").setHttpRequestMethod("POST")
                .setIp("127.0.0.1").setCreatedTime(LocalDateTime.now()).setUrl("http://localhost:5000/role").setUsername("test");
    }

    @Test
    public void testProperties() {
        assertThat(rocketMQTemplate.getProducer().getNamesrvAddr(), equalTo("127.0.0.1:9877"));
        assertThat(rocketMQTemplate.getProducer().getProducerGroup(), equalTo("pecado-test-producer-group"));
        assertThat(rocketMQTemplate.getProducer().getAccessChannel(), equalTo(AccessChannel.LOCAL));
        assertThat(rocketMQTemplate.getProducer().getSendMsgTimeout(), equalTo(3000));
        assertThat(rocketMQTemplate.getProducer().getMaxMessageSize(), equalTo(4 * 1024 * 1024));
        assertThat(rocketMQTemplate.getProducer().getRetryTimesWhenSendAsyncFailed(), equalTo(2));
        assertThat(rocketMQTemplate.getProducer().getRetryTimesWhenSendFailed(), equalTo(2));
        assertThat(rocketMQTemplate.getProducer().getCompressMsgBodyOverHowmuch(), equalTo(1024 * 4));
    }

    @Test
    public void testSendMessage() {
        Exception exception = assertThrows(MessagingException.class, () -> rocketMQTemplate.syncSend(MQConstants.TOPIC_SYSTEM_LOG, logDTO));
        assertThat(exception.getMessage(), containsString("RemotingConnectException"));
    }
}
