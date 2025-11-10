package com.coderlee.guarded.suspension.lock.demo;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * GuardedQueueApp 是 GuardedQueue 的测试应用类。
 *
 * 该类演示了 Guarded Suspension 模式的工作原理：
 * 1. 创建一个消费者线程尝试从空队列获取数据（会被阻塞）
 * 2. 等待一段时间后，创建一个生产者线程向队列添加数据
 * 3. 生产者添加数据后会唤醒被阻塞的消费者线程
 *
 * @see GuardedQueue
 * @see Executors
 * @see ExecutorService
 */
@Slf4j
public class GuardedQueueApp {
    /**
     * 应用程序入口点
     *
     * @param args 命令行参数
     * @throws RuntimeException 当线程执行被中断时抛出
     */
    public static void main(String[] args) {
        // 创建 GuardedQueue 实例
        GuardedQueue guardedQueue = new GuardedQueue();

        // 创建固定大小为3的线程池
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        // 提交消费者任务：从队列中获取元素
        executorService.execute(() -> {
            // 调用 get 方法获取队列元素，如果队列为空则会阻塞等待
            Integer rs = guardedQueue.get();
            // 记录获取到的结果
            log.info("{} get rs: {}", Thread.currentThread().getName(), rs);
        });

        try {
            // 主线程休眠2秒，确保消费者线程先执行并进入等待状态
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            // 如果休眠被中断，则抛出运行时异常
            throw new RuntimeException(e);
        }

        // 提交生产者任务：向队列中添加元素
        executorService.execute(() -> {
            // 记录添加操作日志
            log.info("{} put 20 to queue", Thread.currentThread().getName());
            // 向队列中添加元素20，此操作会唤醒等待的消费者线程
            guardedQueue.put(20);
        });

        // 关闭线程池，不再接受新任务
        executorService.shutdown();

        try {
            // 等待所有任务在30秒内完成
            executorService.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            // 如果等待过程被中断，则抛出运行时异常
            throw new RuntimeException(e);
        }
    }
}
