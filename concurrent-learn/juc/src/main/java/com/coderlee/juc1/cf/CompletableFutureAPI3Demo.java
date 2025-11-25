package com.coderlee.juc1.cf;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

/**
 * CompletableFuture API 演示类 - 展示不同类型的链式调用方法
 *
 * 本类演示了 CompletableFuture 的三种主要链式操作模式：
 * 1. thenApply - 转换阶段结果并传递给下一个阶段
 * 2. thenRun - 执行不依赖于前一阶段结果的操作
 * 3. thenAccept - 消费前一阶段的结果但不产生新结果
 */
@Slf4j
public class CompletableFutureAPI3Demo {
    public static void main(String[] args) {
        // 示例1: 使用 thenApply 进行结果转换的链式调用
        // 每个 thenApply 都会接收前一个阶段的结果并返回新值
        CompletableFuture.supplyAsync(() -> 1)           // 异步提供初始值 1
                .thenApply(f -> f + 1)                  // 将结果加1 (1->2)
                .thenApply(f -> f + 2)                  // 再将结果加2 (2->4)
                .thenAccept(r -> log.info("只消耗上一步结果，无需返回，最后输出: {}", r)); // 最终消费结果4
        log.info("main continue...");                   // 主线程继续执行，不等待异步任务完成

        // 示例2: 使用 thenRun 执行不关心结果的操作
        // thenRun 不接收前一阶段的结果，只是在前一阶段完成后执行指定操作
        CompletableFuture.supplyAsync(() -> "step1 result")  // 异步提供字符串结果
                .thenRun(() -> log.info("step2: do something")) // 不关心前一阶段结果，只执行操作
                .join();                                    // 等待整个链式操作完成

        log.info("main continue...");                       // 主线程继续执行

        // 示例3: 使用 thenAccept 消费前一阶段的结果
        // thenAccept 接收前一阶段的结果但不产生新的结果
        CompletableFuture.supplyAsync(() -> "step1 result")  // 异步提供字符串结果
                .thenAccept(r -> log.info("step2: do something, result is {}", r)) // 消费结果并执行操作
                .join();                                    // 等待整个链式操作完成

        log.info("main continue...");                       // 主线程继续执行

        // 示例4: 组合使用 thenApply 和 thenAccept
        // 展示完整的链式调用流程：转换 -> 消费
        CompletableFuture.supplyAsync(() -> "step1 result")  // 异步提供初始字符串结果
                .thenApply(r -> r + ", step2 result")       // 转换结果，添加额外信息
                .thenAccept(r -> log.info("step3: do something, result is {}", r)) // 消费最终结果
                .join();                                    // 等待整个链式操作完成
    }
}
