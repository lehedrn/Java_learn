package com.coderlee.concurrent.design.half.sync.async.wrong;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;

/**
 * 错误示例的测试类。
 * <p>
 * 展示了未使用异步机制时的整体执行效率。
 * </p>
 */
@Slf4j
public class WrongPayTest {

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
