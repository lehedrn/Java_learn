package com.coderlee.concurrent.threadgroup;

import lombok.extern.slf4j.Slf4j;

/**
 * ThreadUserTest 类用于演示如何创建和启动一个用户线程。
 * <p>
 * 该类通过实现一个简单的线程示例，展示了以下内容：
 * <ul>
 *     <li>如何使用 {@link Thread} 类创建线程。</li>
 *     <li>如何为线程指定名称并启动线程。</li>
 *     <li>如何在运行时获取当前线程的名称并通过日志输出。</li>
 * </ul>
 */
@Slf4j
public class ThreadUserTest {

    /**
     * 程序入口方法，用于启动用户线程并输出线程信息。
     * <p>
     * 该方法执行以下操作：
     * <ol>
     *     <li>创建一个新的线程实例，并为其指定一个自定义名称 "threadUser"。</li>
     *     <li>在线程中打印当前线程的名称。</li>
     *     <li>启动线程以执行其任务。</li>
     * </ol>
     *
     * @param args 命令行参数（未使用）。
     */
    public static void main(String[] args) {
        // 创建一个新的线程实例，使用 lambda 表达式定义线程的任务逻辑。
        Thread threadUser = new Thread(() -> {
            // 输出当前线程的名称，验证线程是否正确启动。
            log.info("我是用户线程, 名称: {}", Thread.currentThread().getName());
        }, "threadUser"); // 为线程指定名称 "threadUser"

        // 启动线程，使其进入就绪状态并等待 CPU 调度。
        threadUser.start();
    }
}