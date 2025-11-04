package com.coderlee.concurrent.learn.lab06;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import lombok.extern.slf4j.Slf4j;

/**
 * 解决SimpleDateFormat类的线程安全问题 
 * 方案六：joda-time方式
 * <p>描述：该类用于演示如何通过 joda-time 解决 `SimpleDateFormat` 的线程安全问题。</p>
 * 
 * <p>joda-time 是一个第三方日期时间处理库，具有线程安全性。与 JDK 自带的 `SimpleDateFormat` 不同，
 * joda-time 提供了更高的并发性能和可靠性，因此推荐在高并发场景下使用。</p>
 * 
 * <p>该类通过以下方式测试 joda-time 的线程安全性：</p>
 * <ul>
 *     <li>使用信号量（{@link Semaphore}）限制最大并发线程数。</li>
 *     <li>使用计数器（{@link CountDownLatch}）确保主线程等待所有子线程完成任务。</li>
 *     <li>在线程池中模拟多线程环境，验证日期格式化的正确性和线程安全性。</li>
 * </ul>
 */
@Slf4j
public class SimpleDateFormatTest08 {

    // 定义线程池的执行次数，用于控制测试规模
    private static final int EXECUTE_COUNT = 1000;

    // 定义并发线程的最大数量，用于限制资源占用
    private static final int THREAD_COUNT = 20;

    // 定义日期格式化器，使用 joda-time 的线程安全实现
    private static DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");

    /**
     * 主方法，用于启动测试。
     * <p>测试流程如下：</p>
     * <ol>
     *     <li>创建信号量，限制最大并发线程数为 {@code THREAD_COUNT}。</li>
     *     <li>创建计数器，确保主线程等待所有子线程完成任务。</li>
     *     <li>通过线程池模拟多线程环境，每个线程尝试解析固定日期字符串。</li>
     *     <li>捕获并处理可能的异常，确保程序的健壮性。</li>
     *     <li>打印成功日志，表示所有线程均成功完成任务。</li>
     * </ol>
     *
     * @param args 命令行参数（未使用）
     */
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
                        // 使用 joda-time 的线程安全日期解析方法
                        DateTime.parse("2025-11-14", formatter).toDate();
                    } catch (Exception e) {
                        // 捕获日期格式化异常，并记录错误日志
                        log.error("线程: [{}] 格式化日期失败", Thread.currentThread().getName(), e);
                        System.exit(1); // 终止程序
                    } finally {
                        // 释放信号量许可，允许其他线程获取
                        semaphore.release();
                    }
                } catch (InterruptedException e) {
                    // 捕获信号量获取过程中可能出现的中断异常
                    log.error("线程: [{}] 获取信号量异常", Thread.currentThread().getName(), e);
                    System.exit(1); // 终止程序
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