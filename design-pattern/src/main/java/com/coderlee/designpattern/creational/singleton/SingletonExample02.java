package com.coderlee.designpattern.creational.singleton;

import com.coderlee.designpattern.creational.singleton.supports.SingletonTestUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 单例模式示例 02 - 静态代码块实现（饿汉式变体）
 * <p>
 * 特点：
 * 1. 利用 JVM 类加载机制保证线程安全
 * 2. 在静态代码块中完成实例创建，可以处理异常
 * 3. 与示例 01 类似，都是类加载时初始化，属于饿汉式
 * </p>
 * 
 * @author coderlee
 * @version 1.0
 */
@Slf4j
public class SingletonExample02 {
    /**
     * 私有构造函数，防止外部通过 new 关键字创建实例
     * 这是单例模式的核心要素之一
     */
    private SingletonExample02() {}

    /**
     * 静态变量声明，初始值为 null
     * 实际实例化在静态代码块中完成
     * 这种方式可以在初始化时处理异常
     */
    private static SingletonExample02 INSTANCE = null;

    /**
     * 静态代码块，在类加载时执行一次
     * JVM 保证静态代码块的线程安全性
     * 只会被执行一次，确保实例唯一性
     */
    static {
        // 在类加载时创建唯一实例
        INSTANCE = new SingletonExample02();
    }

    /**
     * 获取单例实例的全局访问点
     * 提供对外访问的唯一入口，返回预先创建的实例
     *
     * @return 唯一的 SingletonExample02 实例
     */
    public static SingletonExample02 getInstance() {
        return INSTANCE;
    }

    /**
     * 主方法，用于测试单例模式的正确性
     * 包括：验证单例性、测试反射攻击
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // ========== 测试一：验证单例模式的正确性 ==========
        SingletonTestUtil.testSingleton(SingletonExample02.class.getSimpleName(), SingletonExample02::getInstance);

        // ========== 测试二：使用反射破坏单例模式 ==========
        SingletonTestUtil.testReflection(SingletonExample02.class.getSimpleName(), SingletonExample02.class, SingletonExample02::getInstance);
    }
}
