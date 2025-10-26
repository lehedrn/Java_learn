package com.coderlee.concurrent.chapter09;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>该类用于演示公平锁和非公平锁的行为差异。</p>
 * <p>通过创建多个线程，分别测试在公平锁和非公平锁的模式下，
 * 线程抢占锁的顺序是否按照线程启动的顺序进行。</p>
 */
@Slf4j
public class FairOrNonFairLockTest {

    /**
     * 主方法，用于运行公平锁和非公平锁的测试。
     * 
     * @param args 命令行参数（未使用）
     */
    public static void main(String[] args) {
        // 测试公平锁行为
        log.info("=============================公平锁测试=================================");
        FairOrNonFairLockTest fairLockTest = new FairOrNonFairLockTest(true);
        fairLockTest.execute(5);

        // 测试非公平锁行为
        log.info("=============================非公平锁测试=================================");
        FairOrNonFairLockTest nonFairLockTest = new FairOrNonFairLockTest(false);
        nonFairLockTest.execute(10);
    }

    private Lock lock;

    /**
     * 构造函数，根据传入的布尔值决定创建公平锁或非公平锁。
     * 
     * @param isFair 如果为true，则创建公平锁；如果为false，则创建非公平锁
     */
    public FairOrNonFairLockTest(Boolean isFair) {
        // 根据isFair参数创建对应的锁实例
        this.lock = new ReentrantLock(isFair);
    }

    /**
     * 抢占锁并释放锁的方法，主要用于展示线程抢占锁的过程。
     * 该方法会记录当前线程抢占锁成功的日志信息。
     */
    public void lockAndUnlock() {
        lock.lock(); // 尝试获取锁
        try {
            log.info("{} 抢占锁成功", Thread.currentThread().getName());
        } finally {
            lock.unlock(); // 确保锁在finally块中被释放
        }
    }

    /**
     * 执行测试的核心方法，创建指定数量的线程，并让它们尝试抢占锁。
     * 
     * @param threadCount 线程的数量
     */
    public void execute(int threadCount) {
        Thread[] threads = new Thread[threadCount];

        // 创建线程数组，并让每个线程尝试抢占锁
        IntStream.range(0, threadCount).forEach((i) -> {
            threads[i] = new Thread(() -> {
                log.info("{} 开始抢占锁", Thread.currentThread().getName());
                lockAndUnlock();
            }, "Thread-" + i); // 为每个线程命名，方便日志追踪
        });

        // 启动所有线程
        IntStream.range(0, threadCount).forEach(i -> threads[i].start());

        // 等待所有线程执行完毕
        IntStream.range(0, threadCount).forEach(i -> {
            try {
                threads[i].join(); // 等待线程结束，确保主线程最后退出
            } catch (InterruptedException e) {
                e.printStackTrace(); // 捕获线程中断异常
            }
        });
    }
}