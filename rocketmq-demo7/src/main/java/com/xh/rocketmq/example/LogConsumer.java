package com.xh.rocketmq.example;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * topic: 主题
 * consumerGroup: 消费者组
 * selectorType: 过滤方式, TAG:标签过滤,仅支持标签, SQL92:SQL过滤,支持标签和属性
 * selectorExpression: 过滤表达式, 根据selectorType定, TAG时, 写标签如 "a || b", SQL92时, 写SQL表达式
 * consumeMode: CONCURRENTLY:并发消费, ORDERLY:顺序消费
 * messageModel: CLUSTERING:集群竞争消费, BROADCASTING:广播消费
 *
 * @author H.Yang
 * @date 2023/4/20
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = "broadcast-topic", consumerGroup = "broadcast-consumer-group", messageModel = MessageModel.BROADCASTING)
public class LogConsumer implements RocketMQListener<String> {
    @Override
    public void onMessage(String s) {
        log.info("LogConsumer onMessage: {}", s);
    }

}
