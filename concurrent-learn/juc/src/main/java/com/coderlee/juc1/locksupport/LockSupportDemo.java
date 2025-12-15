package com.coderlee.juc1.locksupport;

import com.coderlee.juc1.utils.SleepUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

/**
 * LockSupport 使用示例类
 * 演示了 LockSupport 的基本使用方法及其特性
 */
@Slf4j
public class LockSupportDemo {

    /**
     * 主方法，程序入口
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 执行第一个演示方法
        demo1();
        // 执行第二个演示方法
        demo2();
        // 执行第三个演示方法
        demo3();
    }

    /**
     * 演示 LockSupport 基本使用方法
     * t1线程先阻塞，然后由t2线程唤醒
     */
    public static void demo1() {
        // 创建线程t1
        Thread t1 = new Thread(() -> {
            log.info("{} ---- come in", Thread.currentThread().getName());
            // 阻塞当前线程
            LockSupport.park();
            log.info("{} 被唤醒", Thread.currentThread().getName());
        }, "t1");
        t1.start();

        // 主线程休眠1秒，确保t1先执行
        SleepUtils.sleep(1);

        // 创建线程t2用于唤醒t1
        new Thread(() -> {
            log.info("{} ---- come in", Thread.currentThread().getName());
            log.info("{} 发出唤醒[t1]通知", Thread.currentThread().getName());
            // 唤醒t1线程
            LockSupport.unpark(t1);
        }, "t2").start();
    }

    /**
     * 演示先unpark后park的情况
     * 展示了LockSupport的许可特性：即使先调用unpark，后续的park也不会阻塞
     */
    public static void demo2() {
        // 创建线程t1，延迟3秒后再执行park操作
        Thread t1 = new Thread(() -> {
            SleepUtils.sleep(3);
            log.info("{} ---- come in", Thread.currentThread().getName());
            // 此时可能已经被unpark过，不会阻塞
            LockSupport.park();
            log.info("{} 被唤醒", Thread.currentThread().getName());
        }, "t1");
        t1.start();

        // 创建线程t2，立即执行unpark操作
        new Thread(() -> {
            log.info("{} ---- come in", Thread.currentThread().getName());
            log.info("{} 发出唤醒[t1]通知", Thread.currentThread().getName());
            // 提前唤醒t1（发放许可）
            LockSupport.unpark(t1);
        }, "t2").start();
    }

    /**
     * 演示LockSupport的许可特性
     * 一个park消耗一个许可，多次unpark只保留一个许可
     */
    public static void demo3() {
        // 创建线程t1
        Thread t1 = new Thread(() -> {
            log.info("{} ---- come in", Thread.currentThread().getName());
            // 第一次park会消耗许可，线程阻塞
            LockSupport.park();
            // 第二次park由于没有许可，线程再次阻塞
            LockSupport.park();
            log.info("{} 被唤醒", Thread.currentThread().getName());
        }, "t1");
        t1.start();

        // 主线程休眠1秒，确保t1先执行
        SleepUtils.sleep(1);

        // 创建线程t2，多次调用unpark
        new Thread(() -> {
            log.info("{} ---- come in", Thread.currentThread().getName());
            log.info("{} 发出唤醒[t1]通知", Thread.currentThread().getName());
            // 多次unpark只会积累一个许可
            LockSupport.unpark(t1);
            LockSupport.unpark(t1);
            LockSupport.unpark(t1);
            LockSupport.unpark(t1);
        }, "t2").start();
    }
}
