package com.coderlee.designpattern.behavioral.proxy.dynamic.jdk;

import java.lang.reflect.Proxy;

/**
 * JDK 动态代理工厂类
 * <p>
 * 用于创建 JDK 动态代理对象
 * 通过 Java 反射机制生成实现了目标对象接口的代理实例
 * </p>
 *
 * @author coderlee
 */
public class JDKProxyFactory {
    /** 目标对象（被代理的对象） */
    private final Object target;
    
    /**
     * 构造方法，注入目标对象
     *
     * @param target 目标对象
     */
    public JDKProxyFactory(Object target) {
        this.target = target;
    }
    
    /**
     * 获取代理对象实例
     * <p>
     * 使用 Proxy.newProxyInstance() 方法创建动态代理对象
     * </p>
     *
     * @return 代理对象实例
     */
    public Object getProxyInstance() {
        // 创建代理对象
        return Proxy.newProxyInstance(
                // 目标对象的类加载器
                target.getClass().getClassLoader(),
                // 目标对象实现的接口数组
                target.getClass().getInterfaces(),
                // 方法调用处理器（拦截器）
                new CalcTimeHandler(target)
        );
    }
}
