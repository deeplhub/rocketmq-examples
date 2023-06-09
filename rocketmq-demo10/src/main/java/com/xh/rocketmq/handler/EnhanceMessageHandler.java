package com.xh.rocketmq.handler;

import cn.hutool.json.JSONUtil;
import com.xh.rocketmq.templete.RocketMQEnhanceTemplate;
import com.xh.rocketmq.model.BaseMessageModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;

import javax.annotation.Resource;
import java.time.Instant;

/**
 * 抽象消息监听器，封装了所有公共处理业务，如：基础日志记录、异常处理、消息重试、警告通知
 *
 * @author H.Yang
 * @date 2023/4/22
 */
@Slf4j
public abstract class EnhanceMessageHandler<T extends BaseMessageModel> {

    /**
     * 重试前缀
     */
    private static final String RETRY_PREFIX = "RETRY_";

    @Resource
    private RocketMQEnhanceTemplate enhanceTemplate;

    /**
     * 消息处理
     *
     * @param message 待处理消息
     * @throws Exception 消费异常
     */

    protected abstract void handleMessage(T message) throws Exception;

    /**
     * 超过重试次数消息，需要启用isRetry
     *
     * @param message 待处理消息
     */

    protected abstract void handleMaxRetriesExceeded(T message);

    /**
     * 是否过滤消息，例如某些
     *
     * @param message 待处理消息
     * @return true: 本次消息被过滤，false：不过滤
     */

    protected boolean isFilter(T message) {

        return false;
    }

    /**
     * 异常时是否重复发送
     *
     * @return true: 消息重试，false：不重试
     */

    protected abstract boolean isRetry();

    /**
     * 消费异常时是否抛出异常
     *
     * @return true: 抛出异常，则由rocketmq机制自动重试；false：消费异常(如果没有开启重试则消息会被自动ack)
     */

    protected abstract boolean throwException();

    /**
     * 最大重试次数
     *
     * @return 最大重试次数，默认5次
     */

    protected int maxRetryTimes() {
        return 5;
    }

    /**
     * 获取延迟级别，默认延迟1分钟
     * <p>
     * -1：立即入队重试
     *
     * @return 默认延迟级别5（延迟时间1分钟）
     */

    protected int getDelayLevel() {
        return 5;
    }

    /**
     * 使用模板模式构建消息消费框架，可自由扩展或删减
     *
     * @param message
     */
    public void dispatchMessage(T message) {
        // 基础日志记录被父类处理了
        log.info("消费者收到消息[{}]", JSONUtil.toJsonStr(message));
        if (this.isFilter(message)) {
            log.info("消息id{}不满足消费条件，已过滤。", message.getKey());
            return;
        }

        // 超过最大重试次数时调用子类方法处理
        if (message.getRetryTimes() > this.maxRetryTimes()) {
            this.handleMaxRetriesExceeded(message);
            return;
        }

        try {
            long now = Instant.now().toEpochMilli();
            this.handleMessage(message);
            long costTime = Instant.now().toEpochMilli() - now;
            log.info("消息{}消费成功，耗时[{}ms]", message.getKey(), costTime);
        } catch (Exception e) {
            log.error("消息{}消费异常", message.getKey(), e);
            // 是捕获异常还是抛出，由子类决定
            if (this.throwException()) {
                throw new RuntimeException(e);
            }
            // 异常时是否重复发送
            if (!this.isRetry()) {
                return;
            }
            // 此时如果不开启重试机制，则默认ACK了
            this.handleRetry(message);
        }
    }

    protected void handleRetry(T message) {
        // 获取子类RocketMQMessageListener注解拿到topic和tag
        RocketMQMessageListener annotation = this.getClass().getAnnotation(RocketMQMessageListener.class);
        if (annotation == null) {
            return;
        }

        //重新构建消息体
        String messageSource = message.getSource();
        if (!messageSource.startsWith(RETRY_PREFIX)) {
            message.setSource(RETRY_PREFIX + messageSource);
        }
        message.setRetryTimes(message.getRetryTimes() + 1);

        SendResult sendResult;

        try {
            // 如果消息发送不成功，则再次重新发送，如果发送异常则抛出由MQ再次处理(异常时不走延迟消息)
            sendResult = enhanceTemplate.send(annotation.topic(), annotation.selectorExpression(), message, this.getDelayLevel());
        } catch (Exception ex) {
            // 此处捕获之后，相当于此条消息被消息完成然后重新发送新的消息
            throw new RuntimeException(ex);
        }
        // 发送失败的处理就是不进行ACK，由RocketMQ重试
        if (sendResult.getSendStatus() != SendStatus.SEND_OK) {
            throw new RuntimeException("重试消息发送失败");
        }

    }
}
