package com.coderlee.artconcurrentbook.chapter01;

import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

/**
 * DeadLockDemo 死锁示例类.
 * <p>
 * 该类通过两个线程分别以不同的顺序获取两个锁，展示了死锁的发生场景。
 * 主要用于演示和学习多线程编程中死锁问题的成因及表现。
 * 
 * 运行程序后，可以使用jps结合jstack命令来观察死锁。
 * </p>
 * 
 * <p>
 * 避免死锁的几个常见方法：
 * 1. 避免一个线程同时获取多个锁；
 * 2. 避免一个线程在锁内同时占用多个资源，尽量保证每个锁只占用一个资源；
 * 3. 尝试使用定时锁，使用Lock.tryLock(timeout)来替代使用内部锁机制；
 * 4. 对于数据库锁，加锁和解锁必须在一个数据库连接里，否则会出现解锁失败的情况。
 * </p>
 * 
 */
@Slf4j
public class DeadLockDemo {

    // 定义两个静态字符串对象作为锁资源
    private static String A = "A";
    private static String B = "B";

    public static void main(String[] args) {
        new DeadLockDemo().deadLock();
    }

    /**
     * 演示死锁的方法.
     * <p>
     * 创建两个线程：
     * - 线程A先获取锁A，再尝试获取锁B；
     * - 线程B先获取锁B，再尝试获取锁A。
     * 由于两个线程持有锁的顺序相反，导致死锁的发生。
     */
    private void deadLock() {
        // 创建线程A
        Thread a = new Thread(() -> {
            synchronized (A) { // 获取锁A
                log.info("[{}] 获取到A锁，正在获取B锁", Thread.currentThread().getName());
                try {
                    TimeUnit.SECONDS.sleep(2); // 模拟业务处理，增加死锁发生的概率
                } catch (InterruptedException e) {
                    e.printStackTrace(); // 异常处理
                }
                synchronized (B) { // 尝试获取锁B
                    log.info("[{}] 获取到B锁", Thread.currentThread().getName());
                }
            }
        }, "Thread-A");

        // 创建线程B
        Thread b = new Thread(() -> {
            synchronized (B) { // 获取锁B
                log.info("[{}] 获取到B锁，正在获取A锁", Thread.currentThread().getName());
                synchronized (A) { // 尝试获取锁A
                    log.info("[{}] 获取到A锁", Thread.currentThread().getName());
                }
            }
        }, "Thread-B");

        a.start(); // 启动线程A
        b.start(); // 启动线程B
    }
}