package com.coderlee.designpattern.behavioral.observer.eventbus;

/**
 * 具体事件监听器：操作日志监听器
 * <p>
 * 记录用户注册的操作日志
 * </p>
 *
 * @author coderlee
 */
public class LogListener implements EventListener {

    @Override
    public void onEvent(Event event) {
        if (event instanceof UserRegisterEvent) {
            UserRegisterEvent registerEvent = (UserRegisterEvent) event;
            System.out.println("📝 [日志服务] 记录操作日志");
            System.out.println("   事件类型：用户注册");
            System.out.println("   用户 ID: " + registerEvent.getUserId());
            System.out.println("   用户名：" + registerEvent.getUsername());
            System.out.println("   时间戳：" + registerEvent.getTimestamp());
            // 模拟记录日志
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("   ✅ 日志记录完成\n");
        }
    }
}
