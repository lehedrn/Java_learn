package com.coderlee.concurrent.chapter16;

import java.util.stream.IntStream;

import lombok.extern.slf4j.Slf4j;

/**
 * CasLockTest 是一个用于测试 {@link CasLock} 接口及其具体实现的类。
 * <p>
 * 该类通过多线程环境下的计数操作验证锁的正确性和线程安全性。
 * 测试的核心逻辑是两个线程并发地对共享变量 {@code count} 进行递增操作，
 * 并确保最终结果与预期一致（即两个线程各执行 10000 次递增，总计 20000）。
 * <p>
 * 测试使用了 {@link MyCasLock} 作为锁的具体实现，并通过日志输出最终结果。
 */
@Slf4j
public class CasLockTest {

    /**
     * 锁的实例，用于保护对共享变量 {@code count} 的访问。
     */
    private CasLock lock = new MyCasLock();

    /**
     * 共享变量，用于记录递增操作的次数。
     */
    private long count = 0;

    /**
     * 对共享变量 {@code count} 进行线程安全的递增操作。
     * <p>
     * 该方法首先获取锁，然后对 {@code count} 进行递增，
     * 最后在 {@code finally} 块中释放锁，以确保即使发生异常也能正确释放锁。
     */
    public void incrementCount() {
        try {
            lock.lock(); // 获取锁，确保当前线程独占访问
            count++; // 对共享变量进行递增操作
        } finally {
            lock.unlock(); // 释放锁，允许其他线程访问
        }
    }

    /**
     * 获取当前共享变量 {@code count} 的值。
     *
     * @return 当前共享变量的值
     */
    public long getCount() {
        return count;
    }

    /**
     * 主方法，用于启动多线程测试。
     * <p>
     * 创建两个线程，每个线程对共享变量 {@code count} 执行 10000 次递增操作。
     * 线程执行完成后，输出最终的计数值以验证锁的正确性。
     *
     * @param args 命令行参数（未使用）
     */
    public static void main(String[] args) {
        CasLockTest test = new CasLockTest();

        // 创建线程 A，执行 10000 次递增操作
        Thread threadA = new Thread(() -> {
            IntStream.rangeClosed(1, 10000).forEach(i -> test.incrementCount());
        });

        // 创建线程 B，执行 10000 次递增操作
        Thread threadB = new Thread(() -> {
            IntStream.rangeClosed(1, 10000).forEach(i -> test.incrementCount());
        });

        // 启动线程 A 和线程 B
        threadA.start();
        threadB.start();

        // 等待线程 A 和线程 B 执行完成
        try {
            threadA.join(); // 等待线程 A 完成
            threadB.join(); // 等待线程 B 完成
        } catch (InterruptedException e) {
            // 捕获中断异常并打印堆栈信息
            e.printStackTrace();
        }

        // 输出最终计数值
        log.info("Final count: {}", test.getCount());
    }
}
