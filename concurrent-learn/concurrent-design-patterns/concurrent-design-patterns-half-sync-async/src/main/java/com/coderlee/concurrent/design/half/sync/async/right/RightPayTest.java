package com.coderlee.concurrent.design.half.sync.async.right;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;

/**
 * 测试类，验证正确的支付服务实现。
 * <p>
 * 主要目的是演示如何使用半同步/半异步模式提高程序性能。
 * </p>
 */
@Slf4j
public class RightPayTest {

    /**
     * 程序入口点。
     * <p>
     * 创建支付服务实例并执行支付操作，记录整个过程的时间消耗。
     * </p>
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        log.info("执行整体任务开始");
        Instant start = Instant.now();
        PayService payService = new PayServiceImpl();
        payService.pay(new BigDecimal("198.76")); // 执行支付
        log.info("执行整体任务结束, 耗时: {} ms", Duration.between(start, Instant.now()).toMillis());
    }
}
