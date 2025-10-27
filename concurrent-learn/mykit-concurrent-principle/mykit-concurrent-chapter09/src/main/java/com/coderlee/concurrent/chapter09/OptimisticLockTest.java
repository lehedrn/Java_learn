package com.coderlee.concurrent.chapter09;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>演示乐观锁机制的类。</p>
 * <p>该类通过使用 {@link AtomicInteger} 实现线程安全的计数操作，
 * 展示了在高并发场景下如何利用乐观锁避免数据竞争问题。</p>
 * 
 * <p>核心功能：</p>
 * <ul>
 *   <li>通过多线程模拟并发环境，验证乐观锁的安全性。</li>
 *   <li>记录并输出最终的计数值，确保所有线程的操作都被正确执行。</li>
 * </ul>
 */
@Slf4j
public class OptimisticLockTest {

    /**
     * 使用 {@link AtomicInteger} 作为计数器，保证线程安全。
     * 通过原子操作实现乐观锁机制，避免显式加锁。
     */
    private AtomicInteger atomicInteger = new AtomicInteger(0);

    /**
     * 增加计数器的值。
     * <p>调用 {@link AtomicInteger#incrementAndGet()} 方法以原子方式递增计数器。</p>
     */
    public void incrementCount() {
        // 原子操作，确保线程安全
        atomicInteger.incrementAndGet();
    }

    /**
     * 获取当前计数器的值。
     *
     * @return 当前计数器的值。
     */
    public int getCount() {
        return atomicInteger.get();
    }

    /**
     * 主方法，用于启动多线程测试。
     * <p>创建 10000 个线程并发调用 {@link #incrementCount()} 方法，
     * 并在所有线程完成后输出最终计数值。</p>
     *
     * @param args 命令行参数（未使用）。
     */
    public static void main(String[] args) {
        OptimisticLockTest optimisticLockTest = new OptimisticLockTest();

        // 定义线程数量
        int threadNums = 10000;
        Thread[] threads = new Thread[threadNums];

        // 创建并启动线程
        IntStream.range(0, threadNums).forEach(i -> {
            threads[i] = new Thread(() -> {
                // 每个线程调用一次计数器递增操作
                optimisticLockTest.incrementCount();
            }, "Thread-" + i);
            threads[i].start();
        });

        // 等待所有线程执行完成
        IntStream.range(0, threadNums).forEach(i -> {
            try {
                threads[i].join(); // 确保主线程等待子线程结束
            } catch (InterruptedException e) {
                e.printStackTrace(); // 异常处理
            }
        });

        // 获取最终计数值并打印日志
        int count = optimisticLockTest.getCount();
        log.info("final count is: {}", count);
    }
}