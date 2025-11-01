package com.coderlee.concurrent.learn.lab01;

import lombok.extern.slf4j.Slf4j;

/**
 * ThreadTest 是一个用于演示多线程基本用法的类。
 * <p>
 * 该类通过继承 {@link Thread} 类创建了一个自定义线程任务，并在主线程中启动该线程，
 * 展示了主线程和新线程之间的执行顺序和线程名称的获取方式。
 * </p>
 */
@Slf4j
public class ThreadTest {

    /**
     * 程序的入口方法。
     * <p>
     * 在该方法中，创建了一个自定义线程任务 {@link MyThreadTask} 的实例，并调用其 {@code start()} 方法启动线程。
     * 同时，打印主线程的名称以区分新线程和主线程。
     * </p>
     *
     * @param args 命令行参数（未使用）
     */
    public static void main(String[] args) {
        // 创建自定义线程任务实例
        Thread thread = new MyThreadTask();
        // 启动线程
        thread.start();
        // 打印主线程名称
        log.info("主线程名称================>>> [{}]", Thread.currentThread().getName());
    }

    /**
     * 自定义线程任务类，继承自 {@link Thread}。
     * <p>
     * 该类重写了 {@link Thread#run()} 方法，用于定义线程启动后需要执行的任务逻辑。
     * 在本例中，任务逻辑为打印当前线程的名称。
     * </p>
     */
    private static class MyThreadTask extends Thread {

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
            log.info("新创建的线程名称================>>> [{}]", Thread.currentThread().getName());
        }
    }
}