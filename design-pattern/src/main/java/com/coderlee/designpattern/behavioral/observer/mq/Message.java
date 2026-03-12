package com.coderlee.designpattern.behavioral.observer.mq;

/**
 * 消息接口
 * <p>
 * 所有消息都实现此接口
 * </p>
 *
 * @author coderlee
 */
public interface Message {
    /**
     * 获取消息类型（Topic）
     *
     * @return 消息类型
     */
    String getType();

    /**
     * 获取消息内容
     *
     * @return 消息内容
     */
    String getContent();

    /**
     * 获取消息时间戳
     *
     * @return 时间戳
     */
    long getTimestamp();
}
