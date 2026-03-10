package com.coderlee.designpattern.structural.brige;

import lombok.extern.slf4j.Slf4j;

/**
 * 微信发送器（具体实现化角色）
 * 实现了 MessageSender 接口，提供微信发送的具体实现
 * 使用 Lombok 的 @Slf4j 注解自动生成日志对象
 */
@Slf4j
public class WechatSender implements MessageSender {
    /**
     * 使用微信方式发送消息
     * 记录日志信息，显示通过微信发送的消息内容
     * @param message 要发送的消息内容
     */
    @Override
    public void send(String message) {
        log.info("使用微信发送消息：{}", message);
    }
}
