package com.coderlee.designpattern.behavioral.proxy.dynamic.cglib;

import com.coderlee.designpattern.behavioral.proxy.User;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 用户数据访问对象
 * <p>
 * 用于演示 Cglib 动态代理的目标类
 * 该类没有实现接口，因此只能使用 Cglib 进行代理
 * </p>
 * <p>
 * 注意：final 类和方法不能被 Cglib 代理
 * </p>
 *
 * @author coderlee
 */
@Slf4j
public class UserDao {
    /**
     * 保存用户信息到数据库
     * <p>
     * 模拟耗时的数据库操作
     * </p>
     *
     * @param user 要保存的用户对象
     */
    public void save(User user) {
        // 模拟数据库写入操作的耗时
        try {
            TimeUnit.MILLISECONDS.sleep(30);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("save user: {}", user);
    }
    
    /**
     * 根据 ID 查询用户信息
     * <p>
     * 模拟耗时的数据库查询操作
     * </p>
     *
     * @param id 用户 ID
     * @return 查询到的用户对象
     */
    public User getUserById(Long id) {
        // 模拟数据库查询操作的耗时
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
