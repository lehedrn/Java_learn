package com.coderlee.designpattern.behavioral.observer.mq;

/**
 * 消息队列演示 - 支付成功事件处理
 * <p>
 * 演示场景：
 * 1. 模拟微服务架构下的支付成功事件处理
 * 2. 支付服务发布支付成功消息到消息队列
 * 3. 订单服务、库存服务、积分服务、通知服务订阅消息
 * 4. 各服务独立处理，互不影响（解耦）
 * </p>
 * <p>
 * 模拟 RabbitMQ/Kafka 的发布订阅模式
 * </p>
 *
 * @author coderlee
 */
public class MQDemo {

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║     观察者模式 - 消息队列演示                     ║");
        System.out.println("║     微服务架构 - 支付成功事件处理                 ║");
        System.out.println("╚══════════════════════════════════════════════════╝\n");

        // 创建消息队列（模拟 RabbitMQ/Kafka）
        MessageQueue mq = new MessageQueue();

        // 创建订阅者（各个微服务）
        MessageSubscriber orderService = new OrderServiceSubscriber();
        MessageSubscriber inventoryService = new InventoryServiceSubscriber();
        MessageSubscriber pointsService = new PointsServiceSubscriber();
        MessageSubscriber notificationService = new NotificationServiceSubscriber();

        // 订阅 PAYMENT_SUCCESS Topic
        System.out.println("--- 服务订阅 Topic ---\n");
        mq.subscribe("PAYMENT_SUCCESS", orderService);
        mq.subscribe("PAYMENT_SUCCESS", inventoryService);
        mq.subscribe("PAYMENT_SUCCESS", pointsService);
        mq.subscribe("PAYMENT_SUCCESS", notificationService);

        System.out.println("\n当前 PAYMENT_SUCCESS Topic 的订阅者数量："
                + mq.getSubscriberCount("PAYMENT_SUCCESS"));

        // 模拟第一笔支付成功
        System.out.println("\n\n===== 第一笔支付成功 =====");
        PaymentSuccessMessage msg1 = new PaymentSuccessMessage(
                "ORDER-20260312-001",  // 订单 ID
                "USER-1001",            // 用户 ID
                299.00,                 // 支付金额
                "支付宝"                 // 支付方式
        );
        mq.publish("PAYMENT_SUCCESS", msg1);

        // 模拟第二笔支付成功
        System.out.println("\n\n===== 第二笔支付成功 =====");
        PaymentSuccessMessage msg2 = new PaymentSuccessMessage(
                "ORDER-20260312-002",
                "USER-1002",
                1599.00,
                "微信支付"
        );
        mq.publish("PAYMENT_SUCCESS", msg2);

        // 模拟第三笔支付成功（大额订单）
        System.out.println("\n\n===== 第三笔支付成功（大额订单） =====");
        PaymentSuccessMessage msg3 = new PaymentSuccessMessage(
                "ORDER-20260312-003",
                "USER-1003",
                9999.00,
                "银行卡"
        );
        mq.publish("PAYMENT_SUCCESS", msg3);

        System.out.println("\n══════════════════════════════════════════════════");
        System.out.println("总共处理消息数量：" + mq.getMessageHistoryCount());
        System.out.println("══════════════════════════════════════════════════");

        System.out.println("\n========== 演示结束 ==========");
    }
}
