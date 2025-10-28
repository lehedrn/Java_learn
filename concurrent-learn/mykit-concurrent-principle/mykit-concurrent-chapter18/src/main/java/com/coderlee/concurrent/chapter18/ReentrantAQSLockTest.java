package com.coderlee.concurrent.chapter18;

import java.util.concurrent.locks.Lock;
import java.util.stream.IntStream;

import lombok.extern.slf4j.Slf4j;

/**
 * ReentrantAQSLockTest 是一个测试类，用于验证 {@link ReentrantAQSLock} 的功能。
 * 该类通过多线程环境下的锁操作，测试锁的可重入性和正确性。
 * 主要测试场景包括：多次获取锁、多次释放锁，以及多线程并发情况下的计数器递增。
 */
@Slf4j
public class ReentrantAQSLockTest {

    private int count; // 计数器变量，用于记录递增操作的次数
    private Lock lock = new ReentrantAQSLock(); // 使用自定义的可重入锁

    /**
     * 增加计数器的值，并测试锁的可重入性。
     * 在方法中，当前线程会两次获取锁并两次释放锁，确保锁的可重入性正常工作。
     */
    public void incrementCount() {
        try {
            lock.lock(); // 第一次获取锁
            log.info("{} 第一次获取到锁", Thread.currentThread().getName());
            lock.lock(); // 第二次获取锁（测试可重入性）
            log.info("{} 第二次获取到锁", Thread.currentThread().getName());
            count++; // 增加计数器的值
        } finally {
            lock.unlock(); // 第一次释放锁
            log.info("{} 第一次释放锁", Thread.currentThread().getName());
            lock.unlock(); // 第二次释放锁（与第二次获取锁对应）
            log.info("{} 第二次释放锁", Thread.currentThread().getName());
        }
    }

    /**
     * 获取当前计数器的值。
     *
     * @return 当前计数器的值
     */
    public long getCount() {
        return count; // 返回计数器的值
    }

    /**
     * 主方法，用于启动测试。
     * 创建多个线程并发执行 {@link #incrementCount()} 方法，
     * 并在所有线程完成后输出最终的计数器值。
     *
     * @param args 命令行参数（未使用）
     */
    public static void main(String[] args) {
        ReentrantAQSLockTest test = new ReentrantAQSLockTest(); // 创建测试实例
        IntStream.range(0, 5).forEach(i -> { // 启动 5 个线程
            try {
                Thread t = new Thread(() -> test.incrementCount()); // 每个线程执行 incrementCount 方法
                t.start(); // 启动线程
                t.join(); // 等待线程执行完成
            } catch (InterruptedException e) {
                e.printStackTrace(); // 捕获并打印中断异常
            }
        });
        log.info("Final count: {}", test.getCount()); // 输出最终的计数器值
    }
}
