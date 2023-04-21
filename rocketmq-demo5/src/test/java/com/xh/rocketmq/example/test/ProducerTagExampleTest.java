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
public class ProducerTagExampleTest {

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @SneakyThrows
    @Test
    public void producer() {
        rocketMQTemplate.convertAndSend("logistics-topic:logistics-tag", "Hello Logistics message 1");
        rocketMQTemplate.convertAndSend("logistics-topic:logistics-tag", "Hello Logistics message 2");
        rocketMQTemplate.convertAndSend("logistics-topic:logistics-tag", "Hello Logistics message 3");
        rocketMQTemplate.convertAndSend("warehouse-topic:warehouse-tag", "Hello Warehouse message 1");
        rocketMQTemplate.convertAndSend("warehouse-topic:warehouse-tag", "Hello Warehouse message 2");

        Thread.sleep(5 * 1000);
    }
}
