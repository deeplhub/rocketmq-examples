package com.xh.rocketmq.code;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 物流发送消息模板工具类
 *
 * @author H.Yang
 * @date 2023/4/22
 */
@Slf4j
@Component
public class StorageRocketMQTemplate extends ExtRocketMQTemplate {

    public StorageRocketMQTemplate(RocketMQTemplate rocketmqTemplate) {
        super(rocketmqTemplate);
    }


    public SendResult sendStorageMessage() {
        BaseMessageModel messageModel = new BaseMessageModel();

        messageModel.setKey(System.currentTimeMillis() + "");
        messageModel.setSource("物流消息");
        messageModel.setBody("发送一条物流消息到消息队列！");
        messageModel.setSendTime(LocalDateTime.now());
        messageModel.setRetryTimes(0);

        return super.send("storage-topic", messageModel);
    }
}
