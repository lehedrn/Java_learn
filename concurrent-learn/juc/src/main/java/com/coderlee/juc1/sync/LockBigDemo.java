package com.coderlee.juc1.sync;

import lombok.extern.slf4j.Slf4j;

/**
 * 演示锁粗化（Lock Coarsening）机制的示例类。
 *
 * 此类展示了JVM运行时优化中的"锁粗化"现象。
 * 当一系列连续的同步操作作用于同一个对象时，
 * JVM会将这些细粒度的锁合并成一个更大范围的锁，
 * 减少多次获取和释放锁的开销，提高程序性能。
 */
@Slf4j
public class LockBigDemo {

    /**
     * 主方法：演示锁粗化优化效果
     * 创建一个线程，在该线程中对同一个对象进行多次连续的同步操作
     *
     * @param args 命令行参数数组
     */
    public static void main(String[] args) {
        // 创建一个共享对象，用于后续的同步操作
        Object obj = new Object();

        // 启动一个新线程执行同步操作
        new Thread(() -> {
            // 第一次对obj对象加锁
            synchronized (obj) {
                log.info("1st lock obj");
            }

            // 第二次对obj对象加锁
            synchronized (obj) {
                log.info("2ed lock obj");
            }

            // 第三次对obj对象加锁
            synchronized (obj) {
                log.info("3rd lock obj");
            }

            // 第四次对obj对象加锁
            synchronized (obj) {
                log.info("4 lock obj");
            }

            // 注释说明：上述4次连续的同步操作相当于下面这一整块同步代码
            // JVM会进行锁粗化优化，将上面4个小的同步块合并为一个大的同步块
            synchronized (obj) {
                log.info("1st lock obj");
                log.info("2ed lock obj");
                log.info("3rd lock obj");
                log.info("4 lock obj");
            }
        }, "t1").start(); // 线程命名为"t1"
    }
}
