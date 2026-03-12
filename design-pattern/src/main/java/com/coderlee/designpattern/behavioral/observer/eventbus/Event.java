package com.coderlee.designpattern.behavioral.observer.eventbus;

/**
 * 事件基类
 * <p>
 * 所有事件都继承自此类，包含事件源和事件时间
 * </p>
 *
 * @author coderlee
 */
public abstract class Event {
    /**
     * 事件源（触发事件的对象）
     */
    private final Object source;

    /**
     * 事件发生的时间戳
     */
    private final long timestamp;

    public Event(Object source) {
        this.source = source;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 获取事件源
     *
     * @return 事件源
     */
    public Object getSource() {
        return source;
    }

    /**
     * 获取时间戳
     *
     * @return 时间戳
     */
    public long getTimestamp() {
        return timestamp;
    }
}
