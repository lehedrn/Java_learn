package com.coderlee.designpattern.creational.singleton;

import com.coderlee.designpattern.creational.singleton.supports.SingletonTestUtil;

import java.util.concurrent.TimeUnit;

/**
 * 单例模式示例 05 - 懒汉式实现（同步代码块版本）
 * <p>
 * 实现方式：使用 synchronized 同步代码块 + 双重检查
 * </p>
 * <p>
 * 特点分析：
 * 1. 延迟加载：只有在第一次调用 getInstance() 时才创建实例
 * 2. 线程安全：通过 synchronized 同步代码块保证
 * 3. 性能优化：相比同步方法，缩小了锁的范围
 * 4. 存在问题：缺少 volatile 关键字，可能存在指令重排序问题
 * </p>
 * <p>
 * 与 Example04 的区别：
 * - Example04 使用同步方法，锁的粒度是整个方法
 * - Example05 使用同步代码块，锁的粒度更小，性能更优
 * </p>
 * <p>
 * 与 Example06 的区别：
 * - Example05 没有使用 volatile 关键字
 * - Example06 添加了 volatile，完全解决了线程安全问题
 * </p>
 *
 * @author coderlee
 * @version 1.0
 */
public class SingletonExample05 {
    /**
     * 私有构造函数，防止外部通过 new 关键字创建实例
     */
    private SingletonExample05() {}

    /**
     * 静态变量存储单例实例
     * 初始为 null，首次调用 getInstance() 时创建实例
     * 注意：未使用 volatile 修饰，可能存在指令重排序风险
     */
    private static SingletonExample05 INSTANCE = null;

    /**
     * 获取单例实例的方法（同步代码块版本）
     * 使用 synchronized 代码块 + 双重检查锁定（DCL）
     *
     * <p>执行流程：</p>
     * <ol>
     *   <li>第一次检查 INSTANCE 是否为 null（减少同步开销）</li>
     *   <li>进入 synchronized 同步代码块</li>
     *   <li>第二次检查 INSTANCE 是否为 null（确保线程安全，但是实际情况，因为存在指令重排的问题，所以还是线程不安全的）</li>
     *   <li>创建实例并赋值给 INSTANCE</li>
     * </ol>
     *
     * @return SingletonExample05 实例
     */
    public static SingletonExample05 getInstance() {
        // 第一次检查：判断实例是否已创建，避免不必要的同步
        if (INSTANCE == null) {
            try {
                TimeUnit.MILLISECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            // 同步代码块：锁定当前类的 Class 对象
            // 确保同一时间只有一个线程能进入代码块创建实例
            synchronized (SingletonExample05.class) {
                try {
                    TimeUnit.MILLISECONDS.sleep(3);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                // 第二次检查：在同步块内再次确认实例是否已创建
                // 防止多个线程同时通过第一次检查后重复创建实例
                if (INSTANCE == null) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(3);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    // 创建新实例
                    // 潜在问题：没有 volatile 修饰，可能发生指令重排序
                    // INSTANCE = new SingletonExample05() 可能重排序为：
                    // 1. 分配内存空间 2. 将 INSTANCE 指向分配的内存地址 3. 初始化对象
                    // 这会导致其他线程可能获取到未完全初始化的实例
                    INSTANCE = new SingletonExample05();
                }
            }
        }

        // 返回创建的实例
        return INSTANCE;
    }

    /**
     * 主方法，用于测试单例模式的正确性和线程安全性
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 测试线程安全性：多线程环境下验证是否只有一个实例
        SingletonTestUtil.testSingletonThreadSafety(SingletonExample05.class.getSimpleName(), SingletonExample05::getInstance);

        // 测试单例性：验证两次获取的是同一个实例
        SingletonTestUtil.testSingleton(SingletonExample05.class.getSimpleName(), SingletonExample05::getInstance);

        // 测试反射攻击：验证反射是否能破坏单例模式
        SingletonTestUtil.testReflection(SingletonExample05.class.getSimpleName(), SingletonExample05.class, SingletonExample05::getInstance);
    }
}
