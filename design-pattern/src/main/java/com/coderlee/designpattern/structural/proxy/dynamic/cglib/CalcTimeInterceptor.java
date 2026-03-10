package com.coderlee.designpattern.structural.proxy.dynamic.cglib;

import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;

/**
 * 计算时间拦截器
 * <p>
 * 实现 Cglib 的 MethodInterceptor 接口，用于拦截目标对象的方法调用
 * 记录方法执行时间，是 Cglib 动态代理的核心组件
 * </p>
 * <p>
 * Cglib 动态代理的特点：
 * - 通过继承目标类的方式创建代理
 * - 可以代理没有实现接口的类
 * - 使用 MethodInterceptor 拦截方法调用
 * </p>
 *
 * @author coderlee
 */
@Slf4j
public class CalcTimeInterceptor implements MethodInterceptor {
    /**
     * 拦截并处理方法调用
     * <p>
     * 当调用代理对象的任何方法时，都会被路由到此方法
     * 可以在此添加额外的处理逻辑（如记录执行时间、事务控制等）
     * </p>
     *
     * @param obj         由 Cglib 生成的代理对象
     * @param method      被调用的方法对象
     * @param objects     方法调用参数
     * @param methodProxy Cglib 提供的方法代理对象，用于调用父类方法
     * @return 方法执行结果
     * @throws Throwable 可能抛出的异常
     */
    @Override
    public Object intercept(Object obj, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        // 记录方法开始执行的时间
        Instant start = Instant.now();
        log.info("开始执行方法：{}", method.getName());
        // 通过 MethodProxy 调用目标对象的方法（比直接反射性能更好）
        Object result = methodProxy.invokeSuper(obj, objects);
        // 记录方法执行完毕并计算耗时
        log.info("方法执行完毕，耗时：{}", Duration.between(start, Instant.now()).toMillis());
        return result;
    }
}
