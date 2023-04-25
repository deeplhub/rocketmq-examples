package com.xh.rocketmq.example.test;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;

/**
 * @author H.Yang
 * @date 2023/4/20
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProducerExampleTest {

    @Resource
    private RocketMQTemplate rocketmqTemplate;

    /**
     * 通过实体类发送消息
     */
    @Test
    @SneakyThrows
    public void orderMessage() {
        TransactionSendResult result = rocketmqTemplate.sendMessageInTransaction("order_topic", MessageBuilder.withPayload("发送一条订单消息到消息队列").build(), 11);
        log.info("消息发送结果：{}", result);

        // 阻塞等待，保证消费
        new CountDownLatch(1).await();
    }

}
