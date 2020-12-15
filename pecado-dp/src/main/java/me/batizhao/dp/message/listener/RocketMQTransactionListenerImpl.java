package me.batizhao.dp.message.listener;

import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.constant.SecurityConstants;
import me.batizhao.common.core.util.ResponseInfo;
import me.batizhao.common.core.util.ResultEnum;
import me.batizhao.system.api.dto.LogDTO;
import me.batizhao.system.api.feign.SystemLogFeignService;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;

import java.time.LocalDateTime;

/**
 * @author batizhao
 * @date 2020/11/24
 */
@RocketMQTransactionListener
@Slf4j
public class RocketMQTransactionListenerImpl implements RocketMQLocalTransactionListener {

    @Autowired
    private SystemLogFeignService systemLogFeignService;

    public static final LogDTO logDTO2 = new LogDTO().setDescription("handleSeata2").setSpend(20).setClassMethod("handleSeata2")
            .setClassName("me.batizhao.ims.web.CodeController").setClientId("client_app").setHttpRequestMethod("POST")
            .setIp("127.0.0.1").setCreatedTime(LocalDateTime.now()).setUrl("http://localhost:5000/role").setUsername("test");

    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        RocketMQLocalTransactionState retState;

        log.info("# COMMIT # 消息 {} commit! ###", msg);

        //这里用更复杂的远程调用做演示，实际本地事务的情况可能更简单一些
        ResponseInfo<Boolean> response = systemLogFeignService.saveLog(logDTO2, SecurityConstants.FROM_IN);

        log.info("# ResponseInfo: {} ###", response);

        //根据本地事务或者远程调用的结果，决定是提交还是删除消息。
        if (response.getCode().equals(ResultEnum.SUCCESS.getCode())) {
            retState = RocketMQLocalTransactionState.COMMIT;
        } else {
            retState = RocketMQLocalTransactionState.ROLLBACK;
        }

        return retState;
    }

    /**
     * 这里需要自己实现事务状态回查
     * @param msg
     * @return
     */
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        RocketMQLocalTransactionState retState = RocketMQLocalTransactionState.UNKNOWN;

        log.info("# 事务消息回查 # msg={}, TransactionState={}", msg, retState);
        return retState;
    }

}
