package com.coderlee.concurrent.design.promise.right;

import com.coderlee.concurrent.design.promise.common.domain.Integral;
import com.coderlee.concurrent.design.promise.common.service.CouponService;
import com.coderlee.concurrent.design.promise.common.service.impl.CouponServiceImpl;
import com.coderlee.concurrent.design.promise.common.service.impl.IntegralServiceImpl;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Promise模式正确使用示例类。
 *
 * <p>演示了如何使用 {@link Promisor} 类来异步执行积分发送任务，
 * 同时并行执行其他任务（如发送优惠券），以提高整体执行效率。</p>
 *
 * @see Promisor
 */
@Slf4j
public class PromiseRightTest {

    /**
     * 主方法，程序入口点。
     *
     * <p>演示了Promise模式的基本使用流程：
     * <ol>
     *   <li>启动异步积分发送任务</li>
     *   <li>并行执行优惠券发送任务</li>
     *   <li>等待积分发送任务完成并获取结果</li>
     * </ol>
     * </p>
     *
     * @param args 命令行参数
     * @throws RuntimeException 当获取异步任务结果失败时抛出
     */
    public static void main(String[] args) {
        // 记录整个流程开始时间
        log.info("用户支付订单成功");
        Instant start = Instant.now();

        // 启动异步积分发送任务
        Future<Object> promise = new Promisor().compute(new IntegralServiceImpl());

        // 并行执行优惠券发送任务
        log.info("发送优惠券任务开始");
        Instant couponStart = Instant.now();
        CouponService couponService = new CouponServiceImpl();
        couponService.sendCoupon();
        log.info("发送优惠券任务结束, 当前发送优惠券耗时: {}", Duration.between(couponStart, Instant.now()).toMillis());

        // 检查积分发送任务是否已完成
        if (promise.isDone()) {
            log.info("发送积分任务已经执行完毕");
        } else {
            log.info("发送积分任务未执行完毕");
        }

        // 等待并获取积分发送任务的结果
        try {
            Integral integral = (Integral) promise.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        // 输出总耗时
        log.info("成功向用户发送积分和优惠券, 总共耗时: {}", Duration.between(start, Instant.now()).toMillis());
    }
}
