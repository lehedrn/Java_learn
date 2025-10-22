package com.coderlee.concurrent.threadgroup;

import lombok.extern.slf4j.Slf4j;

/**
 * ThreadDaemonTest 类用于演示如何创建和启动守护线程。
 * <p>
 * 该类通过实现一个简单的线程示例，展示了以下内容：
 * <ul>
 *     <li>如何使用 {@link Thread} 类创建线程。</li>
 *     <li>如何将线程设置为守护线程。</li>
 *     <li>如何区分守护线程和用户线程的行为。</li>
 * </ul>
 * <p>
 * 守护线程是一种在后台运行的线程，当所有用户线程结束时，守护线程会自动终止。
 */
@Slf4j
public class ThreadDaemonTest {

    /**
     * 程序入口方法，用于启动守护线程并输出线程信息。
     * <p>
     * 该方法执行以下操作：
     * <ol>
     *     <li>创建一个新的线程实例，并为其指定一个自定义名称 "threadDaemon"。</li>
     *     <li>调用 {@link Thread#setDaemon(boolean)} 方法将线程设置为守护线程。</li>
     *     <li>启动线程以执行其任务。</li>
     *     <li>在主线程中打印当前线程的名称。</li>
     * </ol>
     *
     * @param args 命令行参数（未使用）。
     */
    public static void main(String[] args) {
        // 创建一个新的线程实例，使用 lambda 表达式定义线程的任务逻辑。
        Thread threadDaemon = new Thread(() -> {
            // 输出当前线程的名称，验证线程是否正确启动。
            log.info("我是守护线程，名称: {}", Thread.currentThread().getName());
        }, "threadDaemon"); // 为线程指定名称 "threadDaemon"

        // 将线程设置为守护线程。
        threadDaemon.setDaemon(true);

        // 启动线程，使其进入就绪状态并等待 CPU 调度。
        threadDaemon.start();

        // 在主线程中输出当前线程的名称，展示主线程与守护线程的区别。
        log.info("我是主线程，名称: {}", Thread.currentThread().getName());
    }
}