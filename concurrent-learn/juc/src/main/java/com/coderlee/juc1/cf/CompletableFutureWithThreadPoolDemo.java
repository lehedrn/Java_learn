package com.coderlee.juc1.cf;

import com.coderlee.juc1.utils.SleepUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * CompletableFutureWithThreadPoolDemo 类演示了 CompletableFuture 在不同线程池配置下的使用方式
 * 包括默认线程池、自定义线程池以及混合使用等场景
 */
@Slf4j
public class CompletableFutureWithThreadPoolDemo {

    /**
     * 程序入口方法，用于执行不同的测试方法
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // method1();  // 使用默认线程池执行所有任务
        // method2();  // 使用自定义线程池执行所有任务
//        method3();     // 混合使用不同线程池执行任务
        method4();
    }

    /**
     * method4 演示全部使用 thenRunAsync 的异步执行方式
     * 每个任务都会在独立的线程中执行
     */
    public static void method4() {
        // 创建一个固定大小为5的线程池
        ExecutorService threadPool = Executors.newFixedThreadPool(5);
        try {
            // 构建 CompletableFuture 异步任务链
            CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
                // 记录执行 task1 的线程名称
                log.info("{} do task1", Thread.currentThread().getName());
                return "task1";  // 返回任务结果
            }, threadPool).thenRunAsync(() -> {
                // 异步执行 task2
                log.info("{} do task2", Thread.currentThread().getName());
            }).thenRunAsync(() -> {
                // 异步执行 task3
                log.info("{} do task3", Thread.currentThread().getName());
            }).thenRunAsync(() -> {
                // 异步执行 task4
                log.info("{} do task4", Thread.currentThread().getName());
            });
            // 等待所有任务完成并记录结果
            log.info("complet future, result is{}", future.join());
        } finally {
            // 确保线程池被正确关闭
            threadPool.shutdown();
        }
    }

    /**
     * method3 演示混合使用 thenRunAsync 和 thenRun 的执行方式
     * 部分任务异步执行，部分任务在前一个任务的线程中同步执行
     */
    public static void method3() {
        // 创建一个固定大小为5的线程池
        ExecutorService threadPool = Executors.newFixedThreadPool(5);
        try {
            // 构建 CompletableFuture 异步任务链
            CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
                // 模拟耗时操作，睡眠20毫秒
                SleepUtils.sleep(20);
                // 记录执行 task1 的线程名称
                log.info("{} do task1", Thread.currentThread().getName());
                return "task1";  // 返回任务结果
            }, threadPool).thenRunAsync(() -> {
                // 使用 thenRunAsync 异步执行 task2，会在默认 ForkJoinPool 中执行
                SleepUtils.sleep(10);
                log.info("{} do task2", Thread.currentThread().getName());
            }).thenRun(() -> {
                // 使用 thenRun 同步执行 task3，在前一个任务的线程中执行
                SleepUtils.sleep(20);
                log.info("{} do task3", Thread.currentThread().getName());
            }).thenRun(() -> {
                // 使用 thenRun 同步执行 task4，在前一个任务的线程中执行
                SleepUtils.sleep(20);
                log.info("{} do task4", Thread.currentThread().getName());
            });
            // 等待所有任务完成并记录结果
            log.info("complet future, result is{}", future.join());
        } finally {
            // 确保线程池被正确关闭
            threadPool.shutdown();
        }
    }

    /**
     * method2 演示全部使用 thenRun 的同步执行方式
     * 所有任务都在前一个任务的线程中顺序执行
     */
    public static void method2() {
        // 创建一个固定大小为5的线程池
        ExecutorService threadPool = Executors.newFixedThreadPool(5);
        try {
            // 构建 CompletableFuture 异步任务链
            CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
                // 模拟耗时操作，睡眠20毫秒
                SleepUtils.sleep(20);
                // 记录执行 task1 的线程名称
                log.info("{} do task1", Thread.currentThread().getName());
                return "task1";  // 返回任务结果
            }, threadPool).thenRun(() -> {
                // 使用 thenRun 同步执行 task2，在前一个任务的线程中执行
                SleepUtils.sleep(10);
                log.info("{} do task2", Thread.currentThread().getName());
            }).thenRun(() -> {
                // 使用 thenRun 同步执行 task3，在前一个任务的线程中执行
                SleepUtils.sleep(20);
                log.info("{} do task3", Thread.currentThread().getName());
            }).thenRun(() -> {
                // 使用 thenRun 同步执行 task4，在前一个任务的线程中执行
                SleepUtils.sleep(20);
                log.info("{} do task4", Thread.currentThread().getName());
            });
            // 等待所有任务完成并记录结果
            log.info("complet future, result is{}", future.join());
        } finally {
            // 确保线程池被正确关闭
            threadPool.shutdown();
        }
    }

    /**
     * method1 演示使用默认线程池的执行方式
     * 所有任务都在默认的 ForkJoinPool.commonPool() 中执行
     */
    public static void method1() {
        // 构建 CompletableFuture 异步任务链，不指定线程池
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            // 模拟耗时操作，睡眠20毫秒
            SleepUtils.sleep(20);
            // 记录执行 task1 的线程名称
            log.info("{} do task1", Thread.currentThread().getName());
            return "task1";  // 返回任务结果
        }).thenRun(() -> {
            // 使用 thenRun 同步执行 task2，在前一个任务的线程中执行
            SleepUtils.sleep(10);
            log.info("{} do task2", Thread.currentThread().getName());
        }).thenRun(() -> {
            // 使用 thenRun 同步执行 task3，在前一个任务的线程中执行
            SleepUtils.sleep(20);
            log.info("{} do task3", Thread.currentThread().getName());
        }).thenRun(() -> {
            // 使用 thenRun 同步执行 task4，在前一个任务的线程中执行
            SleepUtils.sleep(20);
            log.info("{} do task4", Thread.currentThread().getName());
        });
        // 等待所有任务完成并记录结果
        log.info("complet future, result is{}", future.join());
    }
}
