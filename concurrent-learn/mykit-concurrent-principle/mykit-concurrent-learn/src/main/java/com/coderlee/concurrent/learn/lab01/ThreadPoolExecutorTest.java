/**
 * ThreadPoolExecutorTest 类用于演示如何使用 `ThreadPoolExecutor` 创建和管理线程池。
 * 该类展示了通过线程池提交任务、获取异步结果以及执行无返回值任务的基本用法。
 */
package com.coderlee.concurrent.learn.lab01;

import java.time.LocalDateTime;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThreadPoolExecutorTest {

    /**
     * 静态线程池实例，核心线程数为3，最大线程数为3，空闲线程存活时间为30秒，任务队列容量为5。
     */
    private static ThreadPoolExecutor threadPool;

    static {
        // 初始化线程池，配置核心线程数、最大线程数、空闲时间、时间单位和任务队列
        threadPool = new ThreadPoolExecutor(
            3, // 核心线程数
            3, // 最大线程数
            30, // 空闲线程存活时间
            TimeUnit.SECONDS, // 时间单位
            new ArrayBlockingQueue<>(5) // 任务队列
        );
    }

    /**
     * 主方法，用于演示线程池的使用。
     * 包括提交无返回值任务、提交有返回值任务并获取结果、以及执行无返回值任务。
     */
    public static void main(String[] args) {
        // 记录主线程名称
        log.info("主线程名称============>> [{}]", Thread.currentThread().getName());

        try {
            // 提交一个无返回值的任务到线程池
            threadPool.submit(() -> {
                log.info("新创建的子线程名称============>> [{}]", Thread.currentThread().getName());
            });

            // 提交一个有返回值的任务到线程池，并获取返回值
            Future<String> future = threadPool.submit(() -> {
                log.info("新创建的子线程名称============>> [{}]", Thread.currentThread().getName());
                return LocalDateTime.now().toString(); // 返回当前时间字符串
            });

            try {
                // 获取异步任务的结果并记录日志
                log.info("从子线程中获取到的数据为===>> {}", future.get());
            } catch (Exception e) {
                // 捕获并打印异常信息
                e.printStackTrace();
            }

            // 执行一个无返回值的任务
            threadPool.execute(() -> {
                log.info("新创建的子线程名称============>> [{}]", Thread.currentThread().getName());
            });
        } finally {
            // 关闭线程池，确保资源释放
            threadPool.shutdown();
        }
    }
}