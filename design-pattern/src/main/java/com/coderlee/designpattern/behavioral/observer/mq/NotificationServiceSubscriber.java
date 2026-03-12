package com.coderlee.designpattern.behavioral.observer.mq;

/**
 * 消息订阅者：通知服务
 * <p>
 * 订阅支付成功消息，发送支付成功通知
 * </p>
 *
 * @author coderlee
 */
public class NotificationServiceSubscriber implements MessageSubscriber {

    @Override
    public void onMessage(Message message) {
        if (message instanceof PaymentSuccessMessage) {
            PaymentSuccessMessage paymentMsg = (PaymentSuccessMessage) message;
            System.out.println("📱 [通知服务] 发送支付成功通知");
            System.out.println("   订单 ID: " + paymentMsg.getOrderId());
            System.out.println("   通知方式：短信 + APP 推送");
            System.out.println("   内容：" + paymentMsg.getContent());
            // 模拟发送通知
            try {
                Thread.sleep(90);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("   ✅ 通知发送完成\n");
        }
    }

    @Override
    public String getName() {
        return "通知服务";
    }

    @Override
    public String getSubscribedTopic() {
        return "PAYMENT_SUCCESS";
    }
}
