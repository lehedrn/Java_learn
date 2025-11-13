/**
 * 另一种正确使用 ThreadLocal 实现线程安全日期格式化的示例。
 * 与 {@link RightSimpleDateFormat} 类似，但手动管理 ThreadLocal 中的实例初始化。
 *
 * @see SimpleDateFormat
 * @see ThreadLocal
 */
package com.coderlee.concurrent.design.threadlocal.right;

import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

@Slf4j
public class RightSimpleDateFormat2 {

    /**
     * 执行任务总数。
     */
    private static final int EXECUTE_COUNT = 1000;

    /**
     * 并发线程数。
     */
    private static final int THREAD_COUNT = 20;

    /**
     * ThreadLocal 存储每个线程私有的 DateFormat 实例。
     */
    private static ThreadLocal<DateFormat> threadLocal = new ThreadLocal<>();

    /**
     * 获取当前线程绑定的 DateFormat 实例。
     * 如果尚未存在，则创建一个新的实例并存储在 ThreadLocal 中。
     *
     * @return 当前线程绑定的 DateFormat 实例
     */
    private static DateFormat getDateFormat() {
        // 获取当前线程中的 DateFormat 实例
        DateFormat dataFormat = threadLocal.get();
        // 如果不存在则创建新实例并保存
        if (null == dataFormat) {
            dataFormat = new SimpleDateFormat("yyyy-MM-dd");
            threadLocal.set(dataFormat);
        }
        return dataFormat;
    }

    /**
     * 主方法，创建多个线程并发执行日期解析任务。
     * 使用 Semaphore 控制最大并发数，CountDownLatch 等待所有任务完成。
     *
     * @param args 启动参数
     */
    public static void main(String[] args) {
        // 初始化信号量控制并发数量
        final Semaphore semaphore = new Semaphore(THREAD_COUNT);
        // 初始化倒计时器等待所有任务完成
        final CountDownLatch countDownLatch = new CountDownLatch(EXECUTE_COUNT);
        // 创建线程池
        ExecutorService executorService = Executors.newCachedThreadPool();

        // 提交任务到线程池
        for (int i = 0; i < EXECUTE_COUNT; i++) {
            executorService.execute(() -> {
                try {
                    // 获取许可
                    semaphore.acquire();
                    try {
                        // 使用 getDateFormat() 方法获取当前线程的 DateFormat 实例进行解析
                        getDateFormat().parse("2025-11-13");
                    } catch (ParseException e) {
                        // 记录异常日志并退出程序
                        log.error("线程: {} 格式化日期失败", Thread.currentThread().getName(), e);
                        System.exit(1);
                    } catch (NumberFormatException e) {
                        // 记录异常日志并退出程序
                        log.error("线程: {} 格式化日期失败", Thread.currentThread().getName(), e);
                        System.exit(1);
                    }
                    // 释放许可
                    semaphore.release();
                } catch (InterruptedException e) {
                    // 处理中断异常并退出程序
                    log.error("信号量发生错误", e);
                    System.exit(1);
                }
                // 减少倒计时器计数
                countDownLatch.countDown();
            });
        }

        try {
            // 等待所有任务完成
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 关闭线程池
        executorService.shutdown();
        // 输出成功信息
        log.info("所有线程格式化日期成功");
    }
}
