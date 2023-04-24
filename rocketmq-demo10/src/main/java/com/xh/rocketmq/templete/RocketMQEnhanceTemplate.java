package com.xh.rocketmq.templete;

import cn.hutool.json.JSONUtil;
import com.xh.rocketmq.model.BaseMessageModel;
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
@Component
public class RocketMQEnhanceTemplate {
    //    @Resource
//    private RocketEnhanceProperties rocketEnhanceProperties;
    private RocketMQTemplate rocketmqTemplate;

    public RocketMQEnhanceTemplate(RocketMQTemplate rocketmqTemplate) {
        this.rocketmqTemplate = rocketmqTemplate;
    }

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


//    /**
//     * 根据环境重新隔离topic
//     *
//     * @param topic 原始topic
//     */
//    private String reBuildTopic(String topic) {
//        if (rocketEnhanceProperties.isEnabledIsolation() && StringUtils.hasText(rocketEnhanceProperties.getEnvironment())) {
//            return topic + "_" + rocketEnhanceProperties.getEnvironment();
//        }
//        return topic;
//    }


    /**
     * 发送同步消息
     *
     * @param topic
     * @param tag
     * @param model
     * @param <T>
     * @return
     */
    public <T extends BaseMessageModel> SendResult send(String topic, String tag, T model) {

        return this.send(this.buildDestination(topic, tag), model);
    }

    public <T extends BaseMessageModel> SendResult send(String destination, T model) {
        Message<T> sendMessage = MessageBuilder.withPayload(model).setHeader(RocketMQHeaders.KEYS, model.getKey()).build();
        SendResult sendResult = rocketmqTemplate.syncSend(destination, sendMessage);

        log.info("[{}]同步消息[{}]发送结果[{}]", destination, JSONUtil.toJsonStr(sendMessage), JSONUtil.toJsonStr(sendResult));

        return sendResult;
    }

    /**
     * 发送延迟消息
     *
     * @param topic
     * @param tag
     * @param message
     * @param delayLevel
     * @param <T>
     * @return
     */
    public <T extends BaseMessageModel> SendResult send(String topic, String tag, T message, int delayLevel) {

        return this.send(this.buildDestination(topic, tag), message, delayLevel);
    }


    public <T extends BaseMessageModel> SendResult send(String destination, T model, int delayLevel) {
        Message<T> sendMessage = MessageBuilder.withPayload(model).setHeader(RocketMQHeaders.KEYS, model.getKey()).build();
        SendResult sendResult = rocketmqTemplate.syncSend(destination, sendMessage, 3000, delayLevel);

        log.info("[{}]延迟等级[{}]消息[{}]发送结果[{}]", destination, delayLevel, JSONUtil.toJsonStr(sendMessage), JSONUtil.toJsonStr(sendResult));
        return sendResult;

    }

}
