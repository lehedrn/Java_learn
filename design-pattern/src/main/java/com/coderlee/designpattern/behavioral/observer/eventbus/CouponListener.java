package com.coderlee.designpattern.behavioral.observer.eventbus;

/**
 * 具体事件监听器：优惠券监听器
 * <p>
 * 用户注册后发送新手优惠券
 * </p>
 *
 * @author coderlee
 */
public class CouponListener implements EventListener {

    @Override
    public void onEvent(Event event) {
        if (event instanceof UserRegisterEvent) {
            UserRegisterEvent registerEvent = (UserRegisterEvent) event;
            System.out.println("🎁 [优惠券服务] 发送新手优惠券");
            System.out.println("   用户名：" + registerEvent.getUsername());
            System.out.println("   优惠券：满 100 减 20、满 200 减 50、包邮券");
            // 模拟发送优惠券
            try {
                Thread.sleep(80);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("   ✅ 优惠券发放完成\n");
        }
    }
}
