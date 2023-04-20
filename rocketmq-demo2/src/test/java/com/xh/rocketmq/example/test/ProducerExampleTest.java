package com.xh.rocketmq.example.test;

import lombok.extern.slf4j.Slf4j;
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
    public void producer() {
        SendResult sendResult = rocketMQTemplate.syncSend("normal-topic", "Hello, World!");
        log.info("RocketMQ 主题：{}，响应：{}", "normal-topic", sendResult);

        System.out.println();
    }
}
