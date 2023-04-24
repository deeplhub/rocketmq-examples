package com.xh.rocketmq.config;

import com.xh.rocketmq.RocketMQEnvironmentProperties;
import lombok.AllArgsConstructor;
import org.apache.rocketmq.spring.support.DefaultRocketMQListenerContainer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.StringUtils;

/**
 * 环境隔离配置
 *
 * @author H.Yang
 * @date 2023/4/24
 */
@AllArgsConstructor
public class EnvironmentIsolationConfiguration implements BeanPostProcessor {
    private RocketMQEnvironmentProperties environmentProperties;


    /**
     * 在装载Bean之前实现参数修改
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof DefaultRocketMQListenerContainer) {
            DefaultRocketMQListenerContainer container = (DefaultRocketMQListenerContainer) bean;
            // 开启消息隔离情况下获取隔离配置，此处隔离topic，根据自己的需求隔离group或者tag
            if (environmentProperties.isIsolation() && StringUtils.hasText(environmentProperties.getName())) {
                container.setTopic(String.join("_", container.getTopic(), environmentProperties.getName()));
            }
            return container;
        }
        return bean;
    }
}

