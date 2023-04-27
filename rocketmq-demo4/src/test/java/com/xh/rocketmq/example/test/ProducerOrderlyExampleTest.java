package com.xh.rocketmq.example.test;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
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
public class ProducerOrderlyExampleTest {

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @SneakyThrows
    @Test
    public void producer() {
        rocketMQTemplate.syncSendOrderly("orderly-topic", "Hello, Orderly message1", "111111");
        rocketMQTemplate.syncSendOrderly("orderly-topic", "Hello, Orderly message2", "111111");
        rocketMQTemplate.syncSendOrderly("orderly-topic", "Hello, Orderly message3", "222222");
        rocketMQTemplate.syncSendOrderly("orderly-topic", "Hello, Orderly message4", "222222");
        rocketMQTemplate.syncSendOrderly("orderly-topic", "Hello, Orderly message5", "333333");


        // 阻塞等待，保证消费
        new CountDownLatch(1).await();
    }
}
