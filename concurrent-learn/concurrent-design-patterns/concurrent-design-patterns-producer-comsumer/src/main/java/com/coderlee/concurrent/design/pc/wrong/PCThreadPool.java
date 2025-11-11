package com.coderlee.concurrent.design.pc.wrong;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池工具类，用于执行异步任务
 *
 * <p>该类封装了一个固定大小的线程池，提供execute和submit两种方式提交任务，
 * 并提供了关闭线程池的方法。</p>
 *
 * @see ThreadPoolExecutor
 */
public class PCThreadPool {

    /**
     * 核心线程数和最大线程数都为8的线程池执行器
     * 队列容量为1024的任务队列
     */
    private static final ThreadPoolExecutor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(
            8,
            8,
            30,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(1024));

    /**
     * 私有构造函数，防止实例化
     * 由于此类是工具类，只需要静态方法即可
     */
    private PCThreadPool() {}

    /**
     * 执行指定的任务
     *
     * @param task 要执行的任务
     * @see ThreadPoolExecutor#execute(Runnable)
     */
    public static void execute(Runnable task) {
        THREAD_POOL_EXECUTOR.execute(task);
    }

    /**
     * 提交指定的任务以供执行
     *
     * @param task 要提交的任务
     * @return 表示任务等待结果的Future对象
     * @see ThreadPoolExecutor#submit(Runnable)
     */
    public static Future<?> submit(Runnable task) {
        return THREAD_POOL_EXECUTOR.submit(task);
    }

    /**
     * 关闭线程池，不再接受新任务，但会继续处理已提交的任务
     *
     * @see ThreadPoolExecutor#shutdown()
     */
    public static void shutdown() {
        THREAD_POOL_EXECUTOR.shutdown();
    }
}
