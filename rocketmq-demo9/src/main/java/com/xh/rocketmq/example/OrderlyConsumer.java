package com.xh.rocketmq.example;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author H.Yang
 * @date 2023/4/20
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = "orderly-topic", consumerGroup = "orderly-group", consumeMode = ConsumeMode.ORDERLY)
public class OrderlyConsumer implements RocketMQListener<String> {
    @Override
    public void onMessage(String s) {
        log.info("OrderlyConsumer onMessage: {}", s);
    }

}
