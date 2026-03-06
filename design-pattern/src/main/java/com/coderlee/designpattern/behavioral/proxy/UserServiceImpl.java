package com.coderlee.designpattern.behavioral.proxy;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 用户服务实现类
 * <p>
 * 实现 UserService 接口，提供具体的业务逻辑
 * 该类是被代理的目标类（真实主题）
 * </p>
 *
 * @author coderlee
 */
@Slf4j
public class UserServiceImpl implements UserService {
    @Override
    public void saveUser(User user) {
        // 模拟数据库操作的耗时
        try {
            TimeUnit.MILLISECONDS.sleep(30);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("save user: {}", user);
    }

    @Override
    public User getUserById(Long id) {
        // 模拟数据库查询的耗时
        try {
            TimeUnit.MILLISECONDS.sleep(5);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // 创建并返回模拟的用户数据
        User user = new User();
        user.setId(id);
        user.setName(String.format("user-%d", id));
        log.info("get user: {}", user);
        return user;
    }
}
