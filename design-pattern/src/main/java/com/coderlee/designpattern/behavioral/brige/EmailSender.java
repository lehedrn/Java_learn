package com.coderlee.designpattern.behavioral.brige;

import lombok.extern.slf4j.Slf4j;

/**
 * 邮件发送器（具体实现化角色）
 * 实现了 MessageSender 接口，提供邮件发送的具体实现
 * 使用 Lombok 的 @Slf4j 注解自动生成日志对象
 */
@Slf4j
public class EmailSender implements MessageSender {
    /**
     * 使用邮件方式发送消息
     * 记录日志信息，显示通过邮件发送的消息内容
     * @param message 要发送的消息内容
     */
    @Override
    public void send(String message) {
        log.info("使用邮件发送消息：{}", message);
    }
}
