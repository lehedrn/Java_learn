package com.coderlee.designpattern.behavioral.observer.eventbus;

/**
 * 用户注册事件
 * <p>
 * 当用户注册成功时触发此事件
 * </p>
 *
 * @author coderlee
 */
public class UserRegisterEvent extends Event {

    /**
     * 用户 ID
     */
    private final String userId;

    /**
     * 用户名
     */
    private final String username;

    /**
     * 用户邮箱
     */
    private final String email;

    public UserRegisterEvent(Object source, String userId, String username, String email) {
        super(source);
        this.userId = userId;
        this.username = username;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "UserRegisterEvent{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
