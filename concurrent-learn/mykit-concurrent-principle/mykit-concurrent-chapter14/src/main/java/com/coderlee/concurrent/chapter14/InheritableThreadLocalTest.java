package com.coderlee.concurrent.chapter14;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>类名称: InheritableThreadLocalTest</p>
 * <p>类描述: 本类用于演示 {@link InheritableThreadLocal} 的基本用法。</p>
 * <p>{@link InheritableThreadLocal} 是 {@link ThreadLocal} 的子类，允许子线程继承父线程的线程本地变量值。
 * 通过该示例，可以观察到主线程设置的线程本地变量如何传递到子线程中。</p>
 */
@Slf4j
public class InheritableThreadLocalTest {

    /**
     * 定义一个 {@link ThreadLocal} 变量，使用其子类 {@link InheritableThreadLocal} 实现线程间变量继承。
     * 子线程能够访问父线程设置的值。
     */
    private static final ThreadLocal<String> THREAD_LOCAL = new InheritableThreadLocal<>();

    /**
     * 程序入口方法。
     * <p>功能描述:</p>
     * <ol>
     *   <li>在主线程中设置线程本地变量值为 "coderlee"。</li>
     *   <li>启动一个新的子线程，在子线程中尝试获取并打印从主线程继承的线程本地变量值。</li>
     *   <li>在主线程中打印当前线程本地变量的值。</li>
     * </ol>
     *
     */
    public static void main(String[] args) {
        // 在主线程中设置线程本地变量值为 "coderlee"
        THREAD_LOCAL.set("coderlee");

        // 启动子线程，验证子线程是否能够继承主线程的线程本地变量
        new Thread(() -> log.info("在子线程中获取到的本地变量值为: {}", THREAD_LOCAL.get())).start();

        // 主线程打印自身线程本地变量的值
        log.info("在主线程中获取到的本地变量值为: {}", THREAD_LOCAL.get());
    }
}