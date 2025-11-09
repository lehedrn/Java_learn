package com.coderlee.concurrent.design.example.right;

import lombok.Getter;

/**
 * 不可变消息信息类
 *
 * 该类是一个线程安全的不可变对象，用于封装设备编码和消息URL信息。
 * 通过将所有字段声明为final并提供只读访问器，确保对象一旦创建后其状态不能被修改。
 *
 * @see <a href="https://docs.oracle.com/javase/tutorial/essential/concurrency/immutable.html">Immutable Objects</a>
 */
@Getter
public final class MessageInfo {

    /** 设备编码 */
    private final String deviceCode;

    /** 消息URL */
    private final String messageUrl;

    /**
     * 构造一个新的消息信息对象
     *
     * @param deviceCode 设备编码，不能为空
     * @param messageUrl 消息URL，不能为空
     */
    public MessageInfo(String deviceCode, String messageUrl) {
        this.deviceCode = deviceCode;
        this.messageUrl = messageUrl;
    }

    /**
     * 通过拷贝现有消息信息对象创建新的实例
     *
     * @param messageInfo 要拷贝的消息信息对象
     * @see {@link #MessageInfo(String, String)}
     */
    public MessageInfo(MessageInfo messageInfo) {
        this.deviceCode = messageInfo.deviceCode;
        this.messageUrl = messageInfo.messageUrl;
    }
}
