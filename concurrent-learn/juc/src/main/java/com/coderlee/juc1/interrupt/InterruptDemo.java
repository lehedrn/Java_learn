package com.coderlee.juc1.interrupt;

import com.coderlee.juc1.utils.SleepUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 演示使用volatile变量作为线程中断标志的示例
 */
@Slf4j
public class InterruptDemo {
    // 使用volatile确保多线程间的可见性，当一个线程修改此值时，其他线程能立即看到更新
    static volatile boolean isStop = false;

    public static void main(String[] args) {
        // 创建并启动第一个线程t1，用于执行主要任务逻辑
        new Thread(() -> {
            // 无限循环执行任务
            while (true) {
                // 检查中断标志是否被设置为true
                if (isStop) {
                    // 如果被中断，则记录日志并退出循环
                    log.info("{} isStop 被修改为true，程序停止", Thread.currentThread().getName());
                    break;
                }
                // 执行任务：打印运行状态
                log.info("{} is running...", Thread.currentThread().getName());
            }
        }, "t1").start();

        // 主线程休眠20毫秒，确保t1有足够时间开始运行
        SleepUtils.sleep(20);

        // 创建并启动第二个线程t2，用于发送中断信号
        new Thread(() -> {
            // 设置中断标志为true，通知t1线程停止运行
            isStop = true;
            log.info("{} 设置isStop为true", Thread.currentThread().getName());
        }, "t2").start();
    }
}
