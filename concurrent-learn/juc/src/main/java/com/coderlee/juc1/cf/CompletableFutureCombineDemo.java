package com.coderlee.juc1.cf;

import com.coderlee.juc1.utils.CostTimeUtils;
import com.coderlee.juc1.utils.SleepUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

/**
 * CompletableFuture 组合操作演示类
 *
 * 本类展示了两种不同的 CompletableFuture 组合方式：
 * 1. 先创建独立的 CompletableFuture，然后使用 thenCombine 进行组合
 * 2. 链式调用 thenCombine 直接组合多个 CompletableFuture
 *
 * 通过 CostTimeUtils 工具类测量总执行时间，验证并行执行的效果
 */
@Slf4j
public class CompletableFutureCombineDemo {

    /**
     * 主方法，依次执行两个演示方法
     */
    public static void main(String[] args) {
        // 演示先创建独立的 CompletableFuture 再组合的方式
        method1();
        // 演示链式调用组合的方式
        method2();
    }

    /**
     * 演示链式调用 thenCombine 组合多个 CompletableFuture
     *
     * 执行流程：
     * 1. 异步执行 task1 (耗时10秒)
     * 2. 异步执行 task2 (耗时20秒)
     * 3. 将 task1 和 task2 的结果相加
     * 4. 异步执行 task3 (耗时30秒)
     * 5. 将前一步的结果与 task3 的结果相加
     *
     * 总共耗时约 max(10, 20, 30) = 30 秒，而不是累加的 60 秒
     */
    public static void method2() {
        // 使用工具类计算执行时间
        CostTimeUtils.calcCostTime(unused -> {
            // 链式调用 thenCombine 组合多个 CompletableFuture
            CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
                // task1: 模拟耗时10秒的任务
                log.info("{} do task1", Thread.currentThread().getName());
                SleepUtils.sleep(10);
                return 10;
            }).thenCombine(CompletableFuture.supplyAsync(() -> {
                // task2: 模拟耗时20秒的任务
                log.info("{} do task2", Thread.currentThread().getName());
                SleepUtils.sleep(20);
                return 20;
            }), (a, b) -> {
                // 合并 task1 和 task2 的结果
                log.info("{} do combin", Thread.currentThread().getName());
                return a + b;
            }).thenCombine(CompletableFuture.supplyAsync(() -> {
                // task3: 模拟耗时30秒的任务
                log.info("{} do task3", Thread.currentThread().getName());
                SleepUtils.sleep(30);
                return 30;
            }), (x, y) -> {
                // 合并前面的结果和 task3 的结果
                log.info("{} do combin", Thread.currentThread().getName());
                return x + y;
            });

            // 输出最终结果
            log.info("final result is {}", future.join());
        }, "combine1");
    }

    /**
     * 演示先创建独立的 CompletableFuture 再使用 thenCombine 组合
     *
     * 执行流程：
     * 1. 异步执行 task1 (耗时10秒)
     * 2. 异步执行 task2 (耗时20秒)
     * 3. 将 task1 和 task2 的结果相加
     *
     * 总共耗时约 max(10, 20) = 20 秒，而不是累加的 30 秒
     */
    public static void method1() {
        // 使用工具类计算执行时间
        CostTimeUtils.calcCostTime(unused -> {
            // 创建第一个异步任务 CompletableFuture
            CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
                // task1: 模拟耗时10秒的任务
                log.info("{} do task1", Thread.currentThread().getName());
                SleepUtils.sleep(10);
                return 10;
            });

            // 创建第二个异步任务 CompletableFuture
            CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> {
                // task2: 模拟耗时20秒的任务
                log.info("{} do task2", Thread.currentThread().getName());
                SleepUtils.sleep(20);
                return 20;
            });

            // 使用 thenCombine 组合两个 CompletableFuture 的结果
            CompletableFuture<Integer> result = future1.thenCombine(future2, (a, b) -> {
                // 合并两个任务的结果
                log.info("{} do combin", Thread.currentThread().getName());
                return a + b;
            });

            // 输出最终结果
            log.info("final result is {}", result.join());
        }, "combine");
    }
}
