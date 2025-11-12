/**
 * 自定义线程池实现
 * <p>
 * 实现了一个简单的线程池，包含工作线程队列和任务队列，
 * 工作线程从任务队列中取出任务并执行。
 * </p>
 */
package com.coderlee.concurrent.design.thread.pool.threadpool;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.IntStream;

@Slf4j
public class ThreadPool {
    /**
     * 默认工作队列大小
     */
    private static final int DEFAULT_WORKQUEUE_SIZE = 5;

    /**
     * 任务队列，用于存储待执行的任务
     */
    private BlockingQueue<Runnable> workQueue;

    /**
     * 工作线程列表，存储所有工作线程
     */
    private List<WorkThread> workThreads = new ArrayList<>();

    /**
     * 构造函数，创建指定大小的线程池
     *
     * @param poolSize 线程池大小
     * @param workQueue 任务队列
     */
    public ThreadPool(int poolSize, BlockingQueue<Runnable> workQueue) {
        // 保存任务队列引用
        this.workQueue = workQueue;

        // 创建指定数量的工作线程并启动
        IntStream.range(0, poolSize).forEach(i -> {
            // 创建工作线程实例
            WorkThread workThread = new WorkThread();
            // 启动工作线程
            workThread.start();
            // 将工作线程添加到线程列表中
            workThreads.add(workThread);
        });
    }

    /**
     * 构造函数，创建指定大小的线程池，使用默认队列大小
     *
     * @param poolSize 线程池大小
     */
    public ThreadPool(int poolSize) {
        // 调用带队列参数的构造函数
        this(poolSize, new LinkedBlockingQueue<>(DEFAULT_WORKQUEUE_SIZE));
    }

    /**
     * 执行指定的任务
     * <p>
     * 将任务添加到任务队列中，等待工作线程执行。
     * </p>
     *
     * @param task 要执行的任务
     */
    public void execute(Runnable task) {
        try {
            // 将任务放入任务队列
            workQueue.put(task);
        } catch (InterruptedException e) {
            // 记录线程中断异常日志
            log.error("workQueue线程中断异常", e);
        }
    }

    /**
     * 工作线程内部类
     * <p>
     * 负责从任务队列中取出任务并执行。
     * </p>
     */
    class WorkThread extends Thread {
        /**
         * 工作线程的执行逻辑
         * <p>
         * 不断从任务队列中取出任务并执行，直到线程被中断。
         * </p>
         */
        @Override
        public void run() {
            // 循环执行任务
            while (true) {
                try {
                    // 从任务队列中取出一个任务
                    Runnable task = workQueue.take();
                    // 执行任务
                    task.run();
                } catch (InterruptedException e) {
                    // 记录Runnable线程中断异常日志
                    log.error("Runnable线程中断异常", e);
                }
            }
        }
    }
}
