package com.coderlee.designpattern.behavioral.observer.eventbus;

/**
 * 具体事件监听器：数据初始化监听器
 * <p>
 * 用户注册后初始化用户数据（创建默认配置、初始化统计信息等）
 * </p>
 *
 * @author coderlee
 */
public class DataInitListener implements EventListener {

    @Override
    public void onEvent(Event event) {
        if (event instanceof UserRegisterEvent) {
            UserRegisterEvent registerEvent = (UserRegisterEvent) event;
            System.out.println("🔧 [数据服务] 初始化用户数据");
            System.out.println("   用户 ID: " + registerEvent.getUserId());
            System.out.println("   创建默认配置...");
            System.out.println("   初始化统计信息...");
            // 模拟初始化数据
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("   ✅ 数据初始化完成\n");
        }
    }
}
