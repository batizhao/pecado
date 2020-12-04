package me.batizhao.system.integration;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.constant.MQConstants;
import me.batizhao.system.PecadoSystemApplication;
import me.batizhao.system.api.dto.LogDTO;
import me.batizhao.system.domain.Log;
import me.batizhao.system.service.LogService;
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
@SpringBootTest(classes = PecadoSystemApplication.class)
@ImportAutoConfiguration(RocketMQAutoConfiguration.class)
@ActiveProfiles("integration")
@Tag("integration")
@Slf4j
@DirtiesContext
public class MessageConsumerTest {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Autowired
    private LogService logService;

    @Test
    public void testSyncSendMessage() {
        LogDTO logDTO = new LogDTO().setDescription("testSyncSendMessage").setSpend(20).setClassMethod("testSyncSendMessage")
                .setClassName("me.batizhao.system.integration.MessageConsumerTest").setClientId("client_app").setHttpRequestMethod("POST")
                .setIp("127.0.0.1").setCreatedTime(LocalDateTime.now()).setUrl("http://localhost:5000/role").setUsername("test");

        SendResult result = rocketMQTemplate.syncSend(MQConstants.TOPIC_SYSTEM_LOG_TAG_COMMON, logDTO);

        log.info("result: {}", result);

        assertThat(result.getSendStatus(), equalTo(SendStatus.SEND_OK));
    }

    @Test
    public void testSyncSendMessageTimeout() {
        LogDTO logDTO = new LogDTO().setDescription("testSyncSendMessageTimeout").setSpend(20).setClassMethod("testSyncSendMessageTimeout")
                .setClassName("me.batizhao.system.integration.MessageConsumerTest").setClientId("client_app").setHttpRequestMethod("POST")
                .setIp("127.0.0.1").setCreatedTime(LocalDateTime.now()).setUrl("http://localhost:5000/role").setUsername("test");

        Exception exception = assertThrows(MessagingException.class, () -> rocketMQTemplate.syncSend(MQConstants.TOPIC_SYSTEM_LOG_TAG_COMMON, logDTO, 0));
        assertThat(exception.getMessage(), containsString("call timeout"));

        Log log1 = logService.getOne(Wrappers.<Log>lambdaQuery().eq(Log::getClassMethod, "testSyncSendMessageTimeout"));
        assertThat(log1, nullValue());
    }

    @Test
    public void testAsyncSendMessage() {
        LogDTO logDTO = new LogDTO().setDescription("testAsyncSendMessage").setSpend(20).setClassMethod("testAsyncSendMessage")
                .setClassName("me.batizhao.system.integration.MessageConsumerTest").setClientId("client_app").setHttpRequestMethod("POST")
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
                .setClassName("me.batizhao.system.integration.MessageConsumerTest").setClientId("client_app").setHttpRequestMethod("POST")
                .setIp("127.0.0.1").setCreatedTime(LocalDateTime.now()).setUrl("http://localhost:5000/role").setUsername("test");

        String response = rocketMQTemplate.sendAndReceive(MQConstants.TOPIC_SYSTEM_LOG + ":reply", logDTO, String.class, 10000L);

        log.info("response: {}", response);

        assertThat(response, equalTo("hello"));
    }

    @Test
    public void givenString_whenSendTransactionMessage_thenCommit() {
        Message<String> msg = MessageBuilder.withPayload("这是一个 KEY_0 事务消息").
                setHeader(RocketMQHeaders.TRANSACTION_ID, "KEY_0").build();

        SendResult sendResult = rocketMQTemplate.sendMessageInTransaction(
                MQConstants.TOPIC_SYSTEM_LOG + ":tagsA", msg, null);

        log.info("sendResult = {}", sendResult);

        assertThat(sendResult.getSendStatus(), equalTo(SendStatus.SEND_OK));

        //TODO: 完成后续数据校验
    }

    @Test
    public void givenString_whenSendTransactionMessage_thenRollback() {
        Message<String> msg = MessageBuilder.withPayload("这是一个 KEY_1 事务消息").
                setHeader(RocketMQHeaders.TRANSACTION_ID, "KEY_1").build();

        SendResult sendResult = rocketMQTemplate.sendMessageInTransaction(
                MQConstants.TOPIC_SYSTEM_LOG + ":tagsB", msg, null);

        log.info("sendResult = {}", sendResult);

        assertThat(sendResult.getSendStatus(), equalTo(SendStatus.SEND_OK));

        //TODO: 完成后续数据校验
    }

    @Test
    public void givenString_whenSendTransactionMessage_thenUnknown() throws InterruptedException {
        Message<String> msg = MessageBuilder.withPayload("这是一个 KEY_2 事务消息").
                setHeader(RocketMQHeaders.TRANSACTION_ID, "KEY_2").build();

        SendResult sendResult = rocketMQTemplate.sendMessageInTransaction(
                MQConstants.TOPIC_SYSTEM_LOG + ":tagsC", msg, null);

        log.info("sendResult = {}", sendResult);

        assertThat(sendResult.getSendStatus(), equalTo(SendStatus.SEND_OK));

        //等 60 秒消息回查
        Thread.sleep(70000L);

        //TODO: 完成后续数据校验
    }

    @Test
    public void testTransactionListener() {
        assertThat(((TransactionMQProducer) rocketMQTemplate.getProducer()).getTransactionListener(), notNullValue());
    }

}
