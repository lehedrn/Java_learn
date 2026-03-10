package com.coderlee.designpattern.structural.proxy.dynamic.cglib;

import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.Enhancer;

/**
 * Cglib 动态代理工厂类
 * <p>
 * 用于创建 Cglib 动态代理对象
 * 通过 Enhancer 增强器生成目标类的子类作为代理
 * </p>
 * <p>
 * 适用场景：
 * - 目标对象没有实现接口
 * - 需要代理类的成员变量和方法
 * - AOP 框架（如 Spring AOP）的底层实现
 * </p>
 *
 * @author coderlee
 */
@Slf4j
public class CglibProxyFactory {
    /** 目标对象（被代理的对象） */
    private final Object target;
    
    /**
     * 构造方法，注入目标对象
     *
     * @param target 目标对象
     */
    public CglibProxyFactory(Object target) {
        this.target = target;
    }
    
    /**
     * 获取代理对象实例
     * <p>
     * 使用 Cglib 的 Enhancer 类创建动态代理对象
     * </p>
     *
     * @return 代理对象实例
     */
    public Object getProxyInstance() {
        // 创建 Enhancer 增强器
        Enhancer enhancer = new Enhancer();
        // 设置代理类的父类（目标对象的类）
        enhancer.setSuperclass(target.getClass());
        // 设置回调方法（拦截器）
        enhancer.setCallback(new CalcTimeInterceptor());
        // 创建并返回代理对象
        return enhancer.create();
    }
}
