package com.xh.rocketmq.example;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author H.Yang
 * @since 2024/1/11
 */
@Slf4j
@Component
@RocketMQMessageListener(
        consumerGroup = "dlq-group",
        topic = "%DLQ%order-group"
)
public class DLQConsumer implements RocketMQListener<MessageExt> {

    @Override
    public void onMessage(MessageExt ext) {
        log.info("消费了死信队列中的消息:{}", ext);
    }
}
