package com.coderlee.juc1.interrupt;

import java.util.concurrent.TimeUnit;

import com.coderlee.juc1.utils.SleepUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 演示线程在sleep状态下被中断的处理机制
 *
 * 本类重点展示了：
 * 1. 线程在sleep期间被中断时会抛出InterruptedException
 * 2. 如何正确处理InterruptedException异常
 * 3. 中断状态的传递和恢复机制
 */
@Slf4j
public class InterruptDemo4 {

    public static void main(String[] args) {
        // 创建工作线程t1，该线程会周期性地睡眠和执行任务
        Thread t1 = new Thread(() -> {
            while (true) {
                // 检查线程是否被中断，如果中断则退出循环
                if (Thread.currentThread().isInterrupted()) {
                    log.info("{} 接收到中断请求，程序停止", Thread.currentThread().getName());
                    break;
                }

                try {
                    // 线程睡眠200毫秒，在此期间可能被其他线程中断
                    TimeUnit.MILLISECONDS.sleep(200);
                } catch (InterruptedException e) {
                    // 当线程在sleep期间被中断时，会抛出InterruptedException
                    log.error("sleep方法抛出异常 {}", e.getMessage(), e);

                    // 重要：捕获异常后需要手动恢复中断状态
                    // 因为抛出InterruptedException时会自动清除中断标志位
                    Thread.currentThread().interrupt();
                    log.info("{} 接收到异常，手动调用 interrupt 方法设置中断标志位", Thread.currentThread().getName());
                }

                // 执行任务逻辑
                log.info("{} is running...", Thread.currentThread().getName());
            }
        }, "t1");

        // 启动工作线程
        t1.start();

        // 主线程短暂睡眠，确保t1有足够时间开始运行
        SleepUtils.sleep(1);

        // 创建控制线程t2，用于向t1发送中断信号
        new Thread(() -> {
            // 向t1线程发送中断请求
            t1.interrupt();
            log.info("{} 对t1进行了中断请求", Thread.currentThread().getName());
        }, "t2").start();
    }
}
