package com.coderlee.designpattern.behavioral.observer.async;

/**
 * 具体观察者：邮件通知服务
 * <p>
 * 发送邮件通知用户订单状态变更
 * </p>
 *
 * @author coderlee
 */
public class EmailNotificationService implements Observer {

    @Override
    public void update(String orderId, String status, String message) {
        System.out.println("📧 [邮件服务] 订单 " + orderId + " - " + message);
        // 模拟发送邮件耗时
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("   ✅ 邮件发送成功");
    }

    @Override
    public String getName() {
        return "邮件通知服务";
    }
}
