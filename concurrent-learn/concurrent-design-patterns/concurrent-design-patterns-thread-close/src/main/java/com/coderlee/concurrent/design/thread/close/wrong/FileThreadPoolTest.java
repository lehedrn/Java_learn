package com.coderlee.concurrent.design.thread.close.wrong;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 使用线程池的错误示例
 * <p>虽然使用了线程池，但没有正确的任务管理和线程池关闭时机控制</p>
 *
 * @see com.coderlee.concurrent.design.thread.close.right.FileRightTest 正确的实现方式
 */
public class FileThreadPoolTest {

    // 并发下载任务数量
    private static final int COUNT = 10;

    // 线程池实例，用于执行下载任务
    private static final ThreadPoolExecutor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(
            1,           // 核心线程数
            1,           // 最大线程数
            30,          // 空闲线程存活时间
            TimeUnit.SECONDS,  // 时间单位
            new LinkedBlockingQueue<>(100)  // 任务队列
    );

    /**
     * 主函数，执行文件下载测试
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 创建倒计时门闩，用于等待所有任务完成
        CountDownLatch countDownLatch = new CountDownLatch(COUNT);
        // 创建文件服务实例
        FileService fileService = new FileServiceImpl();

        // 使用线程池执行多个下载任务
        for (int i = 1; i <= COUNT; i++) {
            // 使用final变量捕获循环变量
            final int index = i;
            // 提交任务到线程池
            THREAD_POOL_EXECUTOR.execute(() -> {
                fileService.downloadFile("coderlee-" + index);
                // 任务完成后减少门闩计数
                countDownLatch.countDown();
            });
        }

        try {
            // 等待所有下载任务完成
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // 关闭线程池
        THREAD_POOL_EXECUTOR.shutdown();
    }
}
