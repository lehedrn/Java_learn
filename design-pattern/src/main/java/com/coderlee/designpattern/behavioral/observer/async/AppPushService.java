package com.coderlee.designpattern.behavioral.observer.async;

/**
 * 具体观察者：APP 推送服务
 * <p>
 * 发送 APP 推送通知用户订单状态变更
 * </p>
 *
 * @author coderlee
 */
public class AppPushService implements Observer {

    @Override
    public void update(String orderId, String status, String message) {
        System.out.println("📲 [APP 推送] 订单 " + orderId + " - " + message);
        // 模拟 APP 推送耗时
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("   ✅ APP 推送成功");
    }

    @Override
    public String getName() {
        return "APP 推送服务";
    }
}
