package com.coderlee.concurrent.design.half.sync.async.right;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 支付服务实现类。
 * <p>
 * 实现了支付逻辑，并利用异步任务机制发送短信通知。
 * </p>
 */
@Slf4j
public class PayServiceImpl implements PayService {

    /**
     * 简单异步任务实例，用于发送短信。
     */
    private SimpleAsyncTask simpleAsyncTask;

    /**
     * 构造函数，初始化异步任务实例。
     */
    public PayServiceImpl() {
        simpleAsyncTask = new SimpleAsyncTask();
    }

    /**
     * 执行支付操作。
     * <p>
     * 先模拟支付流程，然后启动异步任务发送短信通知。
     * </p>
     *
     * @param bigDecimal 支付金额
     */
    @Override
    public void pay(BigDecimal bigDecimal) {
        log.info("执行支付逻辑开始");
        Instant start = Instant.now();
        try {
            TimeUnit.SECONDS.sleep(2); // 模拟支付耗时
        } catch (InterruptedException e) {
            log.error("支付逻辑发生异常", e);
        }
        log.info("支付逻辑执行结束, 支付的金额为: {}, 耗时: {} ms", bigDecimal, Duration.between(start, Instant.now()).toMillis());

        log.info("执行发送短信的逻辑开始");
        Instant smsStart = Instant.now();
        Future<String> execute = simpleAsyncTask.execute("13950601366"); // 发送短信
        try {
            log.info("发送短信的逻辑执行结果: {}", execute.get()); // 获取短信发送结果
        } catch (Exception e) {
            log.error("发送短信的逻辑执行异常", e);
        }
        log.info("发送短信的逻辑执行结束, 耗时: {} ms", Duration.between(smsStart, Instant.now()).toMillis());
    }
}

