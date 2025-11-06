package com.coderlee.artconcurrentbook.chapter01;

import lombok.extern.slf4j.Slf4j;

/**
 * ConcurrencyTest 类用于演示并发执行和串行执行的性能对比。
 * <p>
 * 该类通过两个方法 {@link #concurrency()} 和 {@link #serial()} 分别模拟了并发执行和串行执行的操作
 * 
 * 运行结果表示：
 * 多线程不一定快
 * 并发执行累加不一定速度会比串行执行的快
 * 
 * 因为线程有创建和上下文切换的开销。
 * </p>
 * 
 * <p>
 * 减少上下文切换的方法：
 * 无锁并发编程
 * CAS算法
 * 使用最少线程
 * 使用协程
 * </p>
 * 
 */
@Slf4j
public class ConcurrencyTest {
    private static final long count = 10000; // 定义循环次数，控制计算量

    public static void main(String[] args) {
        concurrency(); // 调用并发执行方法
        serial();      // 调用串行执行方法
    }

    /**
     * 并发执行方法。
     * <p>
     * 该方法创建一个线程来执行部分计算任务（变量 a 的累加），同时在主线程中执行另一部分计算任务（变量 b 的递减）。
     * 最终通过日志输出并发执行的耗时以及计算结果。
     */
    private static void concurrency() {
        long start = System.currentTimeMillis(); // 记录开始时间
        Thread thread = new Thread(() -> {
            int a = 0;
            for (int i = 0; i < count; i++) { // 循环进行累加操作
                a += 5;
            }
            log.info("a = {}", a); // 输出变量 a 的最终值
        });
        thread.start(); // 启动线程
        int b = 0;
        for (int i = 0; i < count; i++) { // 主线程进行递减操作
            b--;
        }
        try {
            thread.join(); // 等待子线程执行完毕
        } catch (InterruptedException e) {
            e.printStackTrace(); // 捕获并处理线程中断异常
        }
        log.info("concurrency: {} ms, b = {}", System.currentTimeMillis() - start, b); // 输出并发执行耗时及 b 的值
    }

    /**
     * 串行执行方法。
     * <p>
     * 该方法在单一线程中依次完成所有计算任务（变量 a 的累加和变量 b 的递减），
     * 并通过日志输出串行执行的耗时以及计算结果。
     */
    private static void serial() {
        long start = System.currentTimeMillis(); // 记录开始时间
        int a = 0;
        for (int i = 0; i < count; i++) { // 循环进行累加操作
            a += 5;
        }
        int b = 0;
        for (int i = 0; i < count; i++) { // 循环进行递减操作
            b--;
        }
        log.info("serial: {} ms, b = {}, a = {}", System.currentTimeMillis() - start, b, a); // 输出串行执行耗时及结果
    }
}