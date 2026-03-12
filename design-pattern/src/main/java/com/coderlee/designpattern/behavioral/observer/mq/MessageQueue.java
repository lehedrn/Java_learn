package com.coderlee.designpattern.behavioral.observer.mq;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 消息队列（模拟 RabbitMQ/Kafka）
 * <p>
 * 实现消息的发布/订阅模式
 * 支持按 Topic（主题）进行消息路由
 * </p>
 *
 * @author coderlee
 */
public class MessageQueue {

    /**
     * 存储 Topic 与订阅者列表的映射关系
     * 使用 ConcurrentHashMap 保证线程安全
     */
    private final Map<String, List<MessageSubscriber>> subscribers;

    /**
     * 消息历史记录
     */
    private final List<Message> messageHistory;

    public MessageQueue() {
        this.subscribers = new ConcurrentHashMap<>();
        this.messageHistory = new ArrayList<>();
    }

    /**
     * 订阅 Topic
     *
     * @param topic 主题名称
     * @param subscriber 订阅者
     */
    public void subscribe(String topic, MessageSubscriber subscriber) {
        subscribers.computeIfAbsent(topic, k -> new CopyOnWriteArrayList<>());
        subscribers.get(topic).add(subscriber);
        System.out.println("✅ " + subscriber.getName() + " 订阅了 Topic: " + topic);
    }

    /**
     * 取消订阅
     *
     * @param topic 主题名称
     * @param subscriber 订阅者
     */
    public void unsubscribe(String topic, MessageSubscriber subscriber) {
        List<MessageSubscriber> subscriberList = subscribers.get(topic);
        if (subscriberList != null) {
            subscriberList.remove(subscriber);
            System.out.println("❌ " + subscriber.getName() + " 取消订阅 Topic: " + topic);
        }
    }

    /**
     * 发布消息
     * <p>
     * 将消息发布到指定 Topic，通知所有订阅该 Topic 的订阅者
     * </p>
     *
     * @param topic 主题名称
     * @param message 消息对象
     */
    public void publish(String topic, Message message) {
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("📢 发布消息到 Topic: " + topic);
        System.out.println("   消息内容：" + message.getContent());
        System.out.println("   时间戳：" + message.getTimestamp());

        List<MessageSubscriber> subscriberList = subscribers.get(topic);

        if (subscriberList == null || subscriberList.isEmpty()) {
            System.out.println("⚠️  没有订阅者");
            return;
        }

        System.out.println("   订阅者数量：" + subscriberList.size());
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");

        // 通知所有订阅者
        for (MessageSubscriber subscriber : subscriberList) {
            subscriber.onMessage(message);
        }

        // 记录消息历史
        messageHistory.add(message);
    }

    /**
     * 获取 Topic 的订阅者数量
     *
     * @param topic 主题名称
     * @return 订阅者数量
     */
    public int getSubscriberCount(String topic) {
        List<MessageSubscriber> subscriberList = subscribers.get(topic);
        return subscriberList != null ? subscriberList.size() : 0;
    }

    /**
     * 获取消息历史数量
     *
     * @return 消息历史数量
     */
    public int getMessageHistoryCount() {
        return messageHistory.size();
    }
}
