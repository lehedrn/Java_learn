package com.coderlee.concurrent.learn.lab09;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * ScheduledThreadPoolTest 类用于演示如何使用 `ScheduledExecutorService` 来调度任务。
 * 它展示了两种调度方式：
 * 1. 使用 `schedule` 方法延迟执行一次性任务。
 * 2. 使用 `scheduleAtFixedRate` 方法以固定频率重复执行任务。
 * 
 * 该类还展示了如何优雅地关闭线程池并等待所有任务完成。
 */
@Slf4j
public class ScheduledThreadPoolTest {

    public static void main(String[] args) {
        // 创建一个包含5个线程的调度线程池
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);

        // 循环创建3个任务，并分别通过两种方式进行调度
        for (int i = 0; i < 3; i++) {
            Task worker = new Task("task-" + i);
            
            // 延迟5秒后执行一次任务
            scheduledThreadPool.schedule(worker, 5, TimeUnit.SECONDS);
            
            // 立即开始执行任务，并每隔5秒重复执行
            scheduledThreadPool.scheduleAtFixedRate(worker, 0, 5, TimeUnit.SECONDS);
        }

        try {
            // 主线程休眠10秒，观察任务执行情况
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.info("shutting down executor...");
        scheduledThreadPool.shutdown(); // 关闭线程池，不再接受新任务

        boolean isDone = false;
        do {
            try {
                // 等待线程池中的任务全部完成，最多等待1天
                isDone = scheduledThreadPool.awaitTermination(1, TimeUnit.DAYS);
                log.info("awaitTermination...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (!isDone); // 循环检查是否所有任务已完成

        log.info("Finished all threads");
    }
}

/**
 * Task 类实现了 `Runnable` 接口，用于定义一个可调度的任务。
 * 每个任务在执行时会记录开始时间和结束时间，并模拟耗时操作。
 */
@Slf4j
@AllArgsConstructor
class Task implements Runnable {

    private String name; // 任务名称

    /**
     * 任务的核心逻辑。
     * 1. 记录任务的开始时间。
     * 2. 模拟耗时操作（休眠1秒）。
     * 3. 记录任务的结束时间。
     */
    @Override
    public void run() {
        // 记录任务开始时间
        log.info("name = {}, startTime = {}", name, LocalDateTime.now());

        try {
            // 模拟任务耗时操作
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 记录任务结束时间
        log.info("name = {}, endTime = {}", name, LocalDateTime.now());
    }
}