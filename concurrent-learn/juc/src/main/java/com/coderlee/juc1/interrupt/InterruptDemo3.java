package com.coderlee.juc1.interrupt;

import com.coderlee.juc1.utils.SleepUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 演示线程中断机制及中断状态变化的示例类
 *
 * 本类展示了以下关键概念：
 * 1. 如何使用 Thread.interrupt() 方法发送中断信号
 * 2. 线程中断状态在不同阶段的变化情况
 * 3. 当线程执行完任务后，中断标志位会被自动清除
 * 4. 已终止的线程调用 isInterrupted() 方法总是返回 false
 *
 * 运行流程：
 * - 创建一个执行循环任务的线程 t1
 * - 在 t1 运行过程中发送中断请求
 * - 观察中断状态在不同时间点的变化
 * - 验证线程结束后中断标志位的行为
 */
@Slf4j
public class InterruptDemo3 {

    public static void main(String[] args) {
        // 创建一个名为 t1 的线程，该线程会执行一段循环任务。
        Thread t1 = new Thread(() -> {
            for (int i = 1; i <= 300; i++) {
                // 打印当前线程名称和循环计数器的值。
                log.info("{} ----- {}", Thread.currentThread().getName(), i);
            }
            // 循环结束后，打印当前线程的中断状态。
            log.info("{} 调用 interrupt() 后的中断标识 02 : {}", Thread.currentThread().getName(), Thread.currentThread().isInterrupted());
        }, "t1");

        // 启动线程 t1。
        t1.start();

        // 打印线程 t1 的初始中断标志位，默认为 false。
        log.info("t1的默认中断标志位: {}", t1.isInterrupted());

        // 主线程休眠 1 秒，确保 t1 线程有时间运行。
        SleepUtils.sleep(1);

        // 中断线程 t1，并打印中断请求后的中断标志位。
        t1.interrupt();
        log.info("{} 对t1进行了中断请求，t1线程的中断标识 01 : {}", Thread.currentThread().getName(), t1.isInterrupted());

        // 主线程再次休眠 2 秒，观察 t1 线程在稍后时刻的中断标志位。
        SleepUtils.sleep(2000);
        // t1线程已经不活动了，不会产生任何影响，因此是false
        // t1.interrupt();
        log.info("2s后，t1线程的中断标识03 : {}", t1.isInterrupted());
    }
}