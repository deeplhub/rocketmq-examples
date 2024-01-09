package com.xh.rocketmq.example;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author H.Yang
 * @since 2024/1/9
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = "order_topic", consumerGroup = "order_product_consumer_group")
public class ProductConsumer implements RocketMQListener<MessageExt> {

    @Override
    public void onMessage(MessageExt messageExt) {
        log.info("产品服务 - 消费者收到消息[{}]", JSONUtil.toJsonStr(messageExt));
    }
}
