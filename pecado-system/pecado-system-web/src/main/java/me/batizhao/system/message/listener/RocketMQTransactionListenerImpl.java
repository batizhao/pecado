package me.batizhao.system.message.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.messaging.Message;

/**
 * @author batizhao
 * @date 2020/11/24
 */
@RocketMQTransactionListener
@Slf4j
public class RocketMQTransactionListenerImpl implements RocketMQLocalTransactionListener {

    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        String transId = (String) msg.getHeaders().get(RocketMQHeaders.TRANSACTION_ID);
        log.info("#### executeLocalTransaction is executed, msgTransactionId={}", transId);

        if (transId.equals("KEY_0")) {
            log.info("# COMMIT # 消息 {} commit! ###", msg);
            return RocketMQLocalTransactionState.COMMIT;
        }

        if (transId.equals("KEY_1")) {
            log.info("# ROLLBACK # 消息 {} rollback! ###", msg);
            return RocketMQLocalTransactionState.ROLLBACK;
        }

        log.info("# UNKNOW # 消息 {} unknown! ###", msg);
        return RocketMQLocalTransactionState.UNKNOWN;
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        String transId = (String) msg.getHeaders().get(RocketMQHeaders.TRANSACTION_ID);
        RocketMQLocalTransactionState retState = RocketMQLocalTransactionState.UNKNOWN;

        if (transId.equals("KEY_0")) {
            retState = RocketMQLocalTransactionState.COMMIT;
        }

        if (transId.equals("KEY_1")) {
            retState = RocketMQLocalTransactionState.ROLLBACK;
        }

        log.info("# 事务消息回查 # " +
                        " msg={}, TransactionState={}",
                msg, retState);
        return retState;
    }

}
