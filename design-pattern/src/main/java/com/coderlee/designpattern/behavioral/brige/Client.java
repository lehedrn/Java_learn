package com.coderlee.designpattern.behavioral.brige;

/**
 * 桥接模式客户端测试类
 * 演示如何使用桥接模式组合不同的消息类型和发送方式
 * 
 * 桥接模式的优点：
 * 1. 解耦抽象和实现，两者可以独立变化
 * 2. 符合开闭原则，新增消息类型或发送方式无需修改现有代码
 * 3. 避免继承爆炸，减少类的数量
 * 
 * 示例场景：
 * - 普通消息 + 短信发送
 * - 紧急消息 + 邮件发送
 */
public class Client {
    public static void main(String[] args) {
        // 创建短信发送器实例
        MessageSender smsSender = new SmsSender();
        // 创建普通消息，使用短信发送
        Message message1 = new NormalMessage(smsSender);
        // 发送消息
        message1.send("hello world");

        // 创建邮件发送器实例
        MessageSender emailSender = new EmailSender();
        // 创建紧急消息，使用邮件发送
        UrgentMessage message2 = new UrgentMessage(emailSender);
        // 发送紧急消息
        message2.send("服务器宕机了！！！");
    }
}
