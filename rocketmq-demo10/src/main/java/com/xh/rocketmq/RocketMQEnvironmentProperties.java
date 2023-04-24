package com.xh.rocketmq;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author H.Yang
 * @date 2023/4/24
 */
@Data
@ConfigurationProperties(prefix = "rocketmq.environment")
public class RocketMQEnvironmentProperties {
    /**
     * 启动隔离，会自动在topic上拼接激活的配置文件，达到自动隔离的效果
     * <p>
     * 默认为true，配置类：EnvironmentIsolationConfig
     */
    private boolean isolation = false;

    /**
     * 隔离环境名称，拼接到topic后，order_topic_local，默认空字符串
     */
    private String name;
}

