/**
 * 自定义线程池测试类
 * <p>
 * 测试自定义线程池的基本功能，提交多个任务并观察执行情况。
 * </p>
 */
package com.coderlee.concurrent.design.thread.pool.threadpool;

import lombok.extern.slf4j.Slf4j;

import java.util.stream.IntStream;

@Slf4j
public class ThreadPoolTest {
    /**
     * 程序入口点
     * <p>
     * 创建自定义线程池实例，并提交10个简单任务进行测试。
     * </p>
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 创建大小为5的线程池
        ThreadPool threadPool = new ThreadPool(5);

        // 提交10个测试任务到线程池
        IntStream.range(0, 10).forEach(i ->
            // 执行日志输出任务
            threadPool.execute(() ->
                log.info("{} ---->> run task[{}] by threadpool", Thread.currentThread().getName(), i)
            )
        );
    }
}
