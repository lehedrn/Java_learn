package com.coderlee.designpattern.behavioral.observer.eventbus;

/**
 * 用户注册服务
 * <p>
 * 处理用户注册业务，注册成功后发布事件
 * </p>
 *
 * @author coderlee
 */
public class UserRegisterService {

    /**
     * 事件总线
     */
    private final EventBus eventBus;

    public UserRegisterService(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    /**
     * 用户注册
     *
     * @param username 用户名
     * @param email 邮箱
     * @return 用户 ID
     */
    public String register(String username, String email) {
        System.out.println("\n===== 用户注册 =====");
        System.out.println("用户名：" + username);
        System.out.println("邮箱：" + email);

        // 模拟创建用户
        String userId = "USER-" + System.currentTimeMillis();
        System.out.println("用户 ID: " + userId);
        System.out.println("✅ 用户创建成功");

        // 发布用户注册事件
        UserRegisterEvent event = new UserRegisterEvent(this, userId, username, email);
        eventBus.publish(event);

        return userId;
    }
}
