package com.xh.rocketmq.example.test;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
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
public class ProducerBroadcastExampleTest {

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @SneakyThrows
    @Test
    public void broadcastMessage() {
        rocketMQTemplate.syncSend("broadcast-topic", "广播消息");

        Thread.sleep(5 * 1000);
    }
}
