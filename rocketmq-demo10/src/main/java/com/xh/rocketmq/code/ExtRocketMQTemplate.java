package com.xh.rocketmq.code;

import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * RocketMQ模板类
 * <p>
 * <p>
 * RocketMQTemplate发送消息的代码如果不封装，我们发送消息需要这样String destination = topic + ":" + tag;template.syncSend(destination, message);
 * <p>
 * 每个人发送消息都要自己处理这个冒号，直接传入topic和tag不香吗？
 * <p>
 * 按照抽离变化点中的变化点，只有消息是变化的，除此之外的其他规则交给封装类
 * <p>
 * RocketMQTemplate主要封装发送消息的日志、异常的处理、消息key设置、等等其他配置封装
 *
 * @author H.Yang
 * @date 2023/4/22
 */
@Slf4j
@AllArgsConstructor
@Component
public class ExtRocketMQTemplate {

    private RocketMQTemplate rocketmqTemplate;


    /**
     * 获取模板，如果封装的方法不够提供原生的使用方式
     */
    public RocketMQTemplate getTemplate() {
        return rocketmqTemplate;
    }

    /**
     * 构建目的地
     */
    public String buildDestination(String topic, String tag) {

        return topic + ":" + tag;
    }


    public <T extends BaseMessageModel> SendResult send(String topic, String tag, T model) {
        Message<T> sendMessage = MessageBuilder.withPayload(model).setHeader(RocketMQHeaders.KEYS, model.getKey()).build();
        SendResult sendResult = rocketmqTemplate.syncSend(this.buildDestination(topic, tag), sendMessage);

        log.info("[{}]同步消息[{}]发送结果[{}]", this.buildDestination(topic, tag), JSONUtil.toJsonStr(sendMessage), JSONUtil.toJsonStr(sendResult));

        return sendResult;
    }

    public <T extends BaseMessageModel> SendResult send(String destination, T model) {
        Message<T> sendMessage = MessageBuilder.withPayload(model).setHeader(RocketMQHeaders.KEYS, model.getKey()).build();
        SendResult sendResult = rocketmqTemplate.syncSend(destination, sendMessage);

        log.info("[{}]同步消息[{}]发送结果[{}]", destination, JSONUtil.toJsonStr(sendMessage), JSONUtil.toJsonStr(sendResult));

        return sendResult;
    }

    public <T extends BaseMessageModel> SendResult send(String destination, T model, int delayLevel) {
        Message<T> sendMessage = MessageBuilder.withPayload(model).setHeader(RocketMQHeaders.KEYS, model.getKey()).build();
        SendResult sendResult = rocketmqTemplate.syncSend(destination, sendMessage, 3000, delayLevel);

        log.info("[{}]延迟等级[{}]消息[{}]发送结果[{}]", destination, delayLevel, JSONUtil.toJsonStr(sendMessage), JSONUtil.toJsonStr(sendResult));
        return sendResult;

    }

}
