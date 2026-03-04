package com.coderlee.designpattern.creational.singleton;

import com.coderlee.designpattern.creational.singleton.supports.SingletonTestUtil;

/**
 * 单例模式示例 07 - 静态内部类实现（懒汉式）
 * <p>
 * 特点：
 * 1. 延迟加载：利用 JVM 类加载机制保证线程安全
 * 2. 线程安全：JVM 保证类加载的线程安全性
 * 3. 高性能：无需同步，性能优秀
 * </p>
 * <p>
 * 实现原理：
 * 1. 外部类加载时不会加载内部类，不会创建实例
 * 2. 只有调用 getInstance() 时才会加载内部类并创建实例
 * 3. JVM 的类加载机制保证了线程安全
 * </p>
 * <p>
 * 优点：
 * 1. 线程安全且性能优秀
 * 2. 实现简洁，易于理解
 * 3. 推荐的生产环境单例实现方式
 * </p>
 *
 * @author coderlee
 * @version 1.0
 */
public class SingletonExample07 {
    /**
     * 私有构造函数，防止外部通过 new 关键字创建实例
     * 确保只能通过 getInstance() 方法获取实例
     */
    private SingletonExample07() {}

    /**
     * 静态内部类（持有者模式）
     * 特点：
     * 1. 只有在被首次使用时才会被加载（调用 getInstance() 方法时）
     * 2. JVM 保证类加载过程的线程安全性
     * 3. 不需要使用 synchronized 或 volatile 关键字
     *
     * 类加载时机：
     * - 外部类 SingletonExample07 被加载时，内部类不会被加载
     * - 只有访问 SingletonExample07Holder.INSTANCE 时，内部类才会被加载
     */
    private static class SingletonExample07Holoder {
        /**
         * 静态常量实例，在内部类加载时创建
         * 利用 JVM 类加载机制保证线程安全
         * 这是懒加载和线程安全的完美结合
         */
        private static SingletonExample07 INSTANCE = new SingletonExample07();
    }

    /**
     * 获取单例实例的方法（静态内部类版本）
     * 通过内部类的类加载机制保证线程安全和延迟加载
     *
     * @return SingletonExample07 实例
     */
    public static SingletonExample07 getInstance() {
        // 返回内部类中持有的实例
        // 第一次调用此方法时会触发内部类的加载和实例的创建
        return SingletonExample07Holoder.INSTANCE;
    }

    /**
     * 主方法，用于测试单例模式的正确性和线程安全性
     * 包括：验证单例性、测试线程安全、测试反射攻击
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 测试线程安全性：多线程环境下验证是否只有一个实例
        SingletonTestUtil.testSingletonThreadSafety(SingletonExample07.class.getSimpleName(), SingletonExample07::getInstance);
        // 测试单例性：验证两次获取的是同一个实例
        SingletonTestUtil.testSingleton(SingletonExample07.class.getSimpleName(), SingletonExample07::getInstance);
        // 测试反射攻击：验证反射是否能破坏单例模式
        SingletonTestUtil.testReflection(SingletonExample07.class.getSimpleName(), SingletonExample07.class, SingletonExample07::getInstance);
    }
}

