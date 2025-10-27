package com.coderlee.concurrent.chapter09;

import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

/**
 * 该类用于演示非可中断锁的行为。
 * 主要测试在多线程环境下，当线程尝试获取同步锁时，
 * 即使线程被中断，也无法立即响应中断的情况。
 */
@Slf4j
public class NonInterruptiblyLockTest {

    /**
     * 程序入口方法，创建两个线程分别调用 [lock] 方法。
     * 通过主线程的休眠和中断操作，观察线程在获取锁时对中断的响应行为。
     *
     * @param args 命令行参数（未使用）
     */
    public static void main(String[] args) {
        NonInterruptiblyLockTest test = new NonInterruptiblyLockTest();

        // 创建两个线程，分别调用 [lock] 方法
        Thread threadA = new Thread(() -> test.lock(), "threadA");
        Thread threadB = new Thread(() -> test.lock(), "threadB");

        // 启动线程 A 和线程 B
        threadA.start();
        threadB.start();

        try {
            // 主线程短暂休眠，确保线程 A 和线程 B 开始执行
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            log.error("主线程休眠时被中断: {}", e.getMessage());
        }

        // 中断线程 A 和线程 B
        threadA.interrupt();
        threadB.interrupt();

        try {
            // 主线程再次休眠，观察线程对中断的响应情况
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            log.error("主线程休眠时被中断: {}", e.getMessage());
        }
    }

    /**
     * 一个同步方法，用于模拟线程抢占锁的行为。
     * 如果线程在获取锁后被中断，会记录日志并继续执行。
     */
    public synchronized void lock() {
        try {
            // 记录当前线程成功抢占锁的日志
            log.info("{} 抢占锁成功", Thread.currentThread().getName());

            // 检查当前线程是否已被中断
            if (Thread.currentThread().isInterrupted()) {
                log.info("{} 线程被中断", Thread.currentThread().getName());
            }

            // 模拟线程持有锁期间的业务逻辑处理
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            // 捕获中断异常，记录日志
            log.info("{} 抢占锁被中断", Thread.currentThread().getName());
        }
    }
}