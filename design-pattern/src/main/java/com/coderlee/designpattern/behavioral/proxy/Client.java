package com.coderlee.designpattern.behavioral.proxy;

import com.coderlee.designpattern.behavioral.proxy.dynamic.cglib.CglibProxyFactory;
import com.coderlee.designpattern.behavioral.proxy.dynamic.cglib.UserDao;
import com.coderlee.designpattern.behavioral.proxy.dynamic.jdk.JDKProxyFactory;
import com.coderlee.designpattern.behavioral.proxy.staticproxy.UserServiceProxy;
import lombok.extern.slf4j.Slf4j;

/**
 * 代理模式客户端测试类
 * <p>
 * 演示三种代理方式的使用：
 * 1. 静态代理
 * 2. JDK 动态代理
 * 3. Cglib 动态代理
 * </p>
 *
 * @author coderlee
 */
@Slf4j
public class Client {
    public static void main(String[] args) {
        staticProxyDemo();
        JDKProxyDemo();
        cglibProxyDemo();
    }

    /**
     * Cglib 动态代理演示
     * <p>
     * Cglib 通过继承目标类的方式创建代理对象，适用于没有实现接口的类
     * </p>
     */
    private static void cglibProxyDemo() {
        log.info("=================动态代理方式二：Cglib====================");
        // 创建目标对象
        UserDao ud = new UserDao();
        // 创建 Cglib 代理工厂
        CglibProxyFactory cglibProxyFactory = new CglibProxyFactory(ud);
        // 获取代理对象
        UserDao userDao = (UserDao) cglibProxyFactory.getProxyInstance();
        // 准备测试数据
        User user = new User();
        user.setId(0L);
        user.setName("coderlee");
        // 通过代理对象调用方法（会自动计算执行时间）
        userDao.save(user);
        userDao.getUserById(1L);
        log.info("===========================================================");
    }

    /**
     * JDK 动态代理演示
     * <p>
     * JDK 动态代理基于反射机制，要求目标对象必须实现接口
     * 代理对象和目标对象实现相同的接口
     * </p>
     */
    private static void JDKProxyDemo() {
        log.info("==================动态代理方式一：JDK====================");
        // 创建目标对象
        UserService us = new UserServiceImpl();
        // 创建 JDK 代理工厂
        JDKProxyFactory jdkProxyFactory = new JDKProxyFactory(us);
        // 获取代理对象
        UserService userService = (UserService) jdkProxyFactory.getProxyInstance();
        // 准备测试数据
        User user = new User();
        user.setId(0L);
        user.setName("coderlee");
        // 通过代理对象调用方法（会自动计算执行时间）
        userService.saveUser(user);
        userService.getUserById(1L);
        log.info("===========================================================");
    }

    /**
     * 静态代理演示
     * <p>
     * 静态代理在编译时就确定了代理关系
     * 代理类需要实现与目标对象相同的接口
     * </p>
     */
    private static void staticProxyDemo() {
        log.info("=======================静态代理方式==========================");
        // 创建目标对象
        UserService us = new UserServiceImpl();
        // 创建静态代理对象，传入目标对象
        UserService userService = new UserServiceProxy(us);
        // 准备测试数据
        User user = new User();
        user.setId(0L);
        user.setName("coderlee");
        // 通过代理对象调用方法（会自动记录执行时间）
        userService.saveUser(user);
        userService.getUserById(1L);
        log.info("===========================================================");
    }
}
