package com.xh.rocketmq.example;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * topic: 主题
 * consumerGroup: 消费者组
 *
 * @author H.Yang
 * @date 2023/4/20
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = "normal-topic", consumerGroup = "normal-group")
public class ConfirmNormalConsumer implements MessageListenerOrderly {

    @Override
    public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
        // 处理消息的逻辑，例如保存到数据库或调用其他服务
        System.out.println("Received message: " + msgs);

        // 处理消息逻辑
        for (MessageExt msg : msgs) {
            try {
                // 处理消息的逻辑
                System.out.println("Received message: " + new String(msg.getBody(), StandardCharsets.UTF_8));

                // 手动ACK确认消费
                // 设置autoCommit为false，表示手动控制ACK
                context.setAutoCommit(false);
            } catch (Exception e) {
                e.printStackTrace();
                // 处理消息失败，返回ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT
                // 表示暂时挂起当前队列，稍后重试
                return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
            }
        }

        // 处理消息成功，返回ConsumeOrderlyStatus.SUCCESS
        return ConsumeOrderlyStatus.SUCCESS;
    }
}
