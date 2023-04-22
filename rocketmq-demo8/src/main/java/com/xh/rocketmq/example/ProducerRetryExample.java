package com.xh.rocketmq.example;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author H.Yang
 * @date 2023/4/20
 */
@Slf4j
@RestController
public class ProducerRetryExample {

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @GetMapping("/retryMessage")
    public String retryMessage() {
        rocketMQTemplate.syncSend("retry-topic", "重试消息");

        return "success";
    }
}
