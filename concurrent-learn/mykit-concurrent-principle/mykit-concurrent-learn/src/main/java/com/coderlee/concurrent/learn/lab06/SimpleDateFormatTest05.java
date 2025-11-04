package com.coderlee.concurrent.learn.lab06;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import lombok.extern.slf4j.Slf4j;

/**
 * 解决SimpleDateFormat类的线程安全问题 
 * 方案四：ThreadLocal方式
 * <p>
 * 该类通过使用 `ThreadLocal` 的方式，确保每个线程拥有独立的 `SimpleDateFormat` 实例副本，
 * 避免多线程并发调用时可能引发的线程安全问题。此方案在高并发场景下具有较高的运行效率，
 * 推荐在生产环境中使用。
 * </p>
 */
@Slf4j
public class SimpleDateFormatTest05 {

    // 定义线程池的执行次数，用于控制测试规模
    private static final int EXECUTE_COUNT = 1000;

    // 定义并发线程的最大数量，用于限制资源占用
    private static final int THREAD_COUNT = 20;

    /**
     * 使用 `ThreadLocal` 存储每个线程的 `SimpleDateFormat` 实例副本。
     * 
     * <p>
     * 每个线程首次调用 `threadLocal.get()` 时会初始化一个 `SimpleDateFormat` 对象，
     * 并在整个线程生命周期内复用该对象，从而避免线程间的共享状态冲突。
     * </p>
     */
    private static ThreadLocal<DateFormat> threadLocal = ThreadLocal.withInitial(() -> {
        return new SimpleDateFormat("yyyy-MM-dd"); // 初始化日期格式为 "yyyy-MM-dd"
    });

    public static void main(String[] args) {
        // 创建一个信号量，限制最大并发线程数为 THREAD_COUNT
        final Semaphore semaphore = new Semaphore(THREAD_COUNT);

        // 创建一个计数器，用于确保主线程等待所有子线程完成
        final CountDownLatch countDownLatch = new CountDownLatch(EXECUTE_COUNT);

        // 创建一个可缓存的线程池
        ExecutorService executorService = Executors.newCachedThreadPool();

        for (int i = 0; i < EXECUTE_COUNT; i++) {
            executorService.execute(() -> {
                try {
                    // 获取信号量许可，控制并发线程数
                    semaphore.acquire();
                    try {
                        // 调用当前线程的 `SimpleDateFormat` 实例解析日期字符串
                        threadLocal.get().parse("2025-11-13");
                    } catch (ParseException e) {
                        // 捕获日期格式化异常，并记录错误日志
                        log.error("线程: [{}] 格式化日期失败", Thread.currentThread().getName(), e);
                        System.exit(1); // 终止程序
                    } catch (NumberFormatException e) {
                        // 捕获数字格式化异常，并记录错误日志
                        log.error("线程: [{}] 格式化日期异常", Thread.currentThread().getName(), e);
                        System.exit(1); // 终止程序
                    } finally {
                        // 释放信号量许可，允许其他线程获取
                        semaphore.release();
                    }
                } catch (InterruptedException e) {
                    // 捕获信号量获取过程中可能出现的中断异常
                    log.error("线程: [{}] 获取信号量异常", Thread.currentThread().getName(), e);
                    System.exit(1); // 终止程序
                } finally {
                    // 显式清理 ThreadLocal 中的值，避免内存泄漏
                    threadLocal.remove();
                }


                // 计数器减一，表示当前线程已完成任务
                countDownLatch.countDown();
            });
        }

        try {
            // 等待所有线程执行完毕
            countDownLatch.await();
        } catch (InterruptedException e) {
            // 捕获主线程等待过程中可能出现的中断异常
            log.error("主线程等待异常", e);
        }

        // 关闭线程池
        executorService.shutdown();

        // 打印成功日志
        log.info("所有线程格式化日期成功");
    }
}