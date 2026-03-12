package com.coderlee.designpattern.behavioral.observer.async;

/**
 * 具体观察者：短信通知服务
 * <p>
 * 发送短信通知用户订单状态变更
 * </p>
 *
 * @author coderlee
 */
public class SmsNotificationService implements Observer {

    @Override
    public void update(String orderId, String status, String message) {
        System.out.println("📱 [短信服务] 订单 " + orderId + " - " + message);
        // 模拟发送短信耗时
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("   ✅ 短信发送成功");
    }

    @Override
    public String getName() {
        return "短信通知服务";
    }
}
