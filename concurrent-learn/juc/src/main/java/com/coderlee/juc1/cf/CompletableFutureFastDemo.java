package com.coderlee.juc1.cf;

import com.coderlee.juc1.utils.CostTimeUtils;
import com.coderlee.juc1.utils.SleepUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

/**
 * CompletableFutureFastDemo 类演示了如何使用 CompletableFuture 的 applyToEither 方法
 * 来实现"最快完成者获胜"的并发模式。该示例启动两个并行任务，哪个任务先完成，
 * 就使用哪个任务的结果，并对结果进行处理。
 */
@Slf4j
public class CompletableFutureFastDemo {

    public static void main(String[] args) {
        // 使用工具类计算执行时间，"game" 为操作名称
        CostTimeUtils.calcCostTime(unused -> {
            // 创建第一个异步任务 future1
            CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
                // 记录线程开始执行任务 A 的日志
                log.info("{} A is start", Thread.currentThread().getName());
                // 模拟任务执行耗时 1 秒
                SleepUtils.sleep(1000);
                // 返回任务 A 的结果
                return "playA";
            });

            // 创建第二个异步任务 future2
            CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
                // 记录线程开始执行任务 B 的日志
                log.info("{} B is start", Thread.currentThread().getName());
                // 模拟任务执行耗时 1 秒
                SleepUtils.sleep(1000);
                // 返回任务 B 的结果
                return "playB";
            });

            // 使用 applyToEither 实现"最快完成者获胜"逻辑
            // 无论 future1 还是 future2 先完成，就立即应用转换函数 r -> r + " is winer"
            CompletableFuture<String> result = future1.applyToEither(future2, r -> r + " is winer");

            // 获取最终结果并记录日志，join() 会阻塞等待任一任务完成
            log.info("{} get final result, is [{}]", Thread.currentThread().getName(), result.join());
        }, "game");
    }
}
