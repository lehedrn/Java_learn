package com.coderlee.designpattern.behavioral.brige;

/**
 * 普通消息类（具体抽象化角色）
 * 继承自 Message 抽象类，表示普通优先级的消息
 * 在发送前会对消息内容进行格式化标记
 */
public class NormalMessage extends Message {
    /**
     * 构造方法，传入消息发送器
     * @param messageSender 消息发送器实例
     */
    public NormalMessage(MessageSender messageSender) {
        super(messageSender);
    }

    /**
     * 发送普通消息的实现
     * 将消息格式化为"normal message: {消息内容}"的格式
     * 然后委托给消息发送器进行实际发送
     * @param message 要发送的消息内容
     */
    @Override
    public void send(String message) {
        // 格式化普通消息，并委托给具体的发送器发送
        messageSender.send(String.format("normal message: %s", message));
    }
}
