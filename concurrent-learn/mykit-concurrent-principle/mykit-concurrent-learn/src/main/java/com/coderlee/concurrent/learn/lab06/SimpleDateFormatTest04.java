package com.coderlee.concurrent.learn.lab06;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import lombok.extern.slf4j.Slf4j;

/**
 * 解决SimpleDateFormat类的线程安全问题 
 * 方案三：Lock锁方式
 * Lock锁方式与synchronized锁方式{@link SimpleDateFormatTest03}实现原理相同，都是在高并发下通过JVM的锁机制来保证程序的线程安全
 * 
 * <p>
 * 虽然这种方式能够解决SimpleDateFormat类的线程安全问题，但是由于在程序的执行过程中，为SimpleDateFormat类对象加上了Lock锁，导致同一时刻只能有一个线程执行parse(String)方法。
 * 此时，会影响程序的执行性能，在要求高并发的生产环境下，此种方式也是不太推荐使用的。
 * </p>
 */
@Slf4j
public class SimpleDateFormatTest04 {

    // 定义线程池的执行次数
    private static final int EXECUTE_COUNT = 1000;

    // 定义并发线程的最大数量
    private static final int THREAD_COUNT = 20;

    // 创建一个 SimpleDateFormat 对象，用于解析日期字符串
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    // 创建一个 Lock 对象，用于加锁和解锁
    private static Lock lock = new ReentrantLock();

    /**
     * 主方法，用于启动多线程测试逻辑。
     * 
     * <p>
     * 通过线程池和信号量机制，模拟大量线程并发调用 {@link SimpleDateFormat#parse(String)} 方法，
     * 并捕获可能的异常。如果发现异常，程序将终止并记录错误日志。
     * </p>
     *
     * @param args 命令行参数（未使用）
     */
    public static void main(String[] args) {
        // 创建一个信号量，限制最大并发线程数为 THREAD_COUNT
        final Semaphore semaphore = new Semaphore(THREAD_COUNT);

        // 创建一个计数器，用于确保主线程等待所有子线程完成
        final CountDownLatch countDownLatch = new CountDownLatch(EXECUTE_COUNT);

        // 创建一个可缓存的线程池
        ExecutorService executorService = Executors.newCachedThreadPool();

        for (int i = 0; i < EXECUTE_COUNT; i++) {
            executorService.execute(() -> {
                try {
                    // 获取信号量许可，控制并发线程数
                    semaphore.acquire();
                    try {
                        lock.lock();
                        // 调用 parse 方法解析日期字符串
                        simpleDateFormat.parse("2025-11-13");
                    } catch (ParseException e) {
                        // 捕获日期格式化异常，并记录错误日志
                        log.error("线程: [{}] 格式化日期失败", Thread.currentThread().getName(), e);
                        System.exit(1); // 终止程序
                    } catch (NumberFormatException e) {
                        // 捕获数字格式化异常，并记录错误日志
                        log.error("线程: [{}] 格式化日期异常", Thread.currentThread().getName(), e);
                        System.exit(1); // 终止程序
                    } finally {
                        lock.unlock();
                    }
                    // 释放信号量许可，允许其他线程获取
                    semaphore.release();
                } catch (InterruptedException e) {
                    // 捕获信号量获取过程中可能出现的中断异常
                    log.error("线程: [{}] 获取信号量异常", Thread.currentThread().getName(), e);
                    System.exit(1); // 终止程序
                }

                // 计数器减一，表示当前线程已完成任务
                countDownLatch.countDown();
            });
        }

        try {
            // 等待所有线程执行完毕
            countDownLatch.await();
        } catch (InterruptedException e) {
            // 捕获主线程等待过程中可能出现的中断异常
            log.error("主线程等待异常", e);
        }

        // 关闭线程池
        executorService.shutdown();

        // 打印成功日志
        log.info("所有线程格式化日期成功");
    }
}