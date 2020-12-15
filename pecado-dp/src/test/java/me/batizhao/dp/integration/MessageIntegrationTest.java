package me.batizhao.dp.integration;

import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.constant.MQConstants;
import me.batizhao.dp.PecadoDevPlatformApplication;
import me.batizhao.system.api.dto.LogDTO;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.spring.autoconfigure.RocketMQAutoConfiguration;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author batizhao
 * @date 2020/11/12
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = PecadoDevPlatformApplication.class)
@ImportAutoConfiguration(RocketMQAutoConfiguration.class)
@ActiveProfiles("integration")
@Tag("integration")
@Slf4j
@DirtiesContext
public class MessageIntegrationTest {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Test
    public void testSyncSendMessage() {
        LogDTO logDTO = new LogDTO().setDescription("testSyncSendMessage").setSpend(20).setClassMethod("testSyncSendMessage")
                .setClassName("me.batizhao.dp.integration.MessageConsumerTest").setClientId("client_app").setHttpRequestMethod("POST")
                .setIp("127.0.0.1").setCreatedTime(LocalDateTime.now()).setUrl("http://localhost:5000/role").setUsername("test");

        SendResult result = rocketMQTemplate.syncSend(MQConstants.TOPIC_SYSTEM_LOG_TAG_COMMON, logDTO);

        log.info("result: {}", result);

        assertThat(result.getSendStatus(), equalTo(SendStatus.SEND_OK));
    }

    @Test
    public void testSyncSendMessageTimeout() {
        LogDTO logDTO = new LogDTO().setDescription("testSyncSendMessageTimeout").setSpend(20).setClassMethod("testSyncSendMessageTimeout")
                .setClassName("me.batizhao.dp.integration.MessageConsumerTest").setClientId("client_app").setHttpRequestMethod("POST")
                .setIp("127.0.0.1").setCreatedTime(LocalDateTime.now()).setUrl("http://localhost:5000/role").setUsername("test");

        Exception exception = assertThrows(MessagingException.class, () -> rocketMQTemplate.syncSend(MQConstants.TOPIC_SYSTEM_LOG_TAG_COMMON, logDTO, 0));
        assertThat(exception.getMessage(), containsString("call timeout"));
    }

    @Test
    public void testAsyncSendMessage() {
        LogDTO logDTO = new LogDTO().setDescription("testAsyncSendMessage").setSpend(20).setClassMethod("testAsyncSendMessage")
                .setClassName("me.batizhao.dp.integration.MessageConsumerTest").setClientId("client_app").setHttpRequestMethod("POST")
                .setIp("127.0.0.1").setCreatedTime(LocalDateTime.now()).setUrl("http://localhost:5000/role").setUsername("test");

        rocketMQTemplate.asyncSend(MQConstants.TOPIC_SYSTEM_LOG_TAG_COMMON, logDTO, new SendCallback() {

            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("result: {}", sendResult);
                assertThat(sendResult.getSendStatus(), equalTo(SendStatus.SEND_OK));
            }

            @Override
            public void onException(Throwable throwable) {
                log.error("result: {}", throwable.getMessage());
                assertThat(throwable.getMessage(), containsString("RemotingConnectException"));
            }
        });
    }

    @Test
    public void testSendAndReceiveMessage() {
        LogDTO logDTO = new LogDTO().setDescription("testSendAndReceiveMessage").setSpend(20).setClassMethod("testSendAndReceiveMessage")
                .setClassName("me.batizhao.dp.integration.MessageConsumerTest").setClientId("client_app").setHttpRequestMethod("POST")
                .setIp("127.0.0.1").setCreatedTime(LocalDateTime.now()).setUrl("http://localhost:5000/role").setUsername("test");

        String response = rocketMQTemplate.sendAndReceive(MQConstants.TOPIC_SYSTEM_LOG + ":reply", logDTO, String.class, 10000L);

        log.info("response: {}", response);

        assertThat(response, equalTo("hello"));
    }

    /**
     * 测试事务消息
     * 一条通过 MQ 发送消息
     * 一条通过 Feign 远程调用
     *
     * 这里可以分别启动、或者停止 System 进行测试
     * 当启动时，异步消息和远程调用同时成功；
     * 当停止时，同时失败。
     */
    @Test
    public void givenLog_whenSendTransactionMessage_thenSeeResult() {
        LogDTO logDTO = new LogDTO().setDescription("givenLog_whenSendTransactionMessage_thenCommit").setSpend(20).setClassMethod("givenLog_whenSendTransactionMessage_thenCommit")
                .setClassName("me.batizhao.dp.integration.MessageConsumerTest").setClientId("client_app").setHttpRequestMethod("POST")
                .setIp("127.0.0.1").setCreatedTime(LocalDateTime.now()).setUrl("http://localhost:5000/role").setUsername("test");

        Message<LogDTO> msg = MessageBuilder.withPayload(logDTO).build();

        SendResult sendResult = rocketMQTemplate.sendMessageInTransaction(MQConstants.TOPIC_SYSTEM_LOG_TAG_COMMON, msg, null);

        log.info("sendResult = {}", sendResult);

        assertThat(sendResult.getSendStatus(), equalTo(SendStatus.SEND_OK));

        //TODO: 完成后续数据校验
    }

    @Test
    public void testTransactionListener() {
        assertThat(((TransactionMQProducer) rocketMQTemplate.getProducer()).getTransactionListener(), notNullValue());
    }

}
