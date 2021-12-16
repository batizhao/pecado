package me.batizhao.dp.controller;

/**
 * 分布式事务测试
 *
 * @author batizhao
 * @date 2020/10/26
 */
//@Tag(name = "分布式事务测试")
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
//     * @return R
//     */
//    @GetMapping("/no/success")
//    public R<Boolean> handleNoTransactionThenSuccess() {
//        systemLogFeignService.saveLog(logDTO, SecurityConstants.FROM_IN);
//        systemLogFeignService.saveLog(logDTO2, SecurityConstants.FROM_IN);
//
//        return R.ok(true);
//    }
//
//    /**
//     * 没有分布式事务框架支持，提交部分成功
//     * @return R
//     */
//    @GetMapping("/no/fail")
//    public R<Boolean> handleNoTransactionThenFail() {
//        R<Boolean> b = systemLogFeignService.saveLog(logDTO, SecurityConstants.FROM_IN);
//
//        //故意抛出异常
//        if(b.getData())
//            throw new RuntimeException("handleNoTransactionThenFail");
//
//        systemLogFeignService.saveLog(logDTO2, SecurityConstants.FROM_IN);
//
//        return R.ok(true);
//    }

    /**
     * 有分布式事务框架支持，提交全部成功
     * @return R
     */
//    @GetMapping("/seata/commit")
//    @GlobalTransactional
//    public R<Boolean> handleTransactionThenSuccess() {
//        systemLogFeignService.saveLog(logDTO, SecurityConstants.FROM_IN);
//        systemLogFeignService.saveLog(logDTO2, SecurityConstants.FROM_IN);
//
//        rocketMQTemplate.syncSend(MQConstants.TOPIC_SYSTEM_LOG_TAG_COMMON, logDTO3);
//
//        return R.ok(true);
//    }

    /**
     * 有分布式事务框架支持，提交全部回滚
     * 这里如果要保证强一致性，需要使用事务消息实现
     * @return R
     * @link me.batizhao.dp.integration.MessageIntegrationTest#givenLog_whenSendTransactionMessage_thenSeeResult
     */
//    @GetMapping("/seata/rollback")
//    @GlobalTransactional
//    public R<Boolean> handleTransactionThenFail() {
//        systemLogFeignService.saveLog(logDTO, SecurityConstants.FROM_IN);
//        R<Boolean> b = systemLogFeignService.saveLog(logDTO2, SecurityConstants.FROM_IN);
//
//        if(b.getData())
//            throw new RuntimeException("handleSeataTransactionThenFail");
//
//        rocketMQTemplate.syncSend(MQConstants.TOPIC_SYSTEM_LOG_TAG_COMMON, logDTO3);
//        return R.ok(false);
//    }

}
