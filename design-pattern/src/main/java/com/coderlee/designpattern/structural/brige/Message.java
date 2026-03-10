package com.coderlee.designpattern.structural.brige;

/**
 * 消息抽象类（抽象化角色）
 * 定义了消息的类型和普通消息、紧急消息等具体消息
 * 持有消息发送器的引用，将抽象部分与实现部分连接起来
 * 这是桥接模式中的桥接类，使得消息类型和发送方式可以独立变化
 */
public abstract class Message {
    /**
     * 消息发送器接口引用
     * 用于委托具体的消息发送操作给实现化角色
     */
    protected MessageSender messageSender;
    
    /**
     * 构造方法，注入消息发送器
     * @param messageSender 消息发送器实例
     */
    public Message(MessageSender messageSender) {
        this.messageSender = messageSender;
    }
    
    /**
     * 发送消息的抽象方法
     * 由子类实现具体的消息处理逻辑
     * @param message 要发送的消息内容
     */
    public abstract void send(String message);
}
