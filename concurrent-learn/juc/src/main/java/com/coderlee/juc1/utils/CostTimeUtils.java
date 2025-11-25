package com.coderlee.juc1.utils;

import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Consumer;

/**
 * 执行时间统计工具类
 * 提供通用的方法来测量代码执行时间
 */
@Slf4j
public class CostTimeUtils {
    /**
     * 计算并记录指定操作的执行时间
     *
     * @param consumer 要执行的操作，接收Void参数的Consumer函数式接口
     * @param msg 操作描述信息，用于日志输出
     */
    public static void calcCostTime(Consumer<Void> consumer, String msg) {
        // 记录操作开始时间
        log.info("{} start...", msg);
        Instant start = Instant.now();
        // 执行传入的操作
        consumer.accept(null);
        // 记录操作结束时间并计算耗时
        log.info("{} end, cost: {} ms", msg, Duration.between(start, Instant.now()).toMillis());
    }
}

