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


    @Test
    public void syncMessages() {
        // 同步消息
        SendResult sendResult = rocketMQTemplate.syncSend("normal-topic", "Hello, World!");
        log.info("RocketMQ 主题：{}，响应：{}", "normal-topic", sendResult);

        System.out.println();
    }

    @Test
    @SneakyThrows
    public void asyncMessages() {
        // 异步消息
        rocketMQTemplate.asyncSend("normal-topic", "Hello, World!", new SendCallback() {

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

        Thread.sleep(5 * 1000);
    }
}
