package com.coderlee.concurrent.learn.lab09;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

/**
 * ScheduledThreadPoolExecutorTest 类用于演示如何使用 `ScheduledExecutorService` 来调度任务。
 * 它展示了通过 `scheduleAtFixedRate` 方法以固定频率执行任务的功能。
 * 
 * 该类还展示了如何优雅地关闭线程池并等待所有任务完成。
 */
@Slf4j
public class ScheduledThreadPoolExecutorTest {

    /**
     * 主方法，程序入口。
     * 1. 创建一个包含3个线程的调度线程池。
     * 2. 使用 `scheduleAtFixedRate` 方法调度一个任务，该任务每隔1秒执行一次。
     * 3. 主线程休眠10秒，观察任务执行情况。
     * 4. 关闭线程池并等待所有任务完成。
     *
     * @param args 命令行参数（未使用）。
     */
    public static void main(String[] args) {
        // 创建一个包含3个线程的调度线程池
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(3);

        // 调度任务，首次延迟1秒后开始执行，之后每隔1秒重复执行
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            log.info("测试ScheduledExecutor，时间: {}", LocalDateTime.now());
        }, 1, 1, TimeUnit.SECONDS);

        try {
            // 主线程休眠10秒，观察任务执行情况
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            // 捕获中断异常并打印堆栈信息
            e.printStackTrace();
        }

        log.info("正在关闭线程池...");

        scheduledExecutorService.shutdown(); // 关闭线程池，不再接受新任务

        boolean isClosed = false;
        do {
            try {
                // 等待线程池中的任务全部完成，最多等待1天
                isClosed = scheduledExecutorService.awaitTermination(1, TimeUnit.DAYS);
            } catch (InterruptedException e) {
                // 捕获中断异常并打印堆栈信息
                e.printStackTrace();
            }
        } while (!isClosed); // 循环检查是否所有任务已完成

        log.info("所有线程执行结束，线程池已关闭");
    }
}