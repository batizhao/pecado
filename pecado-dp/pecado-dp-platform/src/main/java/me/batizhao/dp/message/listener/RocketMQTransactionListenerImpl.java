package me.batizhao.dp.message.listener;

/**
 * @author batizhao
 * @date 2020/11/24
 */
//@RocketMQTransactionListener
//@Slf4j
public class RocketMQTransactionListenerImpl {

//    @Autowired
//    private SystemLogFeignService systemLogFeignService;
//
//    public static final LogDTO logDTO2 = new LogDTO().setDescription("handleSeata2").setSpend(20).setClassMethod("handleSeata2")
//            .setClassName("me.batizhao.ims.web.CodeController").setClientId("client_app").setHttpRequestMethod("POST")
//            .setIp("127.0.0.1").setCreatedTime(LocalDateTime.now()).setUrl("http://localhost:5000/role").setUsername("test");
//
//    @Override
//    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
//        RocketMQLocalTransactionState retState;
//
//        log.info("# COMMIT # 消息 {} commit! ###", msg);
//
//        //这里用更复杂的远程调用做演示，实际本地事务的情况可能更简单一些
//        R<Boolean> response = systemLogFeignService.saveLog(logDTO2, SecurityConstants.FROM_IN);
//
//        log.info("# R: {} ###", response);
//
//        //根据本地事务或者远程调用的结果，决定是提交还是删除消息。
//        if (response.getCode().equals(ResultEnum.SUCCESS.getCode())) {
//            retState = RocketMQLocalTransactionState.COMMIT;
//        } else {
//            retState = RocketMQLocalTransactionState.ROLLBACK;
//        }
//
//        return retState;
//    }
//
//    /**
//     * 这里需要自己实现事务状态回查
//     * @param msg
//     * @return
//     */
//    @Override
//    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
//        RocketMQLocalTransactionState retState = RocketMQLocalTransactionState.UNKNOWN;
//
//        log.info("# 事务消息回查 # msg={}, TransactionState={}", msg, retState);
//        return retState;
//    }

}
