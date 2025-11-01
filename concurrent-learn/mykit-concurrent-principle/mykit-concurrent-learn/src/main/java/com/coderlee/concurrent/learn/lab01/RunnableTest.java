package com.coderlee.concurrent.learn.lab01;

import lombok.extern.slf4j.Slf4j;

/**
 * RunnableTest 是一个用于演示多线程基本用法的类。
 * <p>
 * 该类通过实现 {@link Runnable} 接口展示了三种创建线程任务的方式：
 * <ul>
 *   <li>使用静态内部类实现 {@link Runnable} 接口</li>
 *   <li>使用匿名类实现 {@link Runnable} 接口</li>
 *   <li>使用 Lambda 表达式实现 {@link Runnable} 接口</li>
 * </ul>
 * 同时，打印了主线程和各个新线程的名称，以区分它们的执行顺序和线程标识。
 * </p>
 */
@Slf4j
public class RunnableTest {

    /**
     * 程序的入口方法。
     * <p>
     * 在该方法中，分别通过以下方式创建并启动线程：
     * <ol>
     *   <li>使用静态内部类 {@link MyRunnableTask} 创建线程</li>
     *   <li>调用 {@link #createByRunnableAnonymousClass()} 方法，使用匿名类创建线程</li>
     *   <li>调用 {@link #createThreadByRunnableLambda()} 方法，使用 Lambda 表达式创建线程</li>
     * </ol>
     * 最后，打印主线程的名称以区分主线程和新线程。
     * </p>
     *
     * @param args 命令行参数（未使用）
     */
    public static void main(String[] args) {
        // 使用静态内部类创建并启动线程
        new Thread(new MyRunnableTask(), "Thread-RunnableImpl").start();

        // 使用匿名类创建并启动线程
        RunnableTest rt = new RunnableTest();
        rt.createByRunnableAnonymousClass().start();

        // 使用 Lambda 表达式创建并启动线程
        rt.createThreadByRunnableLambda().start();

        // 打印主线程名称
        log.info("主线程名称=====================>> [{}]", Thread.currentThread().getName());
    }

    /**
     * 静态内部类，用于实现 {@link Runnable} 接口。
     * <p>
     * 该类定义了一个简单的线程任务，任务逻辑为打印当前线程的名称。
     * </p>
     */
    private static class MyRunnableTask implements Runnable {

        /**
         * 定义线程启动后执行的任务逻辑。
         * <p>
         * 该方法会在调用 {@link Thread#start()} 方法后由 JVM 自动调用，
         * 并打印当前线程的名称以展示线程的运行状态。
         * </p>
         */
        @Override
        public void run() {
            // 打印当前线程的名称
            log.info("新创建的线程名称==============>> [{}]", Thread.currentThread().getName());
        }
    }

    /**
     * 使用匿名类实现 {@link Runnable} 接口创建线程。
     * <p>
     * 该方法返回一个新创建的线程实例，线程任务逻辑为打印当前线程的名称。
     * </p>
     *
     * @return 新创建的线程实例
     */
    public Thread createByRunnableAnonymousClass() {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                // 打印当前线程的名称
                log.info("新创建的线程名称==============>> [{}]", Thread.currentThread().getName());
            }
        }, "Thread-RunnableAnonymous");
    }

    /**
     * 使用 Lambda 表达式实现 {@link Runnable} 接口创建线程。
     * <p>
     * 该方法返回一个新创建的线程实例，线程任务逻辑为打印当前线程的名称。
     * </p>
     *
     * @return 新创建的线程实例
     */
    public Thread createThreadByRunnableLambda() {
        return new Thread(() -> log.info("新创建的线程名称==============>> [{}]", Thread.currentThread().getName()), "Thread-RunnableLambda");
    }
}