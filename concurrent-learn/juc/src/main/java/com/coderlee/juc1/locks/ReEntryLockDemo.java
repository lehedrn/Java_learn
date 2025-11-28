package com.coderlee.juc1.locks;

import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 可重入锁演示类
 * 本类演示了 Java 中可重入锁的概念，包括 synchronized 关键字和 ReentrantLock 的可重入特性，
 * 同时展示了错误使用可重入锁导致的问题。
 */
@Slf4j
public class ReEntryLockDemo {
    // 创建一个 ReentrantLock 实例
    Lock lock = new ReentrantLock();

    /**
     * 主方法，程序入口
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 可以取消注释以下任意一行来运行不同的演示方法

        // reEntry1();     // 演示 synchronized 关键字的可重入性
        // reEntry2();     // 演示 synchronized 方法的可重入性
        // reEntry3();     // 演示 ReentrantLock 的正确可重入使用
        wrongReEntry();    // 演示 ReentrantLock 错误使用导致的问题
    }

    /**
     * 演示 ReentrantLock 错误使用的场景
     * 展示了当加锁次数与解锁次数不匹配时导致其他线程无法获取锁的问题
     */
    public static void wrongReEntry() {
        ReEntryLockDemo demo = new ReEntryLockDemo();
        // 创建三个线程同时执行 wrongUseLock 方法
        new Thread(demo::wrongUseLock, "t1").start();
        new Thread(demo::wrongUseLock, "t2").start();
        new Thread(demo::wrongUseLock, "t3").start();
    }

    /**
     * 演示 ReentrantLock 正确可重入使用的场景
     */
    public static void reEntry3() {
        ReEntryLockDemo demo = new ReEntryLockDemo();
        // 创建一个线程执行 useLock 方法
        new Thread(demo::useLock, "t1").start();
    }

    /**
     * 演示 synchronized 方法的可重入性
     */
    public static void reEntry2() {
        ReEntryLockDemo demo = new ReEntryLockDemo();
        // 创建一个线程执行 m1 方法
        new Thread(demo::m1, "t1").start();
    }

    /**
     * 演示 synchronized 代码块的可重入性
     * 在同一个线程中多次获取同一个对象的监视器锁
     */
    public static void reEntry1() {
        final Object obj = new Object();
        new Thread(() -> {
            // 第一次获取 obj 对象的监视器锁
            synchronized (obj) {
                log.info("{} 拿到锁，准备进入下一层[{}]", Thread.currentThread().getName(), 2);
                // 第二次获取 obj 对象的监视器锁（可重入）
                synchronized (obj) {
                    log.info("{} 拿到锁，准备进入下一层[{}]", Thread.currentThread().getName(), 3);
                    // 第三次获取 obj 对象的监视器锁（可重入）
                    synchronized (obj) {
                        log.info("{} 拿到锁，目前在第[{}]层", Thread.currentThread().getName(), 3);
                    }
                }
            }
        }, "t1").start();
    }

    /**
     * 错误使用 ReentrantLock 的示例方法
     * 故意缺少一次 unlock 调用，导致锁无法完全释放
     */
    public void wrongUseLock() {
        // 第一次加锁
        lock.lock();
        try {
            log.info("{} come in ---> {}", Thread.currentThread().getName(), "1");
            // 第二次加锁（可重入）
            lock.lock();
            try {
                log.info("{} come in ---> {}", Thread.currentThread().getName(), "2");
                // 第三次加锁（可重入）
                lock.lock();
                try {
                    log.info("{} come in ---> {}", Thread.currentThread().getName(), "3");
                } finally {
                    // 故意少了一个配对的unlock
                    // 由于加锁次数和释放次数不一样，第二个线程始终无法获取到锁，导致一直在等待。
                    // lock.unlock(); // 这里缺少了一次解锁操作
                }
            } finally {
                // 第二次解锁
                lock.unlock();
            }
        } finally {
            // 第一次解锁
            lock.unlock();
        }
    }

    /**
     * 正确使用 ReentrantLock 可重入特性的示例方法
     * 每次加锁都有对应的解锁操作
     */
    public void useLock() {
        // 第一次加锁
        lock.lock();
        try {
            log.info("{} come in ---> {}", Thread.currentThread().getName(), "1");
            // 第二次加锁（可重入）
            lock.lock();
            try {
                log.info("{} come in ---> {}", Thread.currentThread().getName(), "2");
                // 第三次加锁（可重入）
                lock.lock();
                try {
                    log.info("{} come in ---> {}", Thread.currentThread().getName(), "3");
                } finally {
                    // 第三次解锁
                    lock.unlock();
                }
            } finally {
                // 第二次解锁
                lock.unlock();
            }
        } finally {
            // 第一次解锁
            lock.unlock();
        }
    }

    /**
     * 使用 synchronized 关键字的可重入示例方法1
     * 调用另一个 synchronized 方法
     */
    public synchronized void m1() {
        log.info("{} come in ---> {}", Thread.currentThread().getName(), "m1");
        m2(); // 调用另一个 synchronized 方法（可重入）
        log.info("{} invoke {} is done.", Thread.currentThread().getName(), "m1");
    }

    /**
     * 使用 synchronized 关键字的可重入示例方法2
     * 调用另一个 synchronized 方法
     */
    public synchronized void m2() {
        log.info("{} come in ---> {}", Thread.currentThread().getName(), "m2");
        m3(); // 调用另一个 synchronized 方法（可重入）
    }

    /**
     * 使用 synchronized 关键字的可重入示例方法3
     */
    public synchronized void m3() {
        log.info("{} come in ---> {}", Thread.currentThread().getName(), "m3");
    }
}
