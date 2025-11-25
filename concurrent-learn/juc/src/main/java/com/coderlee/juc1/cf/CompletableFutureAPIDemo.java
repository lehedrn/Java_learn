package com.coderlee.juc1.cf;

import com.coderlee.juc1.utils.SleepUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * CompletableFuture API 使用示例类
 *
 * 本类演示了 CompletableFuture 的获得结果和触发计算
 * 包括 get(), get(timeout), join(), getNow(), complete() 方法的使用方式
 * 和不同情况下的返回结果表现
 */
@Slf4j
public class CompletableFutureAPIDemo {

    public static void main(String[] args) {
        // m1();      // 演示 get() 阻塞等待结果
        // m2();      // 演示 get(timeout) 超时控制
        // m3();      // 演示 join() 阻塞等待结果
        // m4();      // 演示 getNow() 立即获取结果（不阻塞）
        // m4_1();    // 演示异步任务完成后使用 getNow()
        // m5();      // 演示 complete() 提前完成任务
        m5_1();       // 演示异步任务完成后使用 complete()
    }

    /**
     * 演示异步任务执行完毕后调用 complete() 方法的行为
     * complete() 会尝试完成 CompletableFuture，但如果已经完成则无效
     */
    public static void m5_1() {
        // 创建一个需要1秒执行时间的异步任务
        CompletableFuture<String> future = buildFuture(1000);
        // 主线程休眠1.1秒，确保异步任务已完成
        SleepUtils.sleep(1100);
        // complete("null") 尝试完成任务，但由于任务已结束，返回 false
        // future.join() 获取最终结果，由于任务已完成，返回 "coderlee"
        log.info("invoker rs: {}, final rs: {}", future.complete("null"), future.join());
    }

    /**
     * 演示在异步任务执行过程中调用 complete() 方法的行为
     * complete() 可以提前完成未结束的任务
     */
    public static void m5() {
        // 创建一个需要1秒执行时间的异步任务
        CompletableFuture<String> future = buildFuture(1000);
        // 在任务完成前调用 complete("null")
        // 由于任务尚未完成，complete 成功设置结果为 "null"，返回 true
        // future.join() 返回 complete 设置的值 "null"
        log.info("invoker rs: {}, final rs: {}", future.complete("null"), future.join());
    }

    /**
     * 演示异步任务执行完毕后使用 getNow() 获取结果
     * getNow() 不阻塞，立即返回结果或默认值
     */
    public static void m4_1() {
        // 创建一个需要1秒执行时间的异步任务
        CompletableFuture<String> future = buildFuture(1000);
        // 主线程休眠1.5秒，确保异步任务已完成
        SleepUtils.sleep(1500);
        // getNow("null") 获取当前结果，任务已完成，返回 "coderlee"
        String result = future.getNow("null");
        log.info("result: {}", result);
    }

    /**
     * 演示异步任务执行过程中使用 getNow() 获取结果
     * getNow() 不阻塞，立即返回默认值
     */
    public static void m4() {
        // 创建一个需要1秒执行时间的异步任务
        CompletableFuture<String> future = buildFuture(1000);
        // 异步任务尚未完成，getNow("null") 立即返回默认值 "null"
        String result = future.getNow("null");
        log.info("result: {}", result);
    }

    /**
     * 演示使用 join() 阻塞等待异步任务完成并获取结果
     * join() 与 get() 类似，但不抛出受检异常
     */
    public static void m3() {
        // 创建一个需要1秒执行时间的异步任务
        CompletableFuture<String> future = buildFuture(1000);
        // join() 阻塞等待直到任务完成，然后返回结果 "coderlee"
        String result = future.join();
        log.info("result: {}", result);
    }

    /**
     * 演示使用 get(timeout, unit) 带超时控制地获取异步任务结果
     * 如果在指定时间内任务未完成，则抛出 TimeoutException
     */
    public static void m2() {
        // 创建一个需要3秒执行时间的异步任务
        CompletableFuture<String> future = buildFuture(3000);
        try {
            // 设置1秒超时时间，由于任务需要3秒完成，会抛出 TimeoutException
            String result = future.get(1, TimeUnit.SECONDS);
            log.info("result: {}", result);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.error("run error", e);
        }
    }

    /**
     * 演示使用 get() 阻塞等待异步任务完成并获取结果
     * get() 会一直阻塞直到任务完成
     */
    public static void m1() {
        // 创建一个需要1秒执行时间的异步任务
        CompletableFuture<String> future = buildFuture(1000);
        try {
            // get() 阻塞等待直到任务完成，然后返回结果 "coderlee"
            String result = future.get();
            log.info("result: {}", result);
        } catch (InterruptedException | ExecutionException e) {
            log.error("run error", e);
        }
    }

    /**
     * 构建异步任务的工具方法
     * @param timeout 执行时间（毫秒）
     * @return CompletableFuture<String> 异步任务对象
     */
    private static CompletableFuture<String> buildFuture(long timeout) {
        return CompletableFuture.supplyAsync(() -> {
            // 模拟耗时操作
            SleepUtils.sleep(timeout);
            // 返回结果
            return "coderlee";
        });
    }
}
