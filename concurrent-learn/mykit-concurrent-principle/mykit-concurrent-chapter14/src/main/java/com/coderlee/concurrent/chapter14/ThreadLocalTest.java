package com.coderlee.concurrent.chapter14;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>演示 {@link ThreadLocal} 的使用及其线程隔离特性的测试类。</p>
 * 
 * <p>此类通过创建两个线程（Thread-A 和 Thread-B），分别设置和获取 {@code ThreadLocal} 中的值，
 * 展示了每个线程拥有独立的变量副本，并验证了调用 {@link ThreadLocal#remove()} 方法后变量的清除效果。</p>
 * <p>
 * 结论：
 * Thread-A线程和Thread-B线程存储在ThreadLocal中的变量互不干扰，
 * Thread-A线程中存储的本地变量只能由Thread-A线程访问，
 * Thread-B线程中存储的本地变量只能由Thread-B线程访问。
 * </p>
 */
@Slf4j
public class ThreadLocalTest {

    /**
     * 定义一个静态的 {@link ThreadLocal} 变量，用于存储线程本地的字符串值。
     * 每个线程对该变量的访问和修改是独立的，互不干扰。
     */
    private static final ThreadLocal<String> THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 程序入口方法，用于启动两个线程并测试 {@link ThreadLocal} 的行为。
     *
     * <p>具体测试内容包括：</p>
     * <ul>
     *   <li>线程 A 设置本地变量值，并验证未删除时的值。</li>
     *   <li>线程 B 设置本地变量值，删除后验证本地变量是否为空。</li>
     * </ul>
     *
     * @param args 命令行参数（未使用）。
     */
    public static void main(String[] args) {
        // 创建线程 A，设置本地变量并打印其值
        Thread threadA = new Thread(() -> {
            THREAD_LOCAL.set("ThreadA:" + Thread.currentThread().getName());
            log.info("{} 本地变量中的值为: {}", Thread.currentThread().getName(), THREAD_LOCAL.get());

            // 再次打印本地变量值，验证未删除时的行为
            log.info("{} 未删除本地变量，本地变量中的值为: {}", Thread.currentThread().getName(), THREAD_LOCAL.get());
        }, "Thread-A");

        // 创建线程 B，设置本地变量并删除后验证其值
        Thread threadB = new Thread(() -> {
            THREAD_LOCAL.set("ThreadB:" + Thread.currentThread().getName());
            log.info("{} 本地变量中的值为: {}", Thread.currentThread().getName(), THREAD_LOCAL.get());

            // 删除本地变量并验证删除后的值
            THREAD_LOCAL.remove();
            log.info("{} 删除本地变量后，本地变量中的值为: {}", Thread.currentThread().getName(), THREAD_LOCAL.get());
        }, "Thread-B");

        // 启动线程 A 和线程 B
        threadA.start();
        threadB.start();
    }
}