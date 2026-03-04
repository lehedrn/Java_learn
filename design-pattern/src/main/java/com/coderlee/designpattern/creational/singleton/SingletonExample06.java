package com.coderlee.designpattern.creational.singleton;

import com.coderlee.designpattern.creational.singleton.supports.SingletonTestUtil;

import java.util.concurrent.TimeUnit;


/**
 * 单例模式示例 06 - 懒汉式实现（双重检查锁定 DCL 版本）
 * <p>
 * 特点：
 * 1. 延迟加载：只有在第一次调用 getInstance() 时才创建实例
 * 2. 线程安全：通过双重检查锁定机制保证线程安全
 * 3. 高性能：避免不必要的同步，只在首次创建时加锁
 * </p>
 * <p>
 * 关键要点：
 * 1. volatile 关键字必不可少，防止指令重排序
 * 2. 两次 null 检查各有作用：外层减少同步开销，内层保证线程安全
 * 3. 推荐的生产环境单例实现方式
 * </p>
 *
 * @author coderlee
 * @version 1.0
 */
public class SingletonExample06 {
    /**
     * 私有构造函数，防止外部通过 new 关键字创建实例
     * 确保只能通过 getInstance() 方法获取实例
     */
    private SingletonExample06() {}

    /**
     * 静态 volatile 变量存储单例实例
     * volatile 关键字的作用：
     * 1. 保证变量的可见性：一个线程修改了变量，其他线程立即可见
     * 2. 禁止指令重排序：防止 INSTANCE = new SingletonExample06() 的重排序问题
     *    （先分配内存 -> 再初始化对象 -> 最后将引用赋值给 INSTANCE）
     */
    private static volatile SingletonExample06 INSTANCE = null;

    /**
     * 获取单例实例的方法（双重检查锁定版本）
     * 结合了性能优势和线程安全保障
     *
     * @return SingletonExample06 实例
     */
    public static SingletonExample06 getInstance() {
        // 第一次检查：判断实例是否已创建（减少同步开销）
        if (INSTANCE == null) {
            // 模拟首次检查后的耗时操作
            try {
                TimeUnit.MILLISECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // 同步代码块，锁定当前类的 Class 对象
            synchronized (SingletonExample06.class) {
                // 模拟进入同步块后的耗时操作
                try {
                    TimeUnit.MILLISECONDS.sleep(3);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                // 第二次检查：在同步块内再次判断（保证线程安全）
                if (INSTANCE == null) {
                    // 模拟实例创建时的耗时操作
                    try {
                        TimeUnit.MILLISECONDS.sleep(3);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    // 创建新实例（volatile 保证这步不会发生指令重排序）
                    INSTANCE = new SingletonExample06();
                }
            }
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
        SingletonTestUtil.testSingletonThreadSafety(SingletonExample06.class.getSimpleName(), SingletonExample06::getInstance);
        // 测试单例性：验证两次获取的是同一个实例
        SingletonTestUtil.testSingleton(SingletonExample06.class.getSimpleName(), SingletonExample06::getInstance);
        // 测试反射攻击：验证反射是否能破坏单例模式
        SingletonTestUtil.testReflection(SingletonExample06.class.getSimpleName(), SingletonExample06.class, SingletonExample06::getInstance);
    }
}

