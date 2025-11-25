package com.coderlee.juc1.cf;

import com.coderlee.juc1.utils.SleepUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * CompletableFuture API 示例类 - 对计算结果进行处理, 演示 handle 和 thenApply 方法的区别
 * <p>
 * 本类通过两个示例方法展示了 CompletableFuture 中 handle 和 thenApply 方法的不同行为：
 * - handle: 无论前一个阶段是否发生异常都会执行，可以处理异常情况
 * - thenApply: 只有当前一个阶段正常完成时才会执行，遇到异常会中断链式调用
 * <p>
 * 示例中使用了线程池来执行异步任务，并演示了异常处理机制
 */
@Slf4j
public class CompletableFutureAPI2Demo {

    public static void main(String[] args) {
        // 注释：默认运行 handle 示例方法，展示 handle 的异常处理能力
        // 如果需要查看 thenApply 的行为，可取消下面一行的注释并注释掉 handle() 调用
        // thenApply();
        handle();
    }


    /**
     * 演示 handle 方法的行为：
     * - handle 方法无论前一个阶段是否抛出异常都会被执行
     * - 可以同时处理正常结果和异常情况
     * - 即使在 handle 中出现新的异常，后续的 handle 仍然会被调用
     */
    public static void handle() {
        // 创建固定大小为3的线程池用于执行异步任务
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        try {
            CompletableFuture.supplyAsync(() -> {
                        log.info("step1: build future");  // 第一步：创建初始值
                        return 1;  // 返回初始数据值 1
                    }, threadPool)
                    .handle((data, e) -> {  // 第二步：处理上一阶段的结果或异常
                        log.info("step2: process future");
                        int i = 10 / 0;  // 故意制造一个除零异常来测试异常处理
                        return data + 2;  // 正常情况下返回增加后的值
                    })
                    .handle((data, e) -> {  // 第三步：继续处理（即使上一步有异常也会执行）
                        log.info("step3: process future");
                        // 注意：此处的 data 可能是 null（如果上一步发生了异常）
                        return data + 3;  // 继续对数据进行处理
                    })
                    .whenComplete((v, e) -> {  // 最终完成回调 - 处理成功的情况
                        if (null == e) {
                            log.info("step4: complete future, result is {}", v);  // 输出最终计算结果
                        }
                    })
                    .exceptionally(e -> {  // 异常处理回调 - 处理失败的情况
                        log.info("step4: complete future, error is {}", e.getMessage());  // 记录错误信息
                        return null;  // 发生异常时返回 null
                    });
        } finally {
            threadPool.shutdown();  // 确保线程池被正确关闭
        }
    }


    /**
     * 演示 thenApply 方法的行为：
     * - thenApply 只有在前一个阶段正常完成时才会执行
     * - 如果前一个阶段抛出异常，则不会执行 thenApply，并且异常会向后传播
     * - 链式调用会在第一个异常处中断
     */
    public static void thenApply() {
        // 创建固定大小为3的线程池用于执行异步任务
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        try {
            CompletableFuture.supplyAsync(() -> {
                        SleepUtils.sleep(1000);  // 模拟耗时操作
                        log.info("step1: build future");  // 第一步：创建初始值
                        return 1;  // 返回初始数据值 1
                    }, threadPool)
                    .thenApply(data -> {  // 第二步：只有第一步成功才执行此转换
                        log.info("step2: process future");
                        return data + 2;  // 对数据进行处理
                    })
                    .thenApply(data -> {  // 第三步：只有第二步成功才执行此转换
                        log.info("step3: process future");
                        return data + 3;  // 继续对数据进行处理
                    })
                    .whenComplete((v, e) -> {  // 最终完成回调 - 处理成功的情况
                        if (null == e) {
                            log.info("step4: complete future, result is {}", v);  // 输出最终计算结果
                        }
                    })
                    .exceptionally(e -> {  // 异常处理回调 - 处理失败的情况
                        log.info("step4: complete future, error is {}", e.getMessage());  // 记录错误信息
                        return null;  // 发生异常时返回 null
                    });
        } finally {
            threadPool.shutdown();  // 确保线程池被正确关闭
        }
    }

}
