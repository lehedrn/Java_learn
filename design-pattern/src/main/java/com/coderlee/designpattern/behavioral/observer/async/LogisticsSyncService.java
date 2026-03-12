package com.coderlee.designpattern.behavioral.observer.async;

/**
 * 具体观察者：物流同步服务
 * <p>
 * 同步订单信息到物流系统
 * </p>
 *
 * @author coderlee
 */
public class LogisticsSyncService implements Observer {

    @Override
    public void update(String orderId, String status, String message) {
        System.out.println("🚚 [物流同步] 订单 " + orderId + " - " + message);
        // 模拟同步物流信息耗时
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("   ✅ 物流信息同步成功");
    }

    @Override
    public String getName() {
        return "物流同步服务";
    }
}
