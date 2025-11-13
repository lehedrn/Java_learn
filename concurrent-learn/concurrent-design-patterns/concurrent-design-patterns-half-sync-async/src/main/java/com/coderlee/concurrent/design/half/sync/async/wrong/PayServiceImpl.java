package com.coderlee.concurrent.design.half.sync.async.wrong;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

/**
 * 错误示例中的支付服务实现类。
 * <p>
 * 在同一个线程中顺序执行支付和发送短信两个步骤，
 * 导致总耗时增加，用户体验下降。
 * </p>
 */
@Slf4j
public class PayServiceImpl implements PayService {

    /**
     * 执行支付操作。
     * <p>
     * 包括支付逻辑和发送短信两部分，都在主线程中顺序执行。
     * </p>
     *
     * @param money 支付金额
     */
    @Override
    public void pay(BigDecimal money) {
        log.info("执行支付逻辑开始");
        Instant start = Instant.now();
        try {
            TimeUnit.SECONDS.sleep(2); // 模拟支付耗时
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("支付逻辑执行结束, 支付的金额为: {}, 耗时: {} ms", money, Duration.between(start, Instant.now()).toMillis());
        log.info("执行发送短信的逻辑开始");
        Instant smsStart = Instant.now();
        try {
            TimeUnit.SECONDS.sleep(3); // 模拟发送短信耗时
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("发送短信的逻辑执行结束, 耗时: {} ms", Duration.between(smsStart, Instant.now()).toMillis());
    }
}

