package com.xh.rocketmq.example;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author H.Yang
 * @date 2023/4/20
 */
@Slf4j
@Component
@RocketMQMessageListener(
        consumerGroup = "order-group",  //消费者组
        topic = "order-topic", //topic
        maxReconsumeTimes = 2 //最大消息重试次数
)
public class OrderConsumer implements RocketMQListener<MessageExt> {

    @Override
    public void onMessage(MessageExt ext) {
        String s = new String(ext.getBody());
        log.info("OrderConsumer 消费当前次数：{}，消费内容：{}", ext.getReconsumeTimes(), s);

        throw new RuntimeException(s + " 是故意抛出一个异常");
    }

}
