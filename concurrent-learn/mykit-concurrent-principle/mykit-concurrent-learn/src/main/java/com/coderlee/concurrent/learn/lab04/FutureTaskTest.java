package com.coderlee.concurrent.learn.lab04;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>演示如何使用 {@link FutureTask} 获取异步任务的结果。</p>
 * 
 * <p>此类包含两种实现方式：</p>
 * <ul>
 *   <li>通过 {@link Thread} 执行异步任务并获取结果。</li>
 *   <li>通过线程池（{@link ExecutorService}）执行异步任务并获取结果。</li>
 * </ul>
 * 
 * <p>该类主要用于展示 {@link FutureTask} 的基本用法，以及如何结合不同的执行器来完成异步计算。</p>
 */
@Slf4j
public class FutureTaskTest {

    public static void main(String[] args) {
        futureWithThread(); // 使用 Thread 执行异步任务
        futureWithExecutor(); // 使用线程池执行异步任务
    }

    /**
     * 使用线程池（{@link ExecutorService}）执行异步任务，并通过 {@link FutureTask} 获取结果。
     * 
     * <p>该方法创建一个单线程的线程池，提交一个 {@link FutureTask} 实例到线程池中执行。
     * 任务完成后，调用 {@link FutureTask#get()} 方法阻塞当前线程，直到结果可用。</p>
     * 
     * <p>无论是否发生异常，线程池都会在任务完成后被关闭。</p>
     */
    public static void futureWithExecutor() {
        ExecutorService executorService = Executors.newSingleThreadExecutor(); // 创建单线程线程池
        try {
            FutureTask<String> futureTask = new FutureTask<>(() -> {
                return "测试FutureTask获取异步结果，时间[" + LocalDateTime.now().toString() + "]"; // 异步任务逻辑
            });
            executorService.submit(futureTask); // 提交任务到线程池
            log.info("FutureTask with executor result is: {}", futureTask.get()); // 阻塞获取异步结果
        } catch (Exception e) {
            e.printStackTrace(); // 捕获并打印异常
        } finally {
            executorService.shutdown(); // 关闭线程池，释放资源
        }
    }

    /**
     * 使用 {@link Thread} 执行异步任务，并通过 {@link FutureTask} 获取结果。
     * 
     * <p>该方法创建一个 {@link FutureTask} 实例，并将其包装到一个新的 {@link Thread} 中执行。
     * 任务完成后，调用 {@link FutureTask#get()} 方法阻塞当前线程，直到结果可用。</p>
     */
    public static void futureWithThread() {
        FutureTask<String> futureTask = new FutureTask<>(() -> {
            return "测试FutureTask获取异步结果，时间[" + LocalDateTime.now().toString() + "]"; // 异步任务逻辑
        });
        new Thread(futureTask).start(); // 启动线程执行任务
        try {
            log.info("FutureTask with thread result is: {}", futureTask.get()); // 阻塞获取异步结果
        } catch (Exception e) {
            e.printStackTrace(); // 捕获并打印异常
        }
    }
}