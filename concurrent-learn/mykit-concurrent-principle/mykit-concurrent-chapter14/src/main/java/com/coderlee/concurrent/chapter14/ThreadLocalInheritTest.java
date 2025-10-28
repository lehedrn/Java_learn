package com.coderlee.concurrent.chapter14;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>类名：ThreadLocalInheritTest</p>
 * <p>描述：该类用于演示 ThreadLocal 在父子线程之间的变量隔离特性。</p>
 * 
 * <p>主要功能：</p>
 * <ul>
 *   <li>在主线程中设置 ThreadLocal 变量，并验证其值是否能够被子线程继承。</li>
 *   <li>通过日志输出，展示 ThreadLocal 的作用域仅限于当前线程。</li>
 * </ul>
 */
@Slf4j
public class ThreadLocalInheritTest {

    /**
     * 定义一个 ThreadLocal 变量，初始值为 null。
     * <p>该变量的作用是存储线程本地的字符串值。</p>
     */
    private static final ThreadLocal<String> THREAD_LOCAL = ThreadLocal.withInitial(() -> null);

    /**
     * 程序入口方法。
     * <p>执行逻辑：</p>
     * <ol>
     *   <li>在主线程中设置 ThreadLocal 变量的值为 "coderlee"。</li>
     *   <li>创建一个子线程，在子线程中尝试获取 ThreadLocal 变量的值，并记录日志。</li>
     *   <li>在主线程中再次获取 ThreadLocal 变量的值，并记录日志。</li>
     * </ol>
     * <p>预期结果：</p>
     * <ul>
     *   <li>子线程无法获取到主线程设置的 ThreadLocal 值，输出为 null。</li>
     *   <li>主线程可以正常获取到自己设置的 ThreadLocal 值。</li>
     * </ul>
     *
     * @param args 命令行参数（未使用）。
     */
    public static void main(String[] args) {
        // 在主线程中设置 ThreadLocal 的值为 "coderlee"
        THREAD_LOCAL.set("coderlee");

        // 创建一个子线程，并在其中尝试获取 ThreadLocal 的值
        new Thread(() -> log.info("在子线程中获取到的本地变量值为: {}", THREAD_LOCAL.get())).start();

        // 在主线程中获取 ThreadLocal 的值并记录日志
        log.info("在主线程中获取到的本地变量值为: {}", THREAD_LOCAL.get());
    }
}