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
public class ProducerDLQExampleTest {

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @Test
    @SneakyThrows
    public void orderMessage() {
        for (int i = 0; i < 5; i++) {
            rocketMQTemplate.syncSend("order-topic", "重试消息" + i);

            Thread.sleep(2000);
        }

        // 阻塞等待，保证消费
        new CountDownLatch(1).await();
    }

}
