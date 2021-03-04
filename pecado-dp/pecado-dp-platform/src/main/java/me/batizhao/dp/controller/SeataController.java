package me.batizhao.dp.controller;

import io.swagger.annotations.Api;
import me.batizhao.common.core.constant.MQConstants;
import me.batizhao.common.core.constant.SecurityConstants;
import me.batizhao.common.core.util.ResponseInfo;
import me.batizhao.dp.service.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * 分布式事务测试
 *
 * @author batizhao
 * @date 2020/10/26
 */
//@Api(tags = "分布式事务测试")
//@RestController
public class SeataController {

//    @Autowired
//    private SystemLogFeignService systemLogFeignService;
//
//    @Autowired
//    private RocketMQTemplate rocketMQTemplate;
//
//    public static final LogDTO logDTO = new LogDTO().setDescription("handleSeata").setSpend(20).setClassMethod("handleSeata")
//            .setClassName("me.batizhao.ims.web.CodeController").setClientId("client_app").setHttpRequestMethod("POST")
//            .setIp("127.0.0.1").setCreatedTime(LocalDateTime.now()).setUrl("http://localhost:5000/role").setUsername("test");
//
//    public static final LogDTO logDTO2 = new LogDTO().setDescription("handleSeata2").setSpend(20).setClassMethod("handleSeata2")
//                .setClassName("me.batizhao.ims.web.CodeController").setClientId("client_app").setHttpRequestMethod("POST")
//                .setIp("127.0.0.1").setCreatedTime(LocalDateTime.now()).setUrl("http://localhost:5000/role").setUsername("test");
//
//    public static final LogDTO logDTO3 = new LogDTO().setDescription("handleSeataMQ").setSpend(20).setClassMethod("handleSeataMQ")
//            .setClassName("me.batizhao.ims.web.CodeController").setClientId("client_app").setHttpRequestMethod("POST")
//            .setIp("127.0.0.1").setCreatedTime(LocalDateTime.now()).setUrl("http://localhost:5000/role").setUsername("test");
//
//
//    /**
//     * 没有分布式事务框架支持，提交成功
//     * @return ResponseInfo
//     */
//    @GetMapping("/no/success")
//    public ResponseInfo<Boolean> handleNoTransactionThenSuccess() {
//        systemLogFeignService.saveLog(logDTO, SecurityConstants.FROM_IN);
//        systemLogFeignService.saveLog(logDTO2, SecurityConstants.FROM_IN);
//
//        return ResponseInfo.ok(true);
//    }
//
//    /**
//     * 没有分布式事务框架支持，提交部分成功
//     * @return ResponseInfo
//     */
//    @GetMapping("/no/fail")
//    public ResponseInfo<Boolean> handleNoTransactionThenFail() {
//        ResponseInfo<Boolean> b = systemLogFeignService.saveLog(logDTO, SecurityConstants.FROM_IN);
//
//        //故意抛出异常
//        if(b.getData())
//            throw new RuntimeException("handleNoTransactionThenFail");
//
//        systemLogFeignService.saveLog(logDTO2, SecurityConstants.FROM_IN);
//
//        return ResponseInfo.ok(true);
//    }

    /**
     * 有分布式事务框架支持，提交全部成功
     * @return ResponseInfo
     */
//    @GetMapping("/seata/commit")
//    @GlobalTransactional
//    public ResponseInfo<Boolean> handleTransactionThenSuccess() {
//        systemLogFeignService.saveLog(logDTO, SecurityConstants.FROM_IN);
//        systemLogFeignService.saveLog(logDTO2, SecurityConstants.FROM_IN);
//
//        rocketMQTemplate.syncSend(MQConstants.TOPIC_SYSTEM_LOG_TAG_COMMON, logDTO3);
//
//        return ResponseInfo.ok(true);
//    }

    /**
     * 有分布式事务框架支持，提交全部回滚
     * 这里如果要保证强一致性，需要使用事务消息实现
     * @return ResponseInfo
     * @link me.batizhao.dp.integration.MessageIntegrationTest#givenLog_whenSendTransactionMessage_thenSeeResult
     */
//    @GetMapping("/seata/rollback")
//    @GlobalTransactional
//    public ResponseInfo<Boolean> handleTransactionThenFail() {
//        systemLogFeignService.saveLog(logDTO, SecurityConstants.FROM_IN);
//        ResponseInfo<Boolean> b = systemLogFeignService.saveLog(logDTO2, SecurityConstants.FROM_IN);
//
//        if(b.getData())
//            throw new RuntimeException("handleSeataTransactionThenFail");
//
//        rocketMQTemplate.syncSend(MQConstants.TOPIC_SYSTEM_LOG_TAG_COMMON, logDTO3);
//        return ResponseInfo.ok(false);
//    }

}
