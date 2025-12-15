package com.coderlee.juc1.interrupt;

import com.coderlee.juc1.utils.SleepUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 演示使用AtomicBoolean作为线程中断标志的示例
 * 相比于volatile boolean，AtomicBoolean提供了更多的原子操作方法
 */
@Slf4j
public class InterruptDemo1 {
    // 使用AtomicBoolean确保线程安全，提供原子性的读写操作
    static AtomicBoolean atomicBoolean = new AtomicBoolean(false);

    public static void main(String[] args) {
        // 创建并启动第一个工作线程t1
        new Thread(() -> {
            // 循环执行任务直到收到中断信号
            while (true) {
                // 检查原子布尔值状态，判断是否需要停止
                if (atomicBoolean.get()) {
                    // 收到中断信号，记录日志并退出循环
                    log.info("{} atomicBoolean 被修改为true，程序停止", Thread.currentThread().getName());
                    break;
                }
                // 继续执行任务
                log.info("{} is running...", Thread.currentThread().getName());
            }
        }, "t1").start();

        // 主线程短暂休眠，让t1线程充分运行
        SleepUtils.sleep(5);

        // 创建并启动第二个控制线程t2，用于发送中断信号
        new Thread(() -> {
            // 原子性地设置boolean值为true，通知t1线程停止
            atomicBoolean.set(true);
            log.info("{} 设置atomicBoolean为true", Thread.currentThread().getName());
        }, "t2").start();
    }
}
