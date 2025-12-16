package com.coderlee.juc1.volatiles;

/**
 * 线程安全的双重检查锁定单例模式实现
 * 使用 volatile 关键字防止指令重排序，保证多线程环境下的可见性和有序性
 */
public class SafeDoubleCheckSingleton {

    /**
     * 静态实例变量，使用 volatile 修饰保证内存可见性并防止指令重排序
     * volatile 确保：
     * 1. 多线程环境下对 singleton 变量修改的可见性
     * 2. 防止 JVM 对对象初始化过程中的指令重排序优化
     */
    private static volatile SafeDoubleCheckSingleton singleton;

    /**
     * 私有构造函数，防止外部直接通过 new 创建实例
     * 这是单例模式的基本要求
     */
    private SafeDoubleCheckSingleton() {}

    /**
     * 获取单例实例的公共静态方法
     * 采用双重检查锁定机制提高性能：
     * 1. 第一次检查避免不必要的同步操作
     * 2. 第二次检查确保只创建一个实例
     *
     * @return 返回唯一的 SafeDoubleCheckSingleton 实例
     */
    public static SafeDoubleCheckSingleton getInstance() {
        // 第一次检查：如果实例已存在，则直接返回，避免进入同步块
        if (singleton == null) {
            // 同步代码块，确保同一时间只有一个线程能执行实例创建逻辑
            synchronized (SafeDoubleCheckSingleton.class) {
                // 第二次检查：再次确认实例是否为空（可能其他线程已经创建）
                if (singleton == null) {
                    // 创建新的单例实例
                    singleton = new SafeDoubleCheckSingleton();
                }
            }
        }
        return singleton;
    }
}
