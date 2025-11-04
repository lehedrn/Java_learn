package com.coderlee.concurrent.learn.lab06;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * <p>演示 {@link SimpleDateFormat} 在多线程环境下的线程安全性问题。</p>
 * 
 * <p>
 * 该类通过创建多个线程并发调用同一个 {@code SimpleDateFormat} 实例的解析方法，
 * 演示了潜在的线程安全问题。如果发生异常（如格式化失败或数字格式错误），
 * 程序会记录错误日志并终止运行。
 * </p>
 * <p>
 * DateFormat类中的Calendar对象被多线程共享，而Calendar对象本身不支持线程安全。
 * </p>
 */
@Slf4j
public class SimpleDateFormatTest01 {

    private static final int EXECUTE_COUNT = 1000; // 总执行次数
    private static final int THREAD_COUNT = 20;   // 并发线程数

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 主方法，用于启动多线程测试。
     * 
     * <p>通过信号量（{@link Semaphore}）限制同时运行的线程数，
     * 使用计数器（{@link CountDownLatch}）确保主线程等待所有子线程完成。</p>
     * 
     */
    public static void main(String[] args) {
        final Semaphore semaphore = new Semaphore(THREAD_COUNT); // 控制并发线程数
        final CountDownLatch countDownLatch = new CountDownLatch(EXECUTE_COUNT); // 等待所有任务完成
        ExecutorService executorService = Executors.newCachedThreadPool(); // 创建线程池

        for (int i = 0; i < EXECUTE_COUNT; i++) {
            executorService.execute(() -> {
                try {
                    semaphore.acquire(); // 获取信号量许可
                    try {
                        // 尝试解析日期字符串，可能会抛出 ParseException 或 NumberFormatException
                        simpleDateFormat.parse("2025-11-13");
                    } catch (ParseException e) {
                        log.error("线程: [{}] 格式化日期失败", Thread.currentThread().getName(), e);
                        System.exit(1); // 发生异常时终止程序
                    } catch (NumberFormatException e) {
                        log.error("线程: [{}] 格式化日期异常", Thread.currentThread().getName(), e);
                        System.exit(1); // 发生异常时终止程序
                    }
                    semaphore.release(); // 释放信号量许可
                } catch (InterruptedException e) {
                    log.error("线程: [{}] 获取信号量异常", Thread.currentThread().getName(), e);
                    System.exit(1); // 发生异常时终止程序
                }
                countDownLatch.countDown(); // 计数器减一，表示一个任务完成
            });
        }

        try {
            countDownLatch.await(); // 主线程等待所有任务完成
        } catch (InterruptedException e) {
            log.error("主线程等待异常", e);
        }

        executorService.shutdown(); // 关闭线程池
        log.info("所有线程格式化日期成功"); // 所有任务完成后打印成功日志
    }
}