package com.coderlee.designpattern.behavioral.observer.eventbus;

/**
 * 事件监听器接口
 * <p>
 * 所有事件监听器都实现此接口
 * </p>
 *
 * @author coderlee
 */
public interface EventListener {
    /**
     * 处理事件
     *
     * @param event 事件对象
     */
    void onEvent(Event event);
}
