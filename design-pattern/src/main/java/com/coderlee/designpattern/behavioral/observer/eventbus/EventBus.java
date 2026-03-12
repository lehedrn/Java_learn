package com.coderlee.designpattern.behavioral.observer.eventbus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 事件总线
 * <p>
 * 核心类，实现事件的订阅和发布功能
 * 支持按事件类型订阅和发布
 * </p>
 *
 * @author coderlee
 */
public class EventBus {

    /**
     * 存储事件类型与监听器列表的映射关系
     * 使用 ConcurrentHashMap 保证线程安全
     */
    private final Map<Class<? extends Event>, List<EventListener>> listeners;

    public EventBus() {
        this.listeners = new ConcurrentHashMap<>();
    }

    /**
     * 订阅事件
     * <p>
     * 监听器订阅指定类型的事件
     * </p>
     *
     * @param eventClass 事件类型
     * @param listener 监听器
     */
    public void subscribe(Class<? extends Event> eventClass, EventListener listener) {
        // 如果该事件类型没有监听器，创建新的列表
        listeners.computeIfAbsent(eventClass, k -> new CopyOnWriteArrayList<>());
        listeners.get(eventClass).add(listener);
        System.out.println("✅ " + listener.getClass().getSimpleName() + " 订阅了 " + eventClass.getSimpleName());
    }

    /**
     * 取消订阅
     * <p>
     * 监听器取消订阅指定类型的事件
     * </p>
     *
     * @param eventClass 事件类型
     * @param listener 监听器
     */
    public void unsubscribe(Class<? extends Event> eventClass, EventListener listener) {
        List<EventListener> listenerList = listeners.get(eventClass);
        if (listenerList != null) {
            listenerList.remove(listener);
            System.out.println("❌ " + listener.getClass().getSimpleName() + " 取消订阅 " + eventClass.getSimpleName());
        }
    }

    /**
     * 发布事件
     * <p>
     * 发布事件，通知所有订阅该事件的监听器
     * </p>
     *
     * @param event 事件对象
     */
    public void publish(Event event) {
        Class<? extends Event> eventClass = event.getClass();
        List<EventListener> listenerList = listeners.get(eventClass);

        if (listenerList != null && !listenerList.isEmpty()) {
            System.out.println("\n📢 发布事件：" + eventClass.getSimpleName());
            System.out.println("   事件详情：" + event);
            System.out.println("   通知 " + listenerList.size() + " 个监听器\n");

            // 通知所有监听器
            for (EventListener listener : listenerList) {
                listener.onEvent(event);
            }
        }
    }

    /**
     * 获取指定事件类型的监听器数量
     *
     * @param eventClass 事件类型
     * @return 监听器数量
     */
    public int getListenerCount(Class<? extends Event> eventClass) {
        List<EventListener> listenerList = listeners.get(eventClass);
        return listenerList != null ? listenerList.size() : 0;
    }
}
