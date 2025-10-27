package com.coderlee.concurrent.chapter09;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.IntStream;

import lombok.extern.slf4j.Slf4j;

/**
 * SharedLockTest 类用于演示读写锁（`ReadWriteLock`）中的共享锁（读锁）的使用。
 * 通过多个线程并发执行，展示如何使用共享锁来协调资源访问。
 */
@Slf4j
public class SharedLockTest {

    /**
     * 主方法，启动多个线程以测试共享锁的行为。
     * 每个线程尝试获取共享锁并执行业务逻辑。
     *
     * @param args 命令行参数（未使用）
     */
    public static void main(String[] args) {
        SharedLockTest test = new SharedLockTest();
        // 创建5个线程，模拟并发场景
        IntStream.range(0, 5).forEach(i -> {
            new Thread(() -> {
                log.info("线程 {} 尝试抢占锁", Thread.currentThread().getName());
                test.lockAndUnlock(); // 调用锁操作方法
            }, String.valueOf(i)).start();
        });
    }

    /**
     * lockAndUnlock 方法用于获取共享锁、执行业务逻辑并释放锁。
     * 展示了共享锁的基本使用模式：获取锁 -> 执行业务逻辑 -> 释放锁。
     */
    public void lockAndUnlock() {
        lock.lock(); // 获取共享锁
        try {
            log.info("线程 {} 抢占锁成功", Thread.currentThread().getName());
            // 模拟业务处理，例如读取共享资源
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.error("线程 {} 被中断", Thread.currentThread().getName());
        } finally {
            lock.unlock(); // 确保在任何情况下都释放锁
            log.info("线程 {} 释放锁成功", Thread.currentThread().getName());
        }
    }

    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock(); // 定义读写锁实例
    private Lock lock = readWriteLock.readLock(); // 获取读锁（共享锁），允许多个线程同时读取资源
}