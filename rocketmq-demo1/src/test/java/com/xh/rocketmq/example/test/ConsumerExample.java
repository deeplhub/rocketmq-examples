package com.xh.rocketmq.example.test;

import org.apache.rocketmq.client.consumer.DefaultLitePullConsumer;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author H.Yang
 * @date 2023/2/27
 */
public class ConsumerExample {

    @Test
    public void consumer() throws Exception {
        // 实例化消费者
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(Cons.DEFAULT_GROUP);

        // 设置NameServer的地址
        consumer.setNamesrvAddr(Cons.HOST);

        // 订阅一个或者多个Topic，以及Tag来过滤需要消费的消息
        consumer.subscribe(Cons.DEFAULT_TOPIC, "*");
        // 注册回调实现类来处理从broker拉取回来的消息
        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(), msgs);
            // 标记该消息已经被成功消费
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        // 启动消费者实例
        consumer.start();
        System.out.printf("Consumer Started.%n");
        System.in.read();
    }

    @Test
    public void asyncConsumer() throws Exception {
        // 实例化消费者
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(Cons.ASYNC_GROUP);

        // 设置NameServer的地址
        consumer.setNamesrvAddr(Cons.HOST);

        // 订阅一个或者多个Topic，以及Tag来过滤需要消费的消息
        consumer.subscribe(Cons.ASYNC_TOPIC, "*");
        // 注册回调实现类来处理从broker拉取回来的消息
        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(), msgs);
            // 标记该消息已经被成功消费
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        // 启动消费者实例
        consumer.start();
        System.out.printf("Consumer Started.%n");
        System.in.read();
    }

    @Test
    public void onewayConsumer() throws Exception {
        // 实例化消费者
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(Cons.ONEWAY_GROUP);

        // 设置NameServer的地址
        consumer.setNamesrvAddr(Cons.HOST);

        // 订阅一个或者多个Topic，以及Tag来过滤需要消费的消息
        consumer.subscribe(Cons.ONEWAY_TOPIC, "*");
        // 注册回调实现类来处理从broker拉取回来的消息
        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(), msgs);
            // 标记该消息已经被成功消费
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        // 启动消费者实例
        consumer.start();
        System.out.printf("Consumer Started.%n");
        System.in.read();
    }


    /**
     * 拉取消费
     * <p>
     * 主动获取 broker 服务器上的消费
     *
     * @throws Exception
     */
    @Test
    public void consumerPull() throws Exception {
        DefaultLitePullConsumer consumer = new DefaultLitePullConsumer(Cons.DEFAULT_GROUP);
        consumer.setNamesrvAddr(Cons.HOST);
        consumer.start();

        Collection<MessageQueue> mqs = consumer.fetchMessageQueues(Cons.DEFAULT_TOPIC);

        System.out.println("queues:");
        mqs.forEach(messageQueue -> System.out.println(messageQueue));

        System.out.println("poll....");

        Collection<MessageQueue> queue = new ArrayList<>();
        MessageQueue qu = new MessageQueue(Cons.DEFAULT_TOPIC, "broker-a", 0);
        queue.add(qu);
        consumer.assign(queue);
        consumer.seek(qu, 4);

        List<MessageExt> poll = consumer.poll();
        poll.forEach(s -> System.out.println(s));


        System.in.read();

    }

}
