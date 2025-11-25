package com.coderlee.juc1.cf;

import com.coderlee.juc1.utils.SleepUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * CompletableFuture构建示例类
 *
 * 该类演示了CompletableFuture的四种基本创建方式：
 * 1. runAsync() - 无返回值的异步执行
 * 2. runAsync(executor) - 使用自定义线程池的无返回值异步执行
 * 3. supplyAsync() - 有返回值的异步执行
 * 4. supplyAsync(executor) - 使用自定义线程池的有返回值异步执行
 *
 * 每个方法都展示了不同场景下的CompletableFuture使用方式
 */
@Slf4j
public class CompletableFutureBuildDemo {

    public static void main(String[] args) {
        // 执行supplyAsyncWithExecutor方法示例
//        runAsync();
//        runAsyncWithExecutor();
//        supplyAsync();
        supplyAsyncWithExecutor();
    }

    /**
     * 使用自定义线程池的supplyAsync示例
     *
     * 创建一个固定大小为3的线程池，使用CompletableFuture.supplyAsync执行有返回值的异步任务
     * 任务在线程池中的线程执行，返回结果后在主线程获取并打印
     */
    public static void supplyAsyncWithExecutor() {
        // 创建固定大小为3的线程池
        ExecutorService threadPool = Executors.newFixedThreadPool(3);

        // 使用supplyAsync创建有返回值的异步任务
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            // 记录当前执行线程名称
            log.info("{} -----is run", Thread.currentThread().getName());
            // 线程睡眠3秒模拟业务处理
            SleepUtils.sleep(3000L);
            // 返回结果
            return "coderlee";
        });

        try {
            // 获取异步任务执行结果并打印
            log.info("completableFuture get : {}", completableFuture.get());
        } catch (InterruptedException | ExecutionException e) {
            // 处理获取结果时可能出现的异常
            throw new RuntimeException(e);
        }

        // 记录主线程结束
        log.info("{} -----is end", Thread.currentThread().getName());
        // 关闭线程池
        threadPool.shutdown();
    }

    /**
     * 使用默认线程池的supplyAsync示例
     *
     * 使用CompletableFuture默认的ForkJoinPool线程池执行有返回值的异步任务
     * 任务在ForkJoinPool中的线程执行，返回结果后在主线程获取并打印
     */
    public static void supplyAsync() {
        // 使用supplyAsync创建有返回值的异步任务（使用默认线程池）
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            // 记录当前执行线程名称
            log.info("{} -----is run", Thread.currentThread().getName());
            // 线程睡眠3秒模拟业务处理
            SleepUtils.sleep(3000L);
            // 返回结果
            return "coderlee";
        });

        try {
            // 获取异步任务执行结果并打印
            log.info("completableFuture get : {}", completableFuture.get());
        } catch (InterruptedException | ExecutionException e) {
            // 处理获取结果时可能出现的异常
            throw new RuntimeException(e);
        }

        // 记录主线程结束
        log.info("{} -----is end", Thread.currentThread().getName());
    }

    /**
     * 使用自定义线程池的runAsync示例
     *
     * 创建一个固定大小为3的线程池，使用CompletableFuture.runAsync执行无返回值的异步任务
     * 任务在线程池中的线程执行完成后，在主线程等待并继续执行
     */
    public static void runAsyncWithExecutor() {
        // 创建固定大小为3的线程池
        ExecutorService threadPool = Executors.newFixedThreadPool(3);

        // 使用runAsync创建无返回值的异步任务
        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
            // 记录当前执行线程名称
            log.info("{} -----is run", Thread.currentThread().getName());
            // 线程睡眠3秒模拟业务处理
            SleepUtils.sleep(3000L);
        }, threadPool);

        try {
            // 等待异步任务执行完成
            log.info("completableFuture get : {}", completableFuture.get());
        } catch (InterruptedException | ExecutionException e) {
            // 处理等待过程中可能出现的异常
            throw new RuntimeException(e);
        }

        // 记录主线程结束
        log.info("{} -----is end", Thread.currentThread().getName());
        // 关闭线程池
        threadPool.shutdown();
    }

    /**
     * 使用默认线程池的runAsync示例
     *
     * 使用CompletableFuture默认的ForkJoinPool线程池执行无返回值的异步任务
     * 任务在ForkJoinPool中的线程执行完成后，在主线程等待并继续执行
     */
    public static void runAsync() {
        // 使用runAsync创建无返回值的异步任务（使用默认线程池）
        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
            // 记录当前执行线程名称
            log.info("{} -----is run", Thread.currentThread().getName());
            // 线程睡眠3秒模拟业务处理
            SleepUtils.sleep(3000L);
        });

        try {
            // 等待异步任务执行完成
            log.info("completableFuture get : {}", completableFuture.get());
        } catch (InterruptedException | ExecutionException e) {
            // 处理等待过程中可能出现的异常
            throw new RuntimeException(e);
        }

        // 记录主线程结束
        log.info("{} -----is end", Thread.currentThread().getName());
    }
}

