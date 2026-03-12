package com.coderlee.designpattern.behavioral.observer.mq;

/**
 * 消息订阅者：库存服务
 * <p>
 * 订阅支付成功消息，扣减库存
 * </p>
 *
 * @author coderlee
 */
public class InventoryServiceSubscriber implements MessageSubscriber {

    @Override
    public void onMessage(Message message) {
        if (message instanceof PaymentSuccessMessage) {
            PaymentSuccessMessage paymentMsg = (PaymentSuccessMessage) message;
            System.out.println("📦 [库存服务] 扣减库存");
            System.out.println("   订单 ID: " + paymentMsg.getOrderId());
            System.out.println("   操作：扣减对应商品库存");
            // 模拟扣减库存
            try {
                Thread.sleep(120);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("   ✅ 库存扣减完成\n");
        }
    }

    @Override
    public String getName() {
        return "库存服务";
    }

    @Override
    public String getSubscribedTopic() {
        return "PAYMENT_SUCCESS";
    }
}
