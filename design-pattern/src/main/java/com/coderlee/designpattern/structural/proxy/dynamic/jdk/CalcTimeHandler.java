package com.coderlee.designpattern.structural.proxy.dynamic.jdk;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;

/**
 * 计算时间处理器
 * <p>
 * 实现 JDK 的 InvocationHandler 接口，作为动态代理的回调处理器
 * 用于拦截目标对象的方法调用，并计算方法执行时间
 * </p>
 * <p>
 * JDK 动态代理的核心：
 * - 基于反射机制实现
 * - 只能代理实现了接口的类
 * - 通过 InvocationHandler 拦截方法调用
 * </p>
 *
 * @author coderlee
 */
@Slf4j
public class CalcTimeHandler implements InvocationHandler {
    /** 目标对象（被代理的对象） */
    private final Object target;

    /**
     * 构造方法，注入目标对象
     *
     * @param target 目标对象
     */
    public CalcTimeHandler(Object target) {
        this.target = target;
    }
    
    /**
     * 拦截并处理方法调用
     * <p>
     * 当调用代理对象的任何方法时，都会被路由到此方法
     * 在此方法中可以添加额外的处理逻辑（如记录执行时间）
     * </p>
     *
     * @param proxy  代理对象本身
     * @param method 被调用的方法对象
     * @param args   方法调用参数
     * @return 方法执行结果
     * @throws Throwable 可能抛出的异常
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 记录方法开始执行的时间
        Instant start = Instant.now();
        log.info("开始执行方法：{}", method.getName());
        // 通过反射调用目标对象的方法
        Object result = method.invoke(target, args);
        // 记录方法执行完毕并计算耗时
        log.info("方法执行完毕，耗时：{}", Duration.between(start, Instant.now()).toMillis());
        return result;
    }
}
