package com.coderlee.designpattern.behavioral.brige;

/**
 * 消息发送器接口（实现化角色）
 * 定义了消息发送的标准接口，所有的具体发送器都需要实现此接口
 * 这是桥接模式中的实现部分，与消息类型抽象部分独立变化
 */
public interface MessageSender {
    /**
     * 发送消息
     * @param message 要发送的消息内容
     */
    void send(String message);
}
