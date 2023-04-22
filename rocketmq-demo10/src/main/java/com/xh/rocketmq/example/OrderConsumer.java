package com.xh.rocketmq.example;

import com.xh.rocketmq.code.BaseMQMessageListener;
import com.xh.rocketmq.code.BaseMessageModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author H.Yang
 * @date 2023/4/20
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = "order-topic", consumerGroup = "order-consumer-group")
public class OrderConsumer extends BaseMQMessageListener<BaseMessageModel> implements RocketMQListener<BaseMessageModel> {

    @Override
    protected void handleMessage(BaseMessageModel message) throws Exception {
        // 这里是最终的业务处理，代码只需要处理资源类关闭异常，其他的可以交给父类重试
        log.info("消费者消息来源[{}]，收到消息内容[{}]", message.getSource(), message.getBody());
        throw new RuntimeException("故意抛个异常");
    }

    @Override
    protected void overMaxRetryTimesMessage(BaseMessageModel message) {
        // 当超过指定重试次数消息时此处方法会被调用；生产中可以进行回退或其他业务操作
        log.info("{} - 当前消息超过重试次数", message.getKey());
    }

    @Override
    protected boolean isRetry() {
        return true;
    }

    @Override
    protected boolean isThrowException() {
        // 是否抛出异常，到消费异常时是被父类拦截处理还是直接抛出异常
        return false;
    }

    @Override
    protected int maxRetryTimes() {
        // 指定需要的重试次数，超过重试次数overMaxRetryTimesMessage会被调用
        return 5;
    }

    @Override
    public void onMessage(BaseMessageModel message) {
        // 这里不处理业务，而是先委派给父类做基础操作，然后父类做完基础操作后会调用子类的实际处理
        super.dispatchMessage(message);
    }
}
