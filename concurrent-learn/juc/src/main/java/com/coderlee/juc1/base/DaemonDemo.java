package com.coderlee.juc1.base;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 守护线程演示类
 * 该类演示了守护线程和用户线程的区别：
 * - 用户线程：不会随着主线程的结束而结束，JVM会等待所有用户线程执行完毕才退出
 * - 守护线程：会随着主线程的结束而结束，当所有用户线程结束后，JVM直接退出，不等待守护线程
 * 可以通过注释line#36 `t1.setDaemon(true)` 代码来观察用户线程与守护线程的区别
 * 注意 setDaemon 方法必须在start方法之前调用，否则会抛出IllegalThreadStateException异常
 * 详见 docs/juc1/01/用户线程和守护线程.md
 */
@Slf4j
public class DaemonDemo {
    /**
     * 主方法，演示守护线程的特性
     *
     * 创建一个守护线程t1，该线程会无限循环运行
     * 主线程睡眠3秒后结束，守护线程t1也会随之结束
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 创建名为"t1"的线程，该线程会持续运行无限循环
        Thread t1 = new Thread(() -> {
            // 输出线程信息，判断是守护线程还是用户线程
            log.info("{} 开始运行, {}", Thread.currentThread().getName(), Thread.currentThread().isDaemon() ? "守护线程" : "用户线程");
            // 无限循环，模拟持续运行的任务
            while (true) {
            }
        }, "t1");

        // 将t1线程设置为守护线程
        // 守护线程会随着主线程的结束而结束
        t1.setDaemon(true);

        // 启动守护线程
        t1.start();

        try {
            // 主线程睡眠3秒，给守护线程一些运行时间
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 主线程运行结束，JVM会直接退出，不会等待守护线程t1
        log.info("{} 运行结束", Thread.currentThread().getName());
    }
}
