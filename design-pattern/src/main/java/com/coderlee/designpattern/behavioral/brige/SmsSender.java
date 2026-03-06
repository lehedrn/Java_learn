package com.coderlee.designpattern.behavioral.brige;

import lombok.extern.slf4j.Slf4j;

/**
 * 短信发送器（具体实现化角色）
 * 实现了 MessageSender 接口，提供短信发送的具体实现
 * 使用 Lombok 的 @Slf4j 注解自动生成日志对象
 */
@Slf4j
public class SmsSender implements MessageSender{
    /**
     * 使用短信方式发送消息
     * 记录日志信息，显示通过短信发送的消息内容
     * @param message 要发送的消息内容
     */
    @Override
    public void send(String message) {
        log.info("使用短信发送消息：{}", message);
    }
}
