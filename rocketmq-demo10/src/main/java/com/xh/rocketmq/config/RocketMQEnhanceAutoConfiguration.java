package com.xh.rocketmq.config;

/**
 * @author H.Yang
 * @date 2023/4/24
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.xh.rocketmq.RocketMQEnvironmentProperties;
import com.xh.rocketmq.templete.RocketMQEnhanceTemplate;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQMessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.converter.CompositeMessageConverter;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;

import java.util.List;

/**
 * MQ 增强自动配置
 *
 * @author H.Yang
 * @date 2023/4/24
 */
@Configuration
@EnableConfigurationProperties(RocketMQEnvironmentProperties.class)
public class RocketMQEnhanceAutoConfiguration {

    /**
     * 注入增强的RocketMQEnhanceTemplate
     */
    @Bean
    public RocketMQEnhanceTemplate rocketMQEnhanceTemplate(RocketMQEnvironmentProperties rocketmqEnvironmentProperties, RocketMQTemplate rocketMQTemplate) {
        return new RocketMQEnhanceTemplate(rocketmqEnvironmentProperties, rocketMQTemplate);
    }

    /**
     * 环境隔离配置
     */
    @Bean
    @ConditionalOnProperty(name = "rocketmq.environment.isolation", havingValue = "true")
    public EnvironmentIsolationConfiguration environmentSetup(RocketMQEnvironmentProperties rocketmqEnvironmentProperties) {

        return new EnvironmentIsolationConfiguration(rocketmqEnvironmentProperties);
    }


    /**
     * 解决RocketMQ Jackson不支持Java时间类型配置
     * 源码参考：org.apache.rocketmq.spring.autoconfigure.MessageConverterConfiguration
     */
    @Bean
    @Primary
    public RocketMQMessageConverter createRocketMQMessageConverter() {
        RocketMQMessageConverter converter = new RocketMQMessageConverter();
        CompositeMessageConverter compositeMessageConverter = (CompositeMessageConverter) converter.getMessageConverter();
        List<MessageConverter> messageConverterList = compositeMessageConverter.getConverters();
        for (MessageConverter messageConverter : messageConverterList) {
            if (messageConverter instanceof MappingJackson2MessageConverter) {
                MappingJackson2MessageConverter jackson2MessageConverter = (MappingJackson2MessageConverter) messageConverter;
                ObjectMapper objectMapper = jackson2MessageConverter.getObjectMapper();
                objectMapper.registerModules(new JavaTimeModule());
            }
        }
        return converter;
    }
}
