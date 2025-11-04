package com.coderlee.concurrent.learn.lab06;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import lombok.extern.slf4j.Slf4j;

/**
 * 解决SimpleDateFormat类的线程安全问题 
 * 方案五：DateTimeFormatter方式
 * <p>
 * DateTimeFormatter是Java8提供的新的日期时间API中的类，DateTimeFormatter类是线程安全的，可以在高并发场景下直接使用DateTimeFormatter类来处理日期的格式化操作
 * 使用DateTimeFormatter类来处理日期的格式化操作运行效率比较高，推荐在高并发业务场景的生产环境使用。
 * </p>
 */
@Slf4j
public class SimpleDateFormatTest07 {

    // 定义线程池的执行次数，用于控制测试规模
    private static final int EXECUTE_COUNT = 1000;

    // 定义并发线程的最大数量，用于限制资源占用
    private static final int THREAD_COUNT = 20;

    // 定义线程安全的日期格式化器
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 主方法：程序入口，用于启动多线程测试。
     * 
     * <p>逻辑步骤：</p>
     * <ol>
     *   <li>创建信号量 (`Semaphore`)，限制最大并发线程数为 [THREAD_COUNT](#L19)。</li>
     *   <li>创建计数器 (`CountDownLatch`)，用于确保主线程等待所有子线程完成。</li>
     *   <li>创建线程池 (`ExecutorService`)，用于管理并发任务。</li>
     *   <li>提交多个任务到线程池，每个任务尝试解析固定日期字符串。</li>
     *   <li>捕获并处理可能的异常，确保程序稳定性。</li>
     *   <li>主线程等待所有子线程完成后，打印成功日志并关闭线程池。</li>
     * </ol>
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
                        // 使用线程安全的 `DateTimeFormatter` 解析日期字符串
                        LocalDate.parse("2025-11-14", formatter);
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