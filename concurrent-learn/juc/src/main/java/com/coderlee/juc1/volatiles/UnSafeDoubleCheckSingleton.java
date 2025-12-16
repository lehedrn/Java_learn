package com.coderlee.juc1.volatiles;

/**
 * 不安全的双重检查锁定单例模式实现
 * 在多线程环境下存在安全隐患，因为缺少 volatile 关键字保护
 */
public class UnSafeDoubleCheckSingleton {

    /**
     * 静态实例变量，未使用 volatile 修饰
     * 这会导致在多线程环境下可能出现问题：
     * 1. 指令重排序可能导致其他线程获取到未完全初始化的对象
     * 2. 缺乏内存可见性保证
     */
    private static UnSafeDoubleCheckSingleton singleton;

    /**
     * 私有构造函数，防止外部直接通过 new 创建实例
     * 这是单例模式的基本要求
     */
    private UnSafeDoubleCheckSingleton() {}

    /**
     * 获取单例实例的公共静态方法
     * 采用双重检查锁定机制尝试提高性能：
     * 1. 第一次检查避免不必要的同步操作
     * 2. 第二次检查确保只创建一个实例
     *
     * 注意：由于 singleton 变量缺少 volatile 修饰，此实现在多线程下是不安全的
     *
     * @return 返回 UnSafeDoubleCheckSingleton 实例（可能存在线程安全问题）
     */
    public static UnSafeDoubleCheckSingleton getInstance() {
        // 第一次检查：如果实例已存在，则直接返回，避免进入同步块
        if (singleton == null) {
            // 同步代码块，确保同一时间只有一个线程能执行实例创建逻辑
            synchronized (UnSafeDoubleCheckSingleton.class) {
                // 第二次检查：再次确认实例是否为空（可能其他线程已经创建）
                if (singleton == null) {
                    // 问题代码处：创建新的单例实例
                    // 由于缺少 volatile 保护，此处可能发生指令重排序
                    // 导致其他线程可能获取到未完全初始化的对象
                    singleton = new UnSafeDoubleCheckSingleton();
                }
            }
        }
        return singleton;
    }
}
