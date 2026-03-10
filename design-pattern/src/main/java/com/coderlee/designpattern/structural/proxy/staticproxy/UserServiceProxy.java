package com.coderlee.designpattern.structural.proxy.staticproxy;

import com.coderlee.designpattern.structural.proxy.User;
import com.coderlee.designpattern.structural.proxy.UserService;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

/**
 * 用户服务静态代理类
 * <p>
 * 实现与目标对象相同的接口，在调用真实业务逻辑前后可以添加额外的处理
 * 此处用于记录方法执行时间
 * </p>
 * <p>
 * 静态代理的特点：
 * - 代理类在编译期就已确定
 * - 一个代理类只能代理一个目标类
 * - 代理类和目标类需要实现相同的接口
 * </p>
 *
 * @author coderlee
 */
@Slf4j
public class UserServiceProxy implements UserService {
    /** 目标对象（被代理的对象） */
    private UserService userService;
    
    /**
     * 构造方法，注入目标对象
     *
     * @param userService 目标用户服务对象
     */
    public UserServiceProxy(UserService userService) {
        this.userService = userService;
    }
    
    @Override
    public void saveUser(User user) {
        // 记录方法开始执行的时间
        Instant start = Instant.now();
        log.info("开始保存用户信息");
        // 调用目标对象的方法
        userService.saveUser(user);
        // 记录方法执行结束的时间
        Instant end = Instant.now();
        // 计算方法执行耗时（毫秒）
        log.info("保存用户信息结束，耗时：{}", end.toEpochMilli() - start.toEpochMilli());
    }

    @Override
    public User getUserById(Long id) {
        // 记录方法开始执行的时间
        Instant start = Instant.now();
        log.info("开始查询用户信息");
        // 调用目标对象的方法
        User user = userService.getUserById(id);
        // 记录方法执行结束的时间
        Instant end = Instant.now();
        // 计算方法执行耗时（毫秒）
        log.info("查询用户信息结束，耗时：{}", end.toEpochMilli() - start.toEpochMilli());
        return user;
    }
}
