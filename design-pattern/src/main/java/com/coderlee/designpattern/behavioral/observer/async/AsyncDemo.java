package com.coderlee.designpattern.behavioral.observer.async;

/**
 * 异步非阻塞观察者模式演示 - 电商订单通知
 * <p>
 * 演示场景：
 * 1. 订单状态变更
 * 2. 异步通知多个服务（短信、邮件、APP 推送、物流同步）
 * 3. 并行执行，不阻塞主线程
 * 4. 统计总耗时
 * </p>
 *
 * @author coderlee
 */
public class AsyncDemo {

    public static void main(String[] args) {
        System.out.println("========== 观察者模式 - 异步非阻塞演示 ==========");
        System.out.println("========== 电商订单状态通知 ==========\n");

        // 创建订单服务
        OrderService orderService = new OrderService();

        // 创建观察者（各个通知服务）
        Observer smsService = new SmsNotificationService();
        Observer emailService = new EmailNotificationService();
        Observer appPushService = new AppPushService();
        Observer logisticsService = new LogisticsSyncService();

        // 订阅订单通知
        orderService.attach(smsService);
        orderService.attach(emailService);
        orderService.attach(appPushService);
        orderService.attach(logisticsService);

        System.out.println("\n当前订阅服务数量：" + orderService.getObserverCount());

        // 订单创建
        orderService.updateOrderStatus("ORDER-20260312-001", "CREATED", "订单已创建，等待付款");

        // 订单支付
        orderService.updateOrderStatus("ORDER-20260312-001", "PAID", "已付款，准备发货");

        // 订单发货
        orderService.updateOrderStatus("ORDER-20260312-001", "SHIPPED", "已发货，物流途中");

        // 关闭线程池
        orderService.shutdown();

        System.out.println("========== 演示结束 ==========");
    }
}
