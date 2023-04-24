package com.xh.rocketmq.config;

/**
 * @author H.Yang
 * @date 2023/4/24
 */

import com.xh.rocketmq.RocketEnhanceProperties;
import com.xh.rocketmq.templete.RocketMQEnhanceTemplate;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MQ 增强自动配置
 *
 * @author H.Yang
 * @date 2023/4/24
 */
@Configuration
@EnableConfigurationProperties(RocketEnhanceProperties.class)
public class RocketMQEnhanceAutoConfiguration {

    /**
     * 注入增强的RocketMQEnhanceTemplate
     */
    @Bean
    public RocketMQEnhanceTemplate rocketMQEnhanceTemplate(RocketEnhanceProperties rocketEnhanceProperties, RocketMQTemplate rocketMQTemplate) {
        return new RocketMQEnhanceTemplate(rocketEnhanceProperties, rocketMQTemplate);
    }

    /**
     * 环境隔离配置
     */
    @Bean
    @ConditionalOnProperty(name = "rocketmq.enhance.enabledIsolation", havingValue = "true")
    public EnvironmentIsolationConfiguration environmentSetup(RocketEnhanceProperties rocketEnhanceProperties) {

        return new EnvironmentIsolationConfiguration(rocketEnhanceProperties);
    }
}
