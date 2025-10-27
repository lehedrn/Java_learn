package com.coderlee.concurrent.chapter09;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import lombok.extern.slf4j.Slf4j;

/**
 * LockSupportTest 类用于演示 Java 并发工具类 `LockSupport` 的基本用法。
 * <p>
 * 该类通过一个线程的阻塞与唤醒操作，展示 `LockSupport.park()` 和 `LockSupport.unpark(Thread)` 的功能。
 * 具体实现中，主线程启动一个子线程并让其阻塞，随后通过 `LockSupport.unpark(Thread)` 唤醒该线程。
 */
@Slf4j
public class LockSupportTest {

    /**
     * 程序入口方法。
     * <p>
     * 主线程创建并启动一个子线程，使其执行 [parkThread()] 方法进入阻塞状态。
     * 随后主线程休眠一定时间后调用 `LockSupport.unpark(Thread)` 唤醒子线程。
     *
     */
    public static void main(String[] args) {
        LockSupportTest test = new LockSupportTest();

        // 创建并启动子线程，执行 [parkThread()] 方法
        Thread thread = new Thread(() -> test.parkThread(), "LockSupport-Thread");
        thread.start();

        try {
            // 主线程休眠 200 毫秒，确保子线程有足够时间进入阻塞状态
            TimeUnit.MILLISECONDS.sleep(200);
        } catch (InterruptedException e) {
            // 捕获中断异常并打印堆栈信息
            e.printStackTrace();
        }

        // 打印日志，表明主线程开始唤醒子线程
        log.info("{} 开始唤醒 {} 线程", Thread.currentThread().getName(), thread.getName());
        LockSupport.unpark(thread); // 唤醒指定线程

        // 打印日志，表明主线程完成唤醒操作
        log.info("{} 结束唤醒 {} 线程", Thread.currentThread().getName(), thread.getName());
    }

    /**
     * 子线程执行的方法，用于演示线程的阻塞与唤醒。
     * <p>
     * 调用 `LockSupport.park()` 使当前线程进入阻塞状态，直到被其他线程通过 `LockSupport.unpark(Thread)` 唤醒。
     */
    public void parkThread() {
        // 打印日志，表明当前线程开始阻塞
        log.info("{} 开始堵塞", Thread.currentThread().getName());

        // 调用 `LockSupport.park()` 使当前线程阻塞
        LockSupport.park();

        // 打印日志，表明当前线程结束阻塞
        log.info("{} 结束堵塞", Thread.currentThread().getName());
    }
}
