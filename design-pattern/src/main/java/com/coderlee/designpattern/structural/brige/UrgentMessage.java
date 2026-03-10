package com.coderlee.designpattern.structural.brige;

/**
 * 紧急消息类（具体抽象化角色）
 * 继承自 Message 抽象类，表示高优先级/紧急的消息
 * 在发送前会对消息内容进行格式化标记
 */
public class UrgentMessage extends Message {
    /**
     * 构造方法，传入消息发送器
     * @param messageSender 消息发送器实例
     */
    public UrgentMessage(MessageSender messageSender) {
        super(messageSender);
    }

    /**
     * 发送紧急消息的实现
     * 将消息格式化为"UrgentMessage: {消息内容}"的格式
     * 然后委托给消息发送器进行实际发送
     * @param message 要发送的消息内容
     */
    @Override
    public void send(String message) {
        // 格式化紧急消息，并委托给具体的发送器发送
        messageSender.send(String.format("UrgentMessage: %s", message));
    }
}
