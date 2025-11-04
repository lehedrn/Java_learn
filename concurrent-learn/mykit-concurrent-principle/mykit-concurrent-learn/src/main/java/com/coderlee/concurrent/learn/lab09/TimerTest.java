package com.coderlee.concurrent.learn.lab09;

import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

/**
 * TimerTest 类用于演示 Java 中 `Timer` 和 `TimerTask` 的基本用法。
 * 通过该类，展示了如何以固定频率调度任务，并在指定时间后取消定时器。
 */
@Slf4j
public class TimerTest {

    /**
     * 主方法，程序的入口点。
     * <p>
     * 该方法创建一个 `Timer` 实例，并使用 `scheduleAtFixedRate` 方法以固定频率调度任务。
     * 任务每隔 1 秒执行一次，输出当前时间到日志中。主线程会在 10 秒后取消定时器。
     *
     * @param args 命令行参数（未使用）。
     */
    public static void main(String[] args) {
        // 创建一个 Timer 实例，用于管理任务调度
        Timer timer = new Timer();

        // 使用 scheduleAtFixedRate 方法调度任务，初始延迟 1 秒，后续每隔 1 秒执行一次
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // 在任务执行时记录当前时间
                log.info("测试Timer类, 时间: {}", LocalDateTime.now());
            }
        }, 1000, 1000); // 初始延迟 1000 毫秒，后续间隔 1000 毫秒

        try {
            // 主线程休眠 10 秒，模拟程序运行期间的任务调度
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            // 捕获中断异常并打印堆栈信息
            e.printStackTrace();
        }

        // 取消定时器，停止所有任务调度
        timer.cancel();
    }
}