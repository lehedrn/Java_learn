package com.coderlee.concurrent.design.promise.right;

import com.coderlee.concurrent.design.promise.common.domain.Integral;
import com.coderlee.concurrent.design.promise.common.service.IntegralService;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * Promise模式实现类，用于异步执行积分发送任务。
 *
 * <p>该类通过 {@link FutureTask} 实现异步执行任务，并返回一个 {@link Future} 对象，
 * 调用方可以通过该对象获取任务执行结果或检查任务状态。</p>
 *
 * @see Future
 * @see FutureTask
 */
@Slf4j
public class Promisor {

    /**
     * 异步执行积分发送任务。
     *
     * <p>此方法会创建一个新的线程来执行积分发送逻辑，并立即返回一个 {@link Future} 对象。
     * 调用方可以通过该对象获取执行结果或检查任务是否完成。</p>
     *
     * @param integralService 积分服务实例，用于实际执行积分发送操作
     * @return 返回一个 {@link Future} 对象，可用于获取任务执行结果
     * @see IntegralService
     * @see Future
     */
    public Future<Object> compute(IntegralService integralService) {
        // 创建FutureTask包装积分发送任务
        FutureTask<Object> futureTask = new FutureTask<>(() -> {
            // 记录任务开始时间
            log.info("发送积分任务开始");
            Instant start = Instant.now();

            // 执行积分发送操作
            Integral integral = integralService.sendIntegral();

            // 记录任务结束时间和耗时
            log.info("发送积分任务结束, 当前发送积分耗时: {}", Duration.between(start, Instant.now()).toMillis());
            return integral;
        });

        // 启动新线程执行任务
        new Thread(futureTask).start();

        // 返回Future对象供调用方使用
        return futureTask;
    }
}
