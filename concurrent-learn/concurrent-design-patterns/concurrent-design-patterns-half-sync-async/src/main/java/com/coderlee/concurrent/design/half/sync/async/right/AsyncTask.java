package com.coderlee.concurrent.design.half.sync.async.right;

import java.util.concurrent.*;

/**
 * 异步任务抽象基类，用于支持半同步/半异步模式。
 * <p>
 * 提供了异步任务的基本框架，包括前置处理、后置处理、异常处理以及执行调度等功能。
 * 子类只需实现 {@link #doExecute(Object...)} 方法即可定义具体的业务逻辑。
 * </p>
 *
 * @param <T> 异步任务返回值的类型
 */
public abstract class AsyncTask<T> {

    /**
     * 默认线程池，用于执行异步任务。
     */
    private volatile Executor executor;
    private static final ExecutorService DEFAULT_EXECUTOR;

    static {
        // 初始化默认线程池
        DEFAULT_EXECUTOR = new ThreadPoolExecutor(
                1,                             // 核心线程数
                1,                             // 最大线程数
                60,                            // 空闲线程存活时间
                TimeUnit.SECONDS,              // 时间单位
                new ArrayBlockingQueue<>(1024),// 阻塞队列容量为1024
                r -> {                         // 自定义线程工厂
                    Thread thread = new Thread(r, "thread-half-sync-async");
                    thread.setDaemon(true);    // 设置为守护线程
                    return thread;
                },
                new ThreadPoolExecutor.CallerRunsPolicy()); // 拒绝策略
    }

    /**
     * 使用默认线程池初始化异步任务。
     */
    public AsyncTask() {
        this(DEFAULT_EXECUTOR);
    }

    /**
     * 使用指定线程池初始化异步任务。
     *
     * @param executor 执行器
     */
    public AsyncTask(Executor executor) {
        this.executor = executor;
    }

    /**
     * 设置执行器。
     *
     * @param executor 新的执行器
     */
    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    /**
     * 在异步任务执行前调用的方法，子类可重写此方法进行预处理。
     *
     * @param params 参数列表
     */
    protected void doPreExecute(Object... params) {

    }

    /**
     * 在异步任务执行完成后调用的方法，子类可重写此方法进行后置处理。
     *
     * @param result 异步任务的结果
     */
    protected void doPostExecute(T result) {

    }

    /**
     * 处理异步任务执行过程中抛出的异常。
     *
     * @param e 异常对象
     */
    protected void doExeception(Exception e) {
        e.printStackTrace();
    }

    /**
     * 定义异步任务的具体执行逻辑，必须由子类实现。
     *
     * @param params 参数列表
     * @return 异步任务的执行结果
     */
    protected abstract T doExecute(Object... params);

    /**
     * 调度并执行异步任务。
     *
     * @param params 参数列表
     * @return 返回一个Future对象，可用于获取异步任务的执行结果
     */
    protected Future<T> dispatch(final Object... params) {
        doPreExecute(params); // 前置处理

        // 创建Callable对象包装实际的任务逻辑
        Callable<T> callable = () -> doExecute(params);

        // 创建FutureTask对象，在任务完成时执行回调
        FutureTask<T> futureTask = new FutureTask<T>(callable) {
            @Override
            protected void done() {
                try {
                    // 获取任务结果并执行后置处理
                    doPostExecute(this.get());
                } catch (InterruptedException | ExecutionException e) {
                    // 处理任务执行过程中的异常
                    doExeception(e);
                }
            }
        };

        // 将任务提交给线程池执行
        executor.execute(futureTask);
        return futureTask;
    }
}
