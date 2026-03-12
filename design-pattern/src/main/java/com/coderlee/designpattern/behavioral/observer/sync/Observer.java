package com.coderlee.designpattern.behavioral.observer.sync;

/**
 * 观察者接口（粉丝）
 * <p>
 * 定义了观察者的更新方法，当被观察者状态改变时调用
 * </p>
 *
 * @author coderlee
 */
public interface Observer {
    /**
     * 更新方法
     * <p>
     * 当被观察者状态改变时，调用此方法通知观察者
     * </p>
     *
     * @param message 通知消息
     */
    void update(String message);

    /**
     * 获取观察者名称
     *
     * @return 名称
     */
    String getName();
}
