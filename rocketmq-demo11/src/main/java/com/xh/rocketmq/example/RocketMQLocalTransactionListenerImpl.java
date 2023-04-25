package com.xh.rocketmq.example;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * 本地事务监听
 * <p>
 * 返回事务状态，COMMIT：提交  ROLLBACK：回滚  UNKNOW：未知
 * <p>
 * 返回COMMIT状态的消息会立即被消费者消费到。
 * 返回ROLLBACK状态的消息会被丢弃。
 * 返回UNKNOWN状态的消息会由Broker过一段时间再来回查事务的状态。
 *
 * @author H.Yang
 * @date 2023/4/25
 */
@Slf4j
@Component
@RocketMQTransactionListener
public class RocketMQLocalTransactionListenerImpl implements RocketMQLocalTransactionListener {

    /**
     * 发送prepare消息成功此方法被回调，该方法用于执行本地事务
     * <p>
     * 消息发送成功回调此方法，此方法执行本地事务
     *
     * @param message 回传的消息，利用transactionId即可获取到该消息的唯一Id
     * @param arg     调用send方法时传递的参数，当send时候若有额外的参数可以传递到send方法中，这里能获取到
     * @return 返回事务状态，COMMIT：提交  ROLLBACK：回滚  UNKNOW：未知
     */
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object arg) {
        log.info("执行本地事务，message:{}，arg:{}", message, arg);
        return RocketMQLocalTransactionState.UNKNOWN;
    }

    /**
     * 本地事务的检查，检查本地事务是否成功
     * <p>
     * 此方法检查事务执行状态
     *
     * @param message 通过获取transactionId来判断这条消息的本地事务执行状态
     * @return 返回事务状态，COMMIT：提交  ROLLBACK：回滚  UNKNOW：未知
     */
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        log.info("检查本地事务:{}", message);
        return RocketMQLocalTransactionState.COMMIT;
    }
}
