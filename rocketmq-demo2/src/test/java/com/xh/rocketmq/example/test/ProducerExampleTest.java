package com.xh.rocketmq.example.test;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
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
    private RocketMQTemplate rocketMQTemplate;


    /**
     * 同步消息
     */
    @Test
    @SneakyThrows
    public void syncMessages() {
        SendResult sendResult = rocketMQTemplate.syncSend("normal-topic", "发送一条同步消息到消息队列");
        log.info("RocketMQ 主题：{}，响应：{}", "normal-topic", sendResult);

        // 阻塞等待，保证消费
        new CountDownLatch(1).await();
    }

    /**
     * 异步消息
     */
    @Test
    @SneakyThrows
    public void asyncMessages() {
        rocketMQTemplate.asyncSend("normal-topic", "发送一条异步消息到消息队列", new SendCallback() {

            @Override
            public void onSuccess(SendResult sendResult) {
                // 处理消息发送成功的逻辑
                log.info("异步发送成功 {}", sendResult);
            }

            @Override
            public void onException(Throwable throwable) {
                // 处理消息发送失败的逻辑
                log.error("异步发送失败{}", throwable);
            }
        });

        // 阻塞等待，保证消费
        new CountDownLatch(1).await();
    }

    /**
     * 发送单向消息
     */
    @Test
    @SneakyThrows
    public void sendOneWayMessage() {
        rocketMQTemplate.sendOneWay("normal-topic", "发送一条发送单向消息到消息队列");

        // 阻塞等待，保证消费
        new CountDownLatch(1).await();
    }

}
