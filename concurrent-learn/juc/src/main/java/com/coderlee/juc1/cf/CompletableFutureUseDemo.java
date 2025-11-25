package com.coderlee.juc1.cf;

import com.coderlee.juc1.utils.SleepUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * CompletableFuture 使用示例类
 *
 * 演示了如何使用 CompletableFuture 进行异步编程，包括基本用法和异常处理机制。
 */
@Slf4j
public class CompletableFutureUseDemo {
    public static void main(String[] args) {
//        future1();
        // 调用 future2 方法演示异常处理
        future2();
    }

    /**
     * 演示带有异常处理的 CompletableFuture 使用方式
     *
     * 创建一个异步任务，在其中模拟随机数计算并有可能抛出异常，
     * 并通过 whenComplete 和 exceptionally 方法处理正常完成和异常情况。
     */
    public static void future2() {
        // 创建固定大小为3的线程池用于执行异步任务
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        try {
            // 开始异步任务链
            CompletableFuture.supplyAsync(() -> {
                // 记录当前线程进入日志
                log.info("{} come in", Thread.currentThread().getName());
                // 生成0-9之间的随机整数作为结果
                int result = ThreadLocalRandom.current().nextInt(10);
                // 模拟耗时操作，休眠1秒
                SleepUtils.sleep(1000);
                // 输出一秒后的结果值
                log.info("{} 1秒后出结果: {}", Thread.currentThread().getName(), result);
                // 如果结果大于2，则人为制造除零异常以测试异常处理
                if (result > 2) {
                    int i = result / 0;
                }
                return result;
            }, threadPool)
            // 当异步任务正常完成后调用此回调函数
            .whenComplete((v, e) -> {
                // 如果没有发生异常，则输出处理完成的结果
                if (e == null) {
                    log.info("{} 处理完成返回结果: {}", Thread.currentThread().getName(), v);
                }
            })
            // 处理异步任务中的异常情况
            .exceptionally(e -> {
                // 打印异常信息
                log.error("{} 异常: {}", Thread.currentThread().getName(), e.getMessage());
                // 返回默认值null表示出现异常时的返回结果
                return null;
            });
        } catch (Exception e) {
            // 捕获并记录可能发生的其他异常
            log.error("{}", e.getMessage());
        } finally {
            // 关闭线程池释放资源
            threadPool.shutdown();
        }
    }

    /**
     * 演示基础的 CompletableFuture 使用方式
     *
     * 创建一个简单的异步任务，并获取其执行结果。
     */
    public static void future1() {
        // 启动一个异步任务来计算随机数
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
            // 记录当前线程进入日志
            log.info("{} come in", Thread.currentThread().getName());
            // 生成0-9之间的随机整数作为结果
            int result = ThreadLocalRandom.current().nextInt(10);
            // 模拟耗时操作，休眠1秒
            SleepUtils.sleep(1000);
            // 输出一秒后的结果值
            log.info("{} 1秒后出结果: {}", Thread.currentThread().getName(), result);
            return result;
        });

        // 主线程继续处理其他业务逻辑
        log.info("{} 处理其他业务逻辑", Thread.currentThread().getName());

        try {
            // 获取异步任务的执行结果（会阻塞直到结果可用）
            log.info("{} 获取结果: {}", Thread.currentThread().getName(), completableFuture.get());
        } catch (InterruptedException | ExecutionException e) {
            // 抛出运行时异常以便上层处理
            throw new RuntimeException(e);
        }
    }
}
