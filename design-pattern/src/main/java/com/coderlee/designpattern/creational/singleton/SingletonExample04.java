package com.coderlee.designpattern.creational.singleton;

import com.coderlee.designpattern.creational.singleton.supports.SingletonTestUtil;

import java.util.concurrent.TimeUnit;

/**
 * 单例模式示例 04 - 懒汉式实现（同步方法版本）
 * <p>
 * 特点：
 * 1. 延迟加载：只有在第一次调用 getInstance() 时才创建实例
 * 2. 线程安全：通过 synchronized 关键字保证方法同步
 * 3. 性能较差：每次调用都需要同步，即使实例已经创建
 * </p>
 * <p>
 * 优缺点分析：
 * 优点：实现简单，线程安全
 * 缺点：同步锁粒度太大，影响性能，尤其是高并发场景
 * </p>
 *
 * @author coderlee
 * @version 1.0
 */
public class SingletonExample04 {
    /**
     * 私有构造函数，防止外部通过 new 关键字创建实例
     * 确保只能通过 getInstance() 方法获取实例
     */
    private SingletonExample04() {}

    /**
     * 静态变量存储单例实例
     * 初始为 null，首次调用 getInstance() 时创建实例
     * 注意：没有使用 volatile 关键字
     */
    private static SingletonExample04 INSTANCE = null;

    /**
     * 获取单例实例的方法（线程安全版本）
     * 使用 synchronized 关键字修饰整个方法，保证线程安全
     * 但同步范围过大，影响性能
     *
     * @return SingletonExample04 实例
     */
    public static synchronized SingletonExample04 getInstance() {
        // 判断实例是否已创建
        if (INSTANCE == null) {
            // 模拟实例创建过程中的耗时操作
            // 在实际应用中可能是复杂的初始化逻辑
            try {
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // 创建新实例
            INSTANCE = new SingletonExample04();
        }
        // 返回创建的实例
        return INSTANCE;
    }

    /**
     * 主方法，用于测试单例模式的正确性和线程安全性
     * 包括：验证单例性、测试线程安全、测试反射攻击
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 测试线程安全性：多线程环境下验证是否只有一个实例
        SingletonTestUtil.testSingletonThreadSafety(SingletonExample04.class.getSimpleName(), SingletonExample04::getInstance);
        // 测试单例性：验证两次获取的是同一个实例
        SingletonTestUtil.testSingleton(SingletonExample04.class.getSimpleName(), SingletonExample04::getInstance);
        // 测试反射攻击：验证反射是否能破坏单例模式
        SingletonTestUtil.testReflection(SingletonExample04.class.getSimpleName(), SingletonExample04.class, SingletonExample04::getInstance);
    }
}

