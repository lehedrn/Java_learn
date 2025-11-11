package com.coderlee.concurrent.design.promise.wrong;

import java.time.Duration;
import java.time.Instant;

import com.coderlee.concurrent.design.promise.common.service.CouponService;
import com.coderlee.concurrent.design.promise.common.service.IntegralService;
import com.coderlee.concurrent.design.promise.common.service.impl.CouponServiceImpl;
import com.coderlee.concurrent.design.promise.common.service.impl.IntegralServiceImpl;

import lombok.extern.slf4j.Slf4j;

/**
 * Promise模式错误实现示例类
 * <p>
 * 该类演示了在处理用户支付后的积分和优惠券发送场景中，错误的并发处理方式。
 * 虽然创建了线程来处理积分发送，但通过 {@link Thread#join()} 方法阻塞了主线程，
 * 导致优惠券发送必须等待积分发送完成后才能开始，没有真正实现并发处理。
 * </p>
 * @author coderlee
 * @see IntegralService 积分服务接口
 * @see CouponService 优惠券服务接口
 * @see IntegralServiceImpl 积分服务实现类
 * @see CouponServiceImpl 优惠券服务实现类
 *
 */
@Slf4j
public class PromiseWrongTest {

    /**
     * 主方法，程序入口
     *
     * 模拟用户支付成功后，需要发送积分和优惠券的业务场景。
     * 展示了错误的并发处理方式：虽然使用了多线程，但由于使用了 {@link Thread#join()}，
     * 实际上是串行执行，没有发挥并发优势。
     *
     * @param args 命令行参数
     * @see IntegralService#sendIntegral() 积分发送方法
     * @see CouponService#sendCoupon() 优惠券发送方法
     */
    public static void main(String[] args) {
        // 记录用户支付成功的日志
        log.info("用户支付订单成功");

        // 初始化积分服务和优惠券服务的实现类
        IntegralService integralService = new IntegralServiceImpl();
        CouponService couponService = new CouponServiceImpl();

        // 记录整个操作的开始时间
        Instant start = Instant.now();

        // 创建积分发送线程
        Thread integralThread = new Thread(() -> {
            // 记录积分发送任务开始的日志
            log.info("发送积分任务开始");
            // 记录积分发送的开始时间
            Instant integralStart = Instant.now();
            // 执行积分发送操作
            integralService.sendIntegral();
            // 记录积分发送任务结束的日志，并输出耗时
            log.info("发送积分任务结束,当前发送积分耗时: {}", Duration.between(integralStart, Instant.now()).toMillis());
        });

        // 启动积分发送线程
        integralThread.start();

        try {
            // 等待积分发送线程执行完成，这会导致主线程阻塞
            // 这是错误的做法，因为优惠券发送必须等待积分发送完成后才能开始
            integralThread.join();
        } catch (InterruptedException e) {
            // 处理线程中断异常
            log.error("线程中断异常", e);
        }

        // 记录优惠券发送任务开始的日志
        log.info("发送优惠券任务开始");

        // 记录优惠券发送的开始时间
        Instant couponStart = Instant.now();

        // 执行优惠券发送操作
        couponService.sendCoupon();

        // 记录优惠券发送任务结束的日志，并输出耗时
        log.info("发送优惠券任务结束,当前发送优惠券耗时: {}", Duration.between(couponStart, Instant.now()).toMillis());

        // 记录整个操作完成的日志，并输出总耗时
        log.info("成功向用户发送积分和优惠券, 总共耗时: {}", Duration.between(start, Instant.now()).toMillis());
    }
}
