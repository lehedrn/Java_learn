package com.coderlee.concurrent.chapter09;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>演示公平锁（Fair Lock）行为的测试类。</p>
 * 
 * <p>该类通过创建多个线程来模拟并发场景，展示了公平锁如何按照线程请求的顺序分配锁。
 * 公平锁的主要特点是它会维护一个等待队列，并按照 FIFO（先进先出）的顺序为线程分配锁，
 * 从而避免线程饥饿问题。</p>
 */
@Slf4j
public class FairLockTest {

    /**
     * 定义一个公平锁实例。
     * 使用 `ReentrantLock` 构造函数的参数 `true` 来启用公平锁模式。
     */
    private Lock lock = new ReentrantLock(true);

    /**
     * 主方法，用于启动多个线程测试公平锁的行为。
     * 
     * <p>创建 4 个线程，每个线程都会尝试调用 [fairLockAndUnlock] 方法。
     * 日志输出将展示线程按照启动顺序获得锁的情况，验证公平锁的行为。</p>
     */
    public static void main(String[] args) {
        // 创建 FairLockTest 实例
        FairLockTest fairLockTest = new FairLockTest();

        // 定义线程数组，用于存储 4 个线程
        Thread[] threads = new Thread[4];

        // 使用 IntStream 初始化线程数组
        IntStream.range(0, 4).forEach((i) -> {
            // 为每个线程指定任务和名称
            threads[i] = new Thread(() -> {
                log.info("{} 开始抢占锁", Thread.currentThread().getName());
                fairLockTest.fairLockAndUnlock();
            }, "Thread-" + i); // 线程命名为 "Thread-0" 到 "Thread-3"
        });

        // 启动所有线程
        IntStream.range(0, 4).forEach(i -> threads[i].start());
    }

    /**
     * 模拟线程抢占公平锁的过程。
     * 
     * <p>该方法首先尝试获取锁，成功获取后记录当前线程名称并释放锁。
     * 在获取锁的过程中，公平锁会确保线程按照请求顺序获得锁。</p>
     */
    public void fairLockAndUnlock() {
        // 获取锁
        lock.lock();
        try {
            // 记录当前线程成功抢占锁的日志信息
            log.info("{} 抢占锁成功", Thread.currentThread().getName());
        } finally {
            // 确保锁在任何情况下都能被释放
            lock.unlock();
        }
    }

}