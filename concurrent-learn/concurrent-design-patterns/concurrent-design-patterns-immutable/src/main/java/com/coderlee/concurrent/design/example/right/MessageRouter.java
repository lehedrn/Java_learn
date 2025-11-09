/**
 * 消息路由管理器
 *
 * 该类实现了线程安全的消息路由表管理，采用单例模式和不可变对象设计模式，
 * 确保在多线程环境下对消息路由信息的访问是安全的。
 *
 * 当需要更新路由信息时，通过创建新的实例来替换旧实例的方式实现缓存刷新，
 * 避免了并发修改异常的问题。
 *
 * @author coderlee
 * @see MessageInfo
 */
package com.coderlee.concurrent.design.example.right;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MessageRouter {

    /**
     * 单例实例，使用volatile保证可见性
     */
    private static volatile MessageRouter instance = new MessageRouter();

    /**
     * 存储消息路由信息的不可变映射表
     */
    private final Map<String, MessageInfo> messageInfoMap;

    /**
     * 私有构造函数，初始化消息路由表
     */
    private MessageRouter() {
        messageInfoMap = this.loadMessageInfoMap();
    }

    /**
     * 加载初始消息路由信息
     *
     * @return 包含预定义消息路由信息的映射表
     */
    private Map<String, MessageInfo> loadMessageInfoMap() {
        Map<String, MessageInfo> map = new HashMap<>();

        // 添加极光推送路由信息
        map.put("jiguang", new MessageInfo("1001", "https://www.jiguang.cn"));

        // 添加信鸽推送路由信息
        map.put("xinge", new MessageInfo("1002", "https://www.xinge.cn"));

        // 添加友盟推送路由信息
        map.put("umeng", new MessageInfo("1003", "https://www.umeng.com"));

        return map;
    }

    /**
     * 获取消息路由信息映射表的只读视图
     *
     * @return 不可修改的消息路由信息映射表
     */
    public Map<String, MessageInfo> getMessageInfoMap() {
        return Collections.unmodifiableMap(messageInfoMap);
    }

    /**
     * 克隆消息路由信息映射表
     *
     * @param map 需要克隆的映射表
     * @return 克隆后的新映射表
     */
    public Map<String, MessageInfo> cloneMessageInfoMap(Map<String, MessageInfo> map) {
        Map<String, MessageInfo> cloneMap = new HashMap<>();

        // 遍历原映射表并复制每个条目
        for (Map.Entry<String, MessageInfo> entry : map.entrySet()) {
            cloneMap.put(entry.getKey(), entry.getValue());
        }

        return cloneMap;
    }

    /**
     * 获取单例实例
     *
     * @return MessageRouter单例实例
     */
    public static MessageRouter getInstance() {
        return instance;
    }

    /**
     * 设置新的单例实例
     *
     * @param newInstance 新的消息路由器实例
     */
    public static void setInstance(MessageRouter newInstance) {
        instance = newInstance;
    }

    /**
     * 更新路由信息
     *
     * 通过创建新的实例来替换当前实例，实现缓存刷新
     *
     * @param routeKey 路由键
     * @param deviceCode 设备代码
     * @param messageUrl 消息URL
     */
    public void updateRoute(String routeKey, String deviceCode, String messageUrl) {

        // 模拟更新数据库数据
        this.updateMessageInfoFromDb(routeKey, deviceCode, messageUrl);

        // 以替换整个MessageRouter实例的方法刷新缓存
        MessageRouter.setInstance(new MessageRouter());
    }

    /**
     * 模拟从数据库更新消息信息
     *
     * @param routeKey 路由键
     * @param deviceCode 设备代码
     * @param messageUrl 消息URL
     */
    private void updateMessageInfoFromDb(String routeKey, String deviceCode, String messageUrl) {
        // 模拟更新数据库数据
    }
}
