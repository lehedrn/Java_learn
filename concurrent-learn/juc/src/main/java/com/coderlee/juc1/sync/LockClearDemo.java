package com.coderlee.juc1.sync;

import lombok.extern.slf4j.Slf4j;

/**
 * 演示锁清除（Lock Clear）机制的示例类。
 *
 * 此类展示了在JVM运行时优化中的一种称为“锁消除”的现象。
 * 当一个对象仅在单个线程内被加锁使用时，JVM会识别出这种无竞争的同步操作，
 * 并自动移除不必要的同步开销，从而提升程序性能。
 */
@Slf4j
public class LockClearDemo {

    /**
     * 全局共享的对象锁实例，用于演示与局部对象的区别。
     * 这个对象在整个类的所有实例之间是共享的。
     */
    private static final Object objLock = new Object();

    /**
     * 主方法：启动多个线程并发执行m1方法。
     * 创建10个线程，每个线程都会调用demo实例的m1方法。
     *
     * @param args 命令行参数数组
     */
    public static void main(String[] args) {
        LockClearDemo demo = new LockClearDemo();
        for (int i = 0; i < 10; i++) {
            // 启动新线程并传入任务逻辑以及线程名称
            new Thread(() -> {
                demo.m1();
            }, String.valueOf(i)).start();
        }
    }

    /**
     * 示例方法，在其中使用synchronized关键字对局部对象进行加锁。
     *
     * JVM在此处可能会触发"锁消除"优化：
     * - 因为每次进入此方法都创建一个新的Object实例(obj)
     * - 每个线程持有的obj都是不同的对象，不存在资源竞争
     * - 所以JIT编译器可以安全地省略掉这个同步块，提高效率
     *
     * 输出包括当前线程名及两个对象的哈希码，便于观察是否发生锁消除。
     */
    public void m1() {
        // 局部变量obj，每次调用方法都会新建一个独立的对象
        Object obj = new Object();

        // 使用synchronized关键字锁定局部对象obj
        synchronized (obj) {
            // 记录日志，输出当前线程名、obj对象和objLock对象的哈希值
            log.info("{}----hello synchronized code block, obj hashcode: {}, objlock hashcode: {}",
                    Thread.currentThread().getName(),
                    obj.hashCode(),
                    objLock.hashCode());
        }
    }
}
