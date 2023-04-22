package com.xh.rocketmq.example.test;

import com.xh.rocketmq.code.BaseMessageModel;
import com.xh.rocketmq.code.ExtRocketMQTemplate;
import com.xh.rocketmq.code.StorageRocketMQTemplate;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.time.LocalDateTime;
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
    private ExtRocketMQTemplate rocketmqTemplate;
    @Resource
    private StorageRocketMQTemplate storageRocketMQTemplate;

    /**
     * 通过实体类发送消息
     */
    @Test
    @SneakyThrows
    public void orderMessage() {
        this.sendOrderMessage();

        // 阻塞等待，保证消费
        new CountDownLatch(1).await();
    }

    /**
     * 注入对应业务的模板类
     */
    @Test
    @SneakyThrows
    public void storageMessage() {
        storageRocketMQTemplate.sendStorageMessage();

        // 阻塞等待，保证消费
        new CountDownLatch(1).await();
    }

    private SendResult sendOrderMessage() {
        BaseMessageModel messageModel = new BaseMessageModel();

        messageModel.setKey(System.currentTimeMillis() + "");
        messageModel.setSource("订单消息");
        messageModel.setBody("发送一条订单消息到消息队列！");
        messageModel.setSendTime(LocalDateTime.now());
        messageModel.setRetryTimes(0);

        return rocketmqTemplate.send("order-topic", messageModel);
    }
}
