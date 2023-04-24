package com.xh.rocketmq.example;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * topic: 主题
 * consumerGroup: 消费者组
 * selectorType: 过滤方式, TAG:标签过滤,仅支持标签, SQL92:SQL过滤,支持标签和属性
 * selectorExpression: 过滤表达式, 根据selectorType定, TAG时, 写标签如 "a || b", SQL92时, 写SQL表达式；指明了只能接收消息属性（header）中a=1的消息， 默认值*，代表全部
 * consumeMode: CONCURRENTLY:并发消费, ORDERLY:顺序消费
 * messageModel: CLUSTERING:集群竞争消费, BROADCASTING:广播消费
 * consumeThreadMax: 指定消费者线程数，默认64，生产中请注意配置，避免过大或者过小
 *
 * @author H.Yang
 * @date 2023/4/20
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = "retry-topic", consumerGroup = "retry-consumer-group")
public class RetryConsumer implements RocketMQListener<MessageExt> {

    private int maxRetryTimes = 3;  // 最多重试次数

    @Override
    public void onMessage(MessageExt ext) {
        String s = new String(ext.getBody());
        log.info("RetryConsumer 消费当前次数：{}，消费内容：{}", ext.getReconsumeTimes(), s);
        // 消费发生异常后会重复消费同一数据，默认会按第3级及之后的延时时间间隔重复消费（即第一次重复消费在首次消费10s后，第二次重复消费在第一次重复消费30s后，以此类推），可以根据消费次数终止重复消费
        if (ext.getReconsumeTimes() > maxRetryTimes) {
            log.info("停止重试，写入数据库...");
            return;
        }

        throw new RuntimeException(s + " 是故意抛出一个异常");
    }

}
