package com.coderlee.concurrent.chapter07;

import java.util.function.Consumer;
import java.util.stream.IntStream;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>该类用于演示静态方法的线程安全性。
 * 通过使用 {@code synchronized} 关键字修饰静态方法，确保多个线程对共享静态变量的安全访问。</p>
 * 
 * <p>具体场景是：两个线程并发执行对静态变量 {@link #count} 的递减操作，
 * 测试在加锁的情况下是否能够正确地完成线程安全的操作。</p>
 */
@Slf4j
public class SynchronizedStaticTest {

    /**
     * 程序入口点。
     * <p>创建两个线程，分别调用 {@link #decrementCount()} 方法对静态变量 {@link #count} 进行递减操作。
     * 最终打印递减后的结果，验证线程安全性。</p>
     *
     * @param args 命令行参数（未使用）
     */
    public static void main(String[] args) {
        // 调用 execute 方法，传入递减操作的逻辑，并获取最终的 count 值
        Long count = SynchronizedStaticTest.execute(i -> SynchronizedStaticTest.decrementCount());
        log.info("count: {}", count); // 打印最终的 count 值
    }

    /**
     * 共享的静态变量，初始值为 2000。
     * 该变量会被多个线程并发修改。
     */
    private static Long count = 2000L;

    /**
     * 线程安全的静态方法，用于递减静态变量 {@link #count} 的值。
     * <p>通过 {@code synchronized} 关键字修饰静态方法，确保同一时间只有一个线程可以执行该方法。</p>
     */
    public static synchronized void decrementCount() {
        count--; // 对静态变量进行递减操作
    }

    /**
     * 执行多线程任务并返回最终的静态变量值。
     * <p>该方法会启动两个线程，每个线程都会调用传入的消费者逻辑对静态变量进行操作。
     * 线程执行完毕后，返回静态变量的最终值。</p>
     *
     * @param consumer 每个线程需要执行的逻辑，通常是一个对静态变量的操作
     * @return 静态变量 {@link #count} 的最终值
     */
    public static Long execute(Consumer<Void> consumer) {
        // 创建第一个线程，执行 1000 次消费者逻辑
        Thread t1 = new Thread(() -> IntStream.range(0, 1000).forEach(i -> consumer.accept(null)));
        // 创建第二个线程，执行 1000 次消费者逻辑
        Thread t2 = new Thread(() -> IntStream.range(0, 1000).forEach(i -> consumer.accept(null)));
        t1.start(); // 启动第一个线程
        t2.start(); // 启动第二个线程
        try {
            t1.join(); // 等待第一个线程执行完毕
            t2.join(); // 等待第二个线程执行完毕
        } catch (InterruptedException e) {
            e.printStackTrace(); // 捕获并打印线程中断异常
        }
        return count; // 返回静态变量的最终值
    }
}