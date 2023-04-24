package com.xh.rocketmq.config;

import com.xh.rocketmq.RocketEnhanceProperties;
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
    private RocketEnhanceProperties rocketEnhanceProperties;


    /**
     * 在装载Bean之前实现参数修改
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof DefaultRocketMQListenerContainer) {
            DefaultRocketMQListenerContainer container = (DefaultRocketMQListenerContainer) bean;

            if (rocketEnhanceProperties.isEnabledIsolation() && StringUtils.hasText(rocketEnhanceProperties.getEnvironment())) {
                container.setTopic(String.join("_", container.getTopic(), rocketEnhanceProperties.getEnvironment()));
            }
            return container;
        }
        return bean;
    }
}

