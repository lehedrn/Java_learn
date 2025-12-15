package com.coderlee.juc1.interrupt;

import com.coderlee.juc1.utils.SleepUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 演示使用Java标准线程中断机制的示例
 * 通过Thread.interrupt()方法发送中断信号，Thread.isInterrupted()检查中断状态
 */
@Slf4j
public class InterruptDemo2 {
    public static void main(String[] args) {
        // 创建并启动工作线程t1
        Thread t1 = new Thread(() -> {
            // 循环执行任务直到收到中断信号
            while (true) {
                // 检查当前线程是否被中断
                if (Thread.currentThread().isInterrupted()) {
                    // 接收到中断信号，记录日志并退出循环
                    log.info("{} 接收到中断请求，程序停止", Thread.currentThread().getName());
                    break;
                }
                // 继续执行任务
                log.info("{} is running...", Thread.currentThread().getName());
            }
        }, "t1");
        t1.start();
        
        // 记录t1线程初始的中断状态（应为false）
        log.info("t1的默认中断标志位: {}", t1.isInterrupted());
        
        // 主线程短暂休眠，让t1充分运行
        SleepUtils.sleep(10);
        
        // 创建并启动控制线程t2，用于发送中断信号
        new Thread(() -> {
            // 对t1线程发送中断信号
            t1.interrupt();
            log.info("{} 对t1进行了中断请求", Thread.currentThread().getName());
            // 记录中断后的标志位状态
            log.info("t1的默认中断标志位: {}", t1.isInterrupted());
        }, "t2").start();
    }
}
