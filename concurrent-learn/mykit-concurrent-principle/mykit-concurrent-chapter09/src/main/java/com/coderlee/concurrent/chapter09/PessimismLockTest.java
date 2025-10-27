package com.coderlee.concurrent.chapter09;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

import lombok.extern.slf4j.Slf4j;

/**
 * PessimismLockTest 类用于演示悲观锁的使用。
 * 悲观锁假设最坏的情况，在整个数据处理过程中，将数据处于锁定状态，
 * 适用于写操作多的场景，以保证数据的一致性。
 */
@Slf4j
public class PessimismLockTest {
    // 定义一个可重入锁对象
    private Lock lock = new ReentrantLock();

    /**
     * lockAndUnlock 方法展示如何使用悲观锁。
     * 使用 lock 进行加锁，并在 finally 块中确保锁一定会被释放。
     */
    public void lockAndUnlock() {
        lock.lock(); // 获取锁
        try {
            // 打印当前线程获取锁成功的信息
            log.info("{} 抢占锁成功", Thread.currentThread().getName());
        } finally {
            lock.unlock(); // 确保锁被释放
        }
    }

    /**
     * main 方法作为程序入口，创建多个线程来测试悲观锁的行为。
     * 创建了5个线程，每个线程尝试获取和释放锁，模拟并发环境下的锁竞争。
     *
     * @param args 命令行参数（此处未使用）
     */
    public static void main(String[] args) {
        PessimismLockTest test = new PessimismLockTest();
        IntStream.range(0, 5).forEach(i -> {
            new Thread(() -> {
                // 打印当前线程开始抢占锁的信息
                log.info("{} 开始抢占锁", Thread.currentThread().getName());
                test.lockAndUnlock(); // 调用 lockAndUnlock 方法
            }, "thread-" + i).start(); // 为线程命名以便于区分
        });
    }
}