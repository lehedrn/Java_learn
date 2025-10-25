package com.coderlee.juc;

import lombok.extern.slf4j.Slf4j;

/**
 * 该类用于比较串行执行和并发执行的性能差异。
 * 通过模拟计算密集型任务（如累加和递减操作）
 * 串行不一定速度比并发执行慢，为什么？
 * 上下文切换问题导致的
 */
@Slf4j
public class SerialVsConcurrentDemo {

    // 定义循环次数，数值越大，计算量越大
    private static final long count = 1000000L;

    public static void main(String[] args) throws InterruptedException {
        // 调用串行执行方法
        serial();
        // 调用并行执行方法
        concurrency();
    }

    /**
     * 并行执行方法，使用多线程完成计算密集型任务。
     * 主线程和子线程分别执行不同的计算任务，最后合并结果。
     */
    private static void concurrency() throws InterruptedException {
        // 记录开始时间
        long start = System.currentTimeMillis();

        // 创建一个新线程，用于执行部分计算任务
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                @SuppressWarnings("unused")
                int a = 0;
                // 子线程执行累加操作
                for (long i = 0; i < count; i++) {
                    a += 5; // 模拟计算任务
                }
            }
        });
        // 启动子线程
        thread.start();

        // 主线程执行递减操作
        int b = 0;
        for (long i = 0; i < count; i++) {
            b--; // 模拟计算任务
        }

        // 计算总耗时（包括等待子线程完成的时间）
        long time = System.currentTimeMillis() - start;

        // 等待子线程执行完毕
        thread.join();

        // 输出并行执行的耗时及结果
        log.info("并行执行耗时：{} ms, b = {}, a = {}", time, b);
    }

    /**
     * 串行执行方法，依次完成所有计算任务。
     * 所有计算任务都在主线程中顺序执行。
     */
    private static void serial() {
        // 记录开始时间
        long start = System.currentTimeMillis();

        // 主线程执行累加操作
        int a = 0;
        for (long i = 0; i < count; i++) {
            a += 5; // 模拟计算任务
        }

        // 主线程执行递减操作
        int b = 0;
        for (long i = 0; i < count; i++) {
            b--; // 模拟计算任务
        }

        // 计算总耗时
        long end = System.currentTimeMillis();

        // 输出串行执行的耗时及结果
        log.info("串行执行耗时：{} ms, b = {}, a = {}", end - start, b, a);
    }
}