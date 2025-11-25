package com.coderlee.juc1.cf;

import com.coderlee.juc1.utils.SleepUtils;
import lombok.extern.slf4j.Slf4j;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.*;

/**
 * FutureThreadPoolDemo - 演示使用 Future 和线程池实现并发任务处理的示例类
 *
 * 本类通过四个不同的方法(m1, m2, m3, m4)展示了从串行执行到并行执行任务的不同方式，
 * 并比较了各种方式下的执行时间，体现了使用线程池和Future模式提升程序性能的效果。
 */
@Slf4j
public class FutureThreadPoolDemo {

    public static void main(String[] args) {
        // 主方法，调用演示方法
//        m1();      // 串行执行示例
//        m2();      // 多线程无返回值执行示例
//        m3();      // 线程池执行但不获取结果示例
        m4();        // 线程池执行且获取Future结果示例
    }

    /**
     * m4 - 使用固定大小线程池执行任务，并通过FutureTask获取异步任务的结果
     *
     * 创建两个异步任务，分别休眠500ms和300ms后返回结果，主线程等待并获取这两个任务的结果，
     * 最后还执行了一个300ms的本地任务，统计总耗时。
     */
    private static void m4() {
        // 创建固定大小为3的线程池
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        Instant start = Instant.now();

        // 创建第一个FutureTask任务，模拟耗时500ms的操作
        FutureTask<String> futureTask1 = new FutureTask<>(() -> {
            SleepUtils.sleep(500);  // 休眠500毫秒模拟业务处理
            return "task1 over";  // 返回任务完成标识
        });
        threadPool.submit(futureTask1);  // 提交任务到线程池

        // 创建第二个FutureTask任务，模拟耗时300ms的操作
        FutureTask<String> futureTask2 = new FutureTask<>(() -> {
            SleepUtils.sleep(300);  // 休眠300毫秒模拟业务处理
            return "task2 over";  // 返回任务完成标识
        });
        threadPool.submit(futureTask2);  // 提交任务到线程池

        try {
            // 获取第一个任务的执行结果（阻塞直到任务完成）
            log.info("task1 result is: {}", futureTask1.get());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        try {
            // 获取第二个任务的执行结果（阻塞直到任务完成）
            log.info("task2 result is: {}", futureTask2.get());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        SleepUtils.sleep(300);  // 主线程继续执行其他工作，耗时300ms
        log.info("costTime: {} ms", Duration.between(start, Instant.now()).toMillis());  // 输出总耗时
        log.info("{} -----end", Thread.currentThread().getName());  // 输出结束日志
        threadPool.shutdown();  // 关闭线程池
    }

    /**
     * m3 - 使用固定大小线程池执行任务，但不关心任务执行结果
     *
     * 创建两个异步任务提交给线程池执行，主线程不等待任务结果，直接继续执行后续操作。
     */
    private static void m3() {
        // 创建固定大小为3的线程池
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        Instant start = Instant.now();

        // 创建第一个FutureTask任务，模拟耗时500ms的操作
        FutureTask<String> futureTask1 = new FutureTask<>(() -> {
            SleepUtils.sleep(500);  // 休眠500毫秒模拟业务处理
            return "task1 over";  // 返回任务完成标识
        });
        threadPool.submit(futureTask1);  // 提交任务到线程池

        // 创建第二个FutureTask任务，模拟耗时300ms的操作
        FutureTask<String> futureTask2 = new FutureTask<>(() -> {
            SleepUtils.sleep(300);  // 休眠300毫秒模拟业务处理
            return "task2 over";  // 返回任务完成标识
        });
        threadPool.submit(futureTask2);  // 提交任务到线程池

        SleepUtils.sleep(300);  // 主线程继续执行其他工作，耗时300ms
        log.info("costTime: {} ms", Duration.between(start, Instant.now()).toMillis());  // 输出总耗时
        log.info("{} -----end", Thread.currentThread().getName());  // 输出结束日志
        threadPool.shutdown();  // 关闭线程池
    }

    /**
     * m2 - 使用手动创建线程的方式执行并发任务
     *
     * 直接创建新线程来执行异步任务，不使用线程池管理。
     */
    private static void m2() {
        Instant start = Instant.now();
        // 启动第一个线程执行500ms的任务
        new Thread(() -> SleepUtils.sleep(500)).start();
        // 启动第二个线程执行300ms的任务
        new Thread(() -> SleepUtils.sleep(300)).start();
        SleepUtils.sleep(300);  // 主线程继续执行其他工作，耗时300ms
        log.info("costTime: {} ms", Duration.between(start, Instant.now()).toMillis());  // 输出总耗时
        log.info("{} -----end", Thread.currentThread().getName());  // 输出结束日志
    }

    /**
     * m1 - 串行执行所有任务
     *
     * 在单个主线程中顺序执行三个任务，没有并发处理。
     */
    private static void m1() {
        Instant start = Instant.now();
        SleepUtils.sleep(500);  // 执行第一个任务，耗时500ms
        SleepUtils.sleep(300);  // 执行第二个任务，耗时300ms
        SleepUtils.sleep(300);  // 执行第三个任务，耗时300ms
        log.info("costTime: {} ms", Duration.between(start, Instant.now()).toMillis());  // 输出总耗时
        log.info("{} -----end", Thread.currentThread().getName());  // 输出结束日志
    }

}
