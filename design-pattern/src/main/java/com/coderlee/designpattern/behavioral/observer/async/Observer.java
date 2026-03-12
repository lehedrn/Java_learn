package com.coderlee.designpattern.behavioral.observer.async;

/**
 * 观察者接口（通知服务）
 * <p>
 * 定义观察者的更新方法，用于接收订单状态变更通知
 * </p>
 *
 * @author coderlee
 */
public interface Observer {
    /**
     * 更新方法
     * <p>
     * 当订单状态改变时，调用此方法通知观察者
     * </p>
     *
     * @param orderId 订单 ID
     * @param status 订单状态
     * @param message 通知消息
     */
    void update(String orderId, String status, String message);

    /**
     * 获取观察者名称
     *
     * @return 名称
     */
    String getName();
}
