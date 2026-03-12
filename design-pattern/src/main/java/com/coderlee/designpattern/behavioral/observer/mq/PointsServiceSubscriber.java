package com.coderlee.designpattern.behavioral.observer.mq;

/**
 * 消息订阅者：积分服务
 * <p>
 * 订阅支付成功消息，给用户增加积分
 * </p>
 *
 * @author coderlee
 */
public class PointsServiceSubscriber implements MessageSubscriber {

    @Override
    public void onMessage(Message message) {
        if (message instanceof PaymentSuccessMessage) {
            PaymentSuccessMessage paymentMsg = (PaymentSuccessMessage) message;
            System.out.println("🎁 [积分服务] 增加用户积分");
            System.out.println("   用户 ID: " + paymentMsg.getUserId());
            System.out.println("   支付金额：￥" + paymentMsg.getAmount());
            // 假设 1 元=10 积分
            int points = (int) (paymentMsg.getAmount() * 10);
            System.out.println("   获得积分：" + points);
            // 模拟增加积分
            try {
                Thread.sleep(80);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("   ✅ 积分增加完成\n");
        }
    }

    @Override
    public String getName() {
        return "积分服务";
    }

    @Override
    public String getSubscribedTopic() {
        return "PAYMENT_SUCCESS";
    }
}
