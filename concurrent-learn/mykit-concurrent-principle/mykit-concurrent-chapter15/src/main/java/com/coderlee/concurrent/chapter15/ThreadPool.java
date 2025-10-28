package com.coderlee.concurrent.chapter15;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.IntStream;

/**
 * 自定义线程池实现类。
 * 该类通过维护一组工作线程（WorkThread）和一个阻塞队列（workQueue）来模拟线程池的行为。
 * 工作线程会从队列中取出任务并执行，直到线程被中断。
 */
public class ThreadPool {

    private static final int DEFAULT_WORKQUEUE_SIZE = 5;

    /** 阻塞队列，用于存储待执行的任务 */
    private BlockingQueue<Runnable> workQueue;

    /** 工作线程列表，用于执行任务 */
    private List<WorkThread> workThreads = new ArrayList<>();

    /**
     * 构造函数，创建指定大小的线程池，并使用自定义的任务队列。
     *
     * @param poolSize 线程池中工作线程的数量
     * @param workQueue 用于存储任务的阻塞队列
     */
    public ThreadPool(int poolSize, BlockingQueue<Runnable> workQueue) {
        this.workQueue = workQueue;
        IntStream.range(0, poolSize).forEach((i) -> {
            WorkThread workThread = new WorkThread();
            workThread.start(); // 启动工作线程
            workThreads.add(workThread); // 将工作线程加入线程池
        });
    }

    /**
     * 构造函数，创建指定大小的线程池，并使用默认容量的任务队列。
     *
     * @param poolSize 线程池中工作线程的数量
     */
    public ThreadPool(int poolSize) {
        this(poolSize, new LinkedBlockingQueue<>(DEFAULT_WORKQUEUE_SIZE));
    }

    /**
     * 提交任务到线程池中执行。
     * 任务会被放入阻塞队列，等待工作线程取走并执行。
     *
     * @param task 待执行的任务
     */
    public void execute(Runnable task) {
        try {
            workQueue.put(task); // 将任务放入队列，如果队列已满，则阻塞等待
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // 恢复中断状态
            e.printStackTrace();
        }
    }

    /**
     * 关闭线程池。
     * 通过中断所有工作线程来停止线程池的运行。
     */
    public void shutdown() {
        if (null != workThreads && workThreads.size() > 0) {
            workThreads.stream().forEach((workThread) -> workThread.interrupt()); // 中断每个工作线程
        }
    }

    /**
     * 工作线程类，负责从任务队列中取出任务并执行。
     * 每个工作线程在一个无限循环中运行，直到被中断。
     */
    class WorkThread extends Thread {

        /**
         * 工作线程的核心逻辑。
         * 不断从任务队列中取出任务并执行，直到线程被中断。
         */
        @Override
        public void run() {
            Thread currentThread = Thread.currentThread(); // 获取当前线程引用
            while (true) {
                try {
                    if (currentThread.isInterrupted()) { // 检查线程是否被中断
                        break; // 如果被中断，退出循环
                    }
                    Runnable workTask = workQueue.take(); // 从队列中取出任务，如果队列为空则阻塞
                    workTask.run(); // 执行任务
                } catch (Exception e) {
                    //发生中断异常时需要重新设置中断标志位
                    currentThread.interrupt();
                }
            }
        }
    }
}