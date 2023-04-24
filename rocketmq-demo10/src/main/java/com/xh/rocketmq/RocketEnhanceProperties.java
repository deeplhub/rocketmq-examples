package com.xh.rocketmq;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author H.Yang
 * @date 2023/4/24
 */
@Data
@ConfigurationProperties(prefix = "rocketmq.enhance")
public class RocketEnhanceProperties {
    /**
     * 启动隔离，启动后会自动在topic上拼接激活的配置文件，达到自动隔离的效果
     */
    private boolean enabledIsolation = false;

    /**
     * 隔离名称, 拼接到topic后，topic_dev，默认空字符串
     */
    private String environment;
}

