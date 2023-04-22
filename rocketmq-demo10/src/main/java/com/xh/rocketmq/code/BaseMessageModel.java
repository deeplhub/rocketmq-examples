package com.xh.rocketmq.code;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 基础消息实体
 *
 * @author H.Yang
 * @date 2023/4/22
 */
@Data
public class BaseMessageModel {

    /**
     * 业务键，用于RocketMQ控制台查看消费情况
     */

    protected String key;

    /**
     * 发送消息来源，用于排查问题
     */

    protected String source;

    /**
     * 消息报文
     */
    protected String body;

    /**
     * 发送时间
     */

    protected LocalDateTime sendTime = LocalDateTime.now();


    /**
     * 重试次数，用于判断重试次数，超过重试次数发送异常警告
     */
    protected Integer retryTimes = 0;

}
