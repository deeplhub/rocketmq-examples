package com.xh.rocketmq.example.test;

/**
 * @author H.Yang
 * @date 2023/2/27
 */
public interface Cons {

    String HOST = "192.168.100.1:9876";

    String DEFAULT_GROUP = "test";
    String DEFAULT_TOPIC = "TopicTest";
    String DEFAULT_TAG = "TagTest";

    String ASYNC_GROUP = "test_async";
    String ASYNC_TOPIC = "TopicAsyncTest";
    String ASYNC_TAG = "TagTest";
    String ASYNC_KEY = "KeyTest";

    String ONEWAY_GROUP = "test_oneway";
    String ONEWAY_TOPIC = "TopicOnewayTest";
    String ONEWAY_TAG = "TagTest";
    String ONEWAY_KEY = "KeyTest";


}
