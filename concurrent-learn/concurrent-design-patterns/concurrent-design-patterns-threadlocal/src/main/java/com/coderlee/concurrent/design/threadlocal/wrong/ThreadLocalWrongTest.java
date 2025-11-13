/**
 * 错误使用 ThreadLocal 的示例类。
 * 忘记调用 remove() 方法导致潜在的内存泄漏风险。
 *
 * @see ThreadLocal#remove()
 */
package com.coderlee.concurrent.design.threadlocal.wrong;

import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.*;

@Slf4j
public class ThreadLocalWrongTest {

    /**
     * 固定大小的线程池。
     */
    private static final ExecutorService THREAD_POOL = new ThreadPoolExecutor(
            1, 1, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<>(1024)
    );

    /**
     * ThreadLocal 存储每个线程私有的字符串数据。
     */
    private static final ThreadLocal<String> THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 请求次数。
     */
    private static final int REQUEST_COUNT = 2;

    /**
     * 主方法，模拟多个请求访问 ThreadLocal 数据。
     * 没有调用 remove() 方法可能导致内存泄漏。
     *
     * @param args 启动参数
     */
    public static void main(String[] args) {
        // 初始化倒计时器等待所有请求完成
        CountDownLatch countDownLatch = new CountDownLatch(REQUEST_COUNT);
        // 开始时间戳
        Instant start = Instant.now();
        log.info("重现问题开始");

        // 循环发起请求
        for (int i = 1; i <= REQUEST_COUNT; i++) {
            int count = i;
            THREAD_POOL.execute(() -> {
                // 获取当前线程的 ThreadLocal 数据
                String username = THREAD_LOCAL.get();
                log.info("第 {} 个请求第1次获取到的数据为: {}", count, username);
                // 设置当前线程的 ThreadLocal 数据
                THREAD_LOCAL.set("coderle-00" + count);
                username = THREAD_LOCAL.get();
                log.info("第 {} 个请求第2次获取到的数据为: {}", count, username);
                // 减少倒计时器计数
                countDownLatch.countDown();
            });
        }

        try {
            // 等待所有请求完成
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 结束时间戳并输出耗时
        log.info("重现问题结束, 耗时: {} ms", Duration.between(start, Instant.now()).toMillis());
        // 关闭线程池
        THREAD_POOL.shutdown();
    }
}
