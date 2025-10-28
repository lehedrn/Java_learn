package com.coderlee.concurrent.chapter15;

import java.util.stream.IntStream;

/**
 * 测试类，用于验证自定义线程池（{@link ThreadPool}）的功能。
 * 该类通过创建一个线程池并向其中提交多个任务，展示了线程池的基本使用方式。
 */
public class ThreadPoolTest {

    /**
     * 主方法，程序入口。
     * <p>
     * 1. 创建一个包含 5 个工作线程的线程池。
     * 2. 提交 10 个任务到线程池中执行。
     * 3. 每个任务会打印当前线程名称和任务编号。
     * 4. 所有任务提交完成后，关闭线程池。
     *
     * @param args 命令行参数（未使用）
     */
    public static void main(String[] args) {
        // 创建一个包含 5 个工作线程的线程池
        ThreadPool threadPool = new ThreadPool(5);

        // 提交 10 个任务到线程池
        IntStream.range(0, 10).forEach((i) -> {
            threadPool.execute(() -> {
                // 打印当前线程名称和任务编号
                System.out.println(Thread.currentThread().getName() + "--->> run task[" + i + "]");
            });
        });

        // 关闭线程池，停止所有工作线程
        threadPool.shutdown();
    }
}
