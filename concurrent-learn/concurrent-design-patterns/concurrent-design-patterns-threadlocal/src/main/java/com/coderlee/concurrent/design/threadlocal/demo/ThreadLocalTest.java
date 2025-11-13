package com.coderlee.concurrent.design.threadlocal.demo;

import lombok.extern.slf4j.Slf4j;

/**
 * 演示 ThreadLocal 基本用法的测试类。
 * 展示了如何在线程之间隔离变量，每个线程设置并获取自己的本地变量值。
 *
 * @see ThreadLocal
 */
@Slf4j
public class ThreadLocalTest {

    /**
     * 定义一个 ThreadLocal 变量，默认初始值为 null。
     */
    private static ThreadLocal<String> threadLocal = ThreadLocal.withInitial(() -> null);

    /**
     * 主方法，启动两个线程演示 ThreadLocal 的基本使用。
     * 每个线程都会设置并打印自己线程内的变量值。
     *
     * @param args 启动参数
     */
    public static void main(String[] args) {
        // 创建线程 A
        Thread a = new Thread(() -> {
            // 设置当前线程的本地变量值
            threadLocal.set("thread-a: " + Thread.currentThread().getName());
            // 获取并打印当前线程的本地变量值
            log.info("线程A本地变量中的值为: {}", threadLocal.get());
        });

        // 创建线程 B
        Thread b = new Thread(() -> {
            // 设置当前线程的本地变量值
            threadLocal.set("thread-b: " + Thread.currentThread().getName());
            // 获取并打印当前线程的本地变量值
            log.info("线程B本地变量中的值为: {}", threadLocal.get());
        });

        // 启动线程 A 和 B
        a.start();
        b.start();
    }
}
