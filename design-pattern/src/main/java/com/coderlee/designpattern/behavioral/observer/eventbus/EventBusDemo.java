package com.coderlee.designpattern.behavioral.observer.eventbus;

/**
 * 事件总线演示 - 用户注册事件处理
 * <p>
 * 演示场景：
 * 1. 用户注册成功
 * 2. 事件总线通知所有订阅者
 * 3. 邮件服务发送欢迎邮件
 * 4. 数据服务初始化用户数据
 * 5. 日志服务记录操作日志
 * 6. 优惠券服务发放新手优惠券
 * </p>
 *
 * @author coderlee
 */
public class EventBusDemo {

    public static void main(String[] args) {
        System.out.println("========== 观察者模式 - 事件总线演示 ==========");
        System.out.println("========== 用户注册事件处理 ==========\n");

        // 创建事件总线
        EventBus eventBus = new EventBus();

        // 创建事件监听器
        EventListener emailListener = new EmailSendListener();
        EventListener dataInitListener = new DataInitListener();
        EventListener logListener = new LogListener();
        EventListener couponListener = new CouponListener();

        // 订阅用户注册事件
        eventBus.subscribe(UserRegisterEvent.class, emailListener);
        eventBus.subscribe(UserRegisterEvent.class, dataInitListener);
        eventBus.subscribe(UserRegisterEvent.class, logListener);
        eventBus.subscribe(UserRegisterEvent.class, couponListener);

        System.out.println("\n当前订阅者数量：" + eventBus.getListenerCount(UserRegisterEvent.class));

        // 创建用户注册服务
        UserRegisterService registerService = new UserRegisterService(eventBus);

        // 用户注册
        registerService.register("张三", "zhangsan@example.com");

        // 第二个用户注册
        registerService.register("李四", "lisi@example.com");

        System.out.println("========== 演示结束 ==========");
    }
}
