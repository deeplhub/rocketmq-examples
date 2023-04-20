package com.xh.rocketmq.example;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author H.Yang
 * @date 2023/4/20
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = "delay-topic", consumerGroup = "delay-group")
public class DelayConsumer implements RocketMQListener<String> {
    @Override
    public void onMessage(String s) {
        log.info("DelayConsumer onMessage: {}", s);
    }

}
