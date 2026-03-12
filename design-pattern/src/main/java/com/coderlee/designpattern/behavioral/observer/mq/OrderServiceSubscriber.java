package com.coderlee.designpattern.behavioral.observer.mq;

/**
 * 消息订阅者：订单服务
 * <p>
 * 订阅支付成功消息，更新订单状态
 * </p>
 *
 * @author coderlee
 */
public class OrderServiceSubscriber implements MessageSubscriber {

    @Override
    public void onMessage(Message message) {
        if (message instanceof PaymentSuccessMessage) {
            PaymentSuccessMessage paymentMsg = (PaymentSuccessMessage) message;
            System.out.println("📦 [订单服务] 更新订单状态");
            System.out.println("   订单 ID: " + paymentMsg.getOrderId());
            System.out.println("   操作：将订单状态更新为【已付款】");
            // 模拟更新订单
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("   ✅ 订单状态更新完成\n");
        }
    }

    @Override
    public String getName() {
        return "订单服务";
    }

    @Override
    public String getSubscribedTopic() {
        return "PAYMENT_SUCCESS";
    }
}
