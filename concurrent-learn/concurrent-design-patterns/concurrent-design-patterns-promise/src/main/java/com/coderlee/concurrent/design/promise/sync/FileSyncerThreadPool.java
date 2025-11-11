package com.coderlee.concurrent.design.promise.sync;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 自定义线程池管理类，专门用于执行异步初始化任务。
 */
public class FileSyncerThreadPool {

    // 初始化一个固定大小为8的核心线程池
    private static final ThreadPoolExecutor THREAD_POOL_EXECUTOR =
            new ThreadPoolExecutor(8, 8, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1024));

    // 私有构造函数防止实例化
    private FileSyncerThreadPool(){}

    /**
     * 提交并执行给定的Runnable任务。
     *
     * @param task 需要被执行的任务
     */
    public static void execute(Runnable task){
        THREAD_POOL_EXECUTOR.execute(task);
    }

    /**
     * 关闭线程池资源。
     */
    public static void shutdown(){
        THREAD_POOL_EXECUTOR.shutdown();
    }

}

