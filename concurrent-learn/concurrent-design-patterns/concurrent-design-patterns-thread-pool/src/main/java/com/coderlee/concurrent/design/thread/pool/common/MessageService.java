/**
 * 消息服务接口
 * <p>
 * 定义了发送消息的基本操作，用于解耦消息发送的具体实现。
 *
 * @see MessageServiceImpl 消息服务的默认实现
 * </p>
 */
package com.coderlee.concurrent.design.thread.pool.common;

public interface MessageService {
    /**
     * 发送指定内容的消息
     *
     * @param message 要发送的消息内容，不能为空
     */
    void sendMessage(String message);
}
