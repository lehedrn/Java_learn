package com.coderlee.designpattern.behavioral.observer.eventbus;

/**
 * 具体事件监听器：邮件发送监听器
 * <p>
 * 用户注册后发送欢迎邮件
 * </p>
 *
 * @author coderlee
 */
public class EmailSendListener implements EventListener {

    @Override
    public void onEvent(Event event) {
        if (event instanceof UserRegisterEvent) {
            UserRegisterEvent registerEvent = (UserRegisterEvent) event;
            System.out.println("📧 [邮件服务] 发送欢迎邮件");
            System.out.println("   收件人：" + registerEvent.getEmail());
            System.out.println("   主题：欢迎 " + registerEvent.getUsername() + " 加入我们！");
            // 模拟发送邮件
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("   ✅ 邮件发送完成\n");
        }
    }
}
