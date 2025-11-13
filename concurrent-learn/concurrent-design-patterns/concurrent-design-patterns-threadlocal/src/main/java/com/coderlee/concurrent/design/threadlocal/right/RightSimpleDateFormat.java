/**
 * 正确使用 ThreadLocal 实现线程安全的日期格式化操作。
 * 使用 ThreadLocal 来保证每个线程拥有独立的 SimpleDateFormat 实例，
 * 避免多线程环境下 SimpleDateFormat 的线程安全问题。
 *
 * @see SimpleDateFormat
 * @see ThreadLocal
 */
package com.coderlee.concurrent.design.threadlocal.right;

import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

@Slf4j
public class RightSimpleDateFormat {

    /**
     * 执行任务总数。
     */
    private static final int EXECUTE_COUNT = 1000;

    /**
     * 并发线程数。
     */
    private static final int THREAD_COUNT = 20;

    /**
     * ThreadLocal 存储每个线程私有的 DateFormat 实例。
     */
    private static ThreadLocal<DateFormat> threadLocal = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));

    /**
     * 主方法，创建多个线程并发执行日期解析任务。
     * 使用 Semaphore 控制最大并发数，CountDownLatch 等待所有任务完成。
     *
     * @param args 启动参数
     */
    public static void main(String[] args) {
        // 初始化信号量控制并发数量
        final Semaphore semaphore = new Semaphore(THREAD_COUNT);
        // 初始化倒计时器等待所有任务完成
        final CountDownLatch countDownLatch = new CountDownLatch(EXECUTE_COUNT);
        // 创建线程池
        ExecutorService executorService = Executors.newCachedThreadPool();

        // 提交任务到线程池
        for (int i = 0; i < EXECUTE_COUNT; i++) {
            executorService.execute(() -> {
                try {
                    // 获取许可
                    semaphore.acquire();
                    try {
                        // 解析固定日期字符串
                        threadLocal.get().parse("2025-11-13");
                    } catch (ParseException e) {
                        // 记录异常日志并退出程序
                        log.error("线程: {} 格式化日期失败", Thread.currentThread().getName(), e);
                        System.exit(1);
                    } catch (NumberFormatException e) {
                        // 记录异常日志并退出程序
                        log.error("线程: {} 格式化日期失败", Thread.currentThread().getName(), e);
                        System.exit(1);
                    }
                    // 释放许可
                    semaphore.release();
                } catch (InterruptedException e) {
                    // 处理中断异常并退出程序
                    log.error("信号量发生错误", e);
                    System.exit(1);
                }
                // 减少倒计时器计数
                countDownLatch.countDown();
            });
        }

        try {
            // 等待所有任务完成
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 关闭线程池
        executorService.shutdown();
        // 输出成功信息
        log.info("所有线程格式化日期成功");
    }
}
