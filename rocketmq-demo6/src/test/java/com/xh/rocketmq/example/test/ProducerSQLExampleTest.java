package com.xh.rocketmq.example.test;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author H.Yang
 * @date 2023/4/20
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProducerSQLExampleTest {

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @SneakyThrows
    @Test
    public void producer() {
        String[] citys = {"shanghai", "beijing", "guangzhou", "shenzhen", "zhengzhou"};
        for (String city : citys) {
            rocketMQTemplate.syncSend("city-topic", MessageBuilder
                    .withPayload("Hello " + city + " message")
                    .setHeader("city", city)
                    .build()
            );
        }

        for (int i = 0; i < 5; i++) {
            rocketMQTemplate.syncSend("number-topic", MessageBuilder
                    .withPayload("Hello Number message " + i)
                    .setHeader("number", i)
                    .build()
            );
        }


        Thread.sleep(5 * 1000);
    }
}
