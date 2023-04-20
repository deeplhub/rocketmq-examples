package com.xh.rocketmq.example.test;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.CountDownLatch2;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author H.Yang
 * @date 2023/2/27
 */
@Slf4j
public class ProducerExample {

    /**
     * 同步消息
     * <p>
     * 这种可靠性同步地发送方式使用的比较广泛，比如：重要的消息通知，短信通知。
     */
    @Test
    public void producer() throws Exception {
        // 实例化消息生产者Producer
        DefaultMQProducer producer = new DefaultMQProducer(Cons.DEFAULT_GROUP);
        // 设置NameServer的地址
        producer.setNamesrvAddr(Cons.HOST);
        // 启动Producer实例
        producer.start();

        for (int i = 0; i < 10; i++) {
            // 创建消息，并指定Topic，Tag和消息体
            Message msg = new Message(Cons.DEFAULT_TOPIC, Cons.DEFAULT_TAG, ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            // 发送消息到一个Broker
            SendResult sendResult = producer.send(msg);
            // 通过sendResult返回消息是否成功送达
            System.out.printf("%s%n", sendResult);
        }
        // 如果不再发送消息，关闭Producer实例。
        producer.shutdown();
    }

    /**
     * 发送异步消息
     * <p>
     * 异步消息通常用在对响应时间敏感的业务场景，即发送端不能容忍长时间地等待Broker的响应。
     *
     * @throws Exception
     */
    @Test
    public void asyncProducer() throws Exception {
        // 实例化消息生产者Producer
        DefaultMQProducer producer = new DefaultMQProducer(Cons.ASYNC_GROUP);
        // 设置NameServer的地址
        producer.setNamesrvAddr(Cons.HOST);
        // 启动Producer实例
        producer.start();
        producer.setRetryTimesWhenSendAsyncFailed(0);

        int messageCount = 10;
        // 根据消息数量实例化倒计时计算器
        final CountDownLatch2 countDownLatch = new CountDownLatch2(messageCount);
        for (int i = 0; i < messageCount; i++) {
            final int index = i;
            // 创建消息，并指定Topic，Tag和消息体
            Message msg = new Message(Cons.ASYNC_TOPIC, Cons.ASYNC_TAG, Cons.ASYNC_KEY, "Hello world".getBytes(RemotingHelper.DEFAULT_CHARSET));
            // SendCallback接收异步返回结果的回调
            producer.send(msg, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    countDownLatch.countDown();
                    System.out.printf("%-10d OK %s %n", index,
                            sendResult.getMsgId());
                }

                @Override
                public void onException(Throwable e) {
                    countDownLatch.countDown();
                    System.out.printf("%-10d Exception %s %n", index, e);
                    e.printStackTrace();
                }
            });
        }
        // 等待5s
        countDownLatch.await(5, TimeUnit.SECONDS);
        // 如果不再发送消息，关闭Producer实例。
        producer.shutdown();
    }

    /**
     * 单向发送消息
     * <p>
     * 这种方式主要用在不特别关心发送结果的场景，例如日志发送。
     *
     * @throws Exception
     */
    @Test
    public void onewayProducer() throws Exception {
        // 实例化消息生产者Producer
        DefaultMQProducer producer = new DefaultMQProducer(Cons.ONEWAY_GROUP);
        // 设置NameServer的地址
        producer.setNamesrvAddr(Cons.HOST);
        // 启动Producer实例
        producer.start();
        for (int i = 0; i < 100; i++) {
            // 创建消息，并指定Topic，Tag和消息体
            Message msg = new Message(Cons.ONEWAY_TOPIC, Cons.ONEWAY_TAG, ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            // 发送单向消息，没有任何返回结果
            producer.sendOneway(msg);

        }
        // 如果不再发送消息，关闭Producer实例。
        producer.shutdown();
    }


    @Test
    public void admin() {
        log.info("AAAAAAAAAAAAA");
    }

}
