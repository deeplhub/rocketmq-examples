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
    private boolean enabledIsolation = false;
    private String environment;
}

