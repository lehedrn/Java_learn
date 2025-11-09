package com.coderlee.concurrent.design.example.wrong;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息路由类，用于管理和更新消息路由信息。
 * <p>
 * 该类采用单例模式实现，并维护一个不可变的消息路由映射表。
 * 路由表通过 {@link #loadMessageInfoMap()} 方法初始化，并提供对外访问接口。
 * 
 * @see MessageInfo
 */
public class MessageRouter {

    /**
     * 单例实例，使用 volatile 修饰以保证多线程环境下的可见性。
     */
    private static volatile MessageRouter instance = new MessageRouter();

    /**
     * 消息路由映射表，存储路由键与消息信息的映射关系。
     */
    private final Map<String, MessageInfo> messageInfoMap;

    /**
     * 私有构造函数，用于初始化消息路由映射表。
     * <p>
     * 调用 {@link #loadMessageInfoMap()} 方法加载初始数据。
     */
    public MessageRouter() {
        this.messageInfoMap = this.loadMessageInfoMap();
    }

    /**
     * 加载消息路由映射表的初始数据。
     * <p>
     * 初始化包含多个预定义路由键和对应的消息信息。
     *
     * @return 包含初始数据的消息路由映射表
     */
    private Map<String, MessageInfo> loadMessageInfoMap() {
        // 创建一个新的 HashMap 实例用于存储消息路由信息
        Map<String, MessageInfo> map = new HashMap<>();
        
        // 添加极光推送的消息路由信息
        map.put("jiguang", new MessageInfo("1001", "https://www.jiguang.cn"));
        
        // 添加信鸽推送的消息路由信息
        map.put("xinge", new MessageInfo("1002", "https://www.xinge.cn"));
        
        // 添加友盟推送的消息路由信息
        map.put("umeng", new MessageInfo("1003", "https://www.umeng.com"));
        
        return map;
    }

    /**
     * 获取当前的消息路由映射表。
     * <p>
     * 返回的消息路由映射表是不可变的，调用者不应直接修改返回值。
     * </p>
     * @return 当前的消息路由映射表
     */
    public Map<String, MessageInfo> getMessageInfoMap() {
        return messageInfoMap;
    }

    /**
     * 更新指定路由键的消息路由信息。
     * <p>
     * 根据路由键查找对应的消息信息，并更新设备代码和消息 URL。
     *
     * @param routeKey 路由键，用于定位消息信息
     * @param deviceCode 新的设备代码
     * @param messageUrl 新的消息 URL
     */
    public void updateRoute(String routeKey, String deviceCode, String messageUrl) {
        // 获取当前的消息路由映射表
        Map<String, MessageInfo> map = instance.getMessageInfoMap();
        
        // 根据路由键获取对应的消息信息
        MessageInfo messageInfo = map.get(routeKey);
        
        // 更新消息信息中的设备代码
        messageInfo.setDeviceCode(deviceCode);
        
        // 更新消息信息中的消息 URL
        messageInfo.setMessageUrl(messageUrl);
        
        // 将更新后的消息信息重新放入映射表中
        map.put(routeKey, messageInfo);
    }
}