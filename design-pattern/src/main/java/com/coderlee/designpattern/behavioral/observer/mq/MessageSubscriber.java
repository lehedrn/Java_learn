package com.coderlee.designpattern.behavioral.observer.mq;

/**
 * 消息订阅者接口
 * <p>
 * 所有消息订阅者都实现此接口
 * </p>
 *
 * @author coderlee
 */
public interface MessageSubscriber {
    /**
     * 接收消息
     *
     * @param message 消息对象
     */
    void onMessage(Message message);

    /**
     * 获取订阅者名称
     *
     * @return 名称
     */
    String getName();

    /**
     * 获取订阅的 Topic
     *
     * @return Topic
     */
    String getSubscribedTopic();
}
