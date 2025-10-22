/**
 * ScheduledThreadPoolDemo.java
 * 
 * 该类演示了如何使用 ScheduledExecutorService 创建一个调度线程池，
 * 并通过它提交延迟任务。每个任务会生成一个随机数并返回结果。
 */
package com.coderlee.juc;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledThreadPoolDemo {

    /**
     * main 方法是程序的入口点。
     * 它创建了一个调度线程池，并提交了多个延迟任务。
     * 每个任务会在指定延迟后执行，生成一个随机数并打印结果。
     */
    public static void main(String[] args) {
        // 创建一个包含 5 个线程的调度线程池
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(5);

        // 提交 5 个延迟任务
        for (int i = 0; i < 5; i++) {
            // 使用 schedule 方法提交一个 Callable 任务，延迟 3 秒后执行
            Future<Integer> result = pool.schedule(new Callable<Integer>() {

                /**
                 * call 方法是任务的核心逻辑。
                 * 生成一个 0 到 99 的随机数，打印当前线程名称和随机数，并返回该随机数。
                 */
                @Override
                public Integer call() throws Exception {
                    int num = new Random().nextInt(100); // 生成随机数
                    System.out.println(Thread.currentThread().getName() + ": " + num); // 打印线程名称和随机数
                    return num; // 返回随机数
                }

            }, 3, TimeUnit.SECONDS); // 设置任务延迟 3 秒执行

            try {
                // 获取任务执行结果并打印
                System.out.println("任务返回结果: " + result.get());
            } catch (InterruptedException | ExecutionException e) {
                // 捕获异常并打印堆栈信息
                e.printStackTrace();
            }
        }

        // 关闭线程池，优雅地结束所有任务
        pool.shutdown();
    }
}