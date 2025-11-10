package com.coderlee.concurrent.design.two.phase.executor;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 线程执行器类，实现了两阶段终止模式（Two-Phase Termination Pattern）
 * <p>
 * 该类负责执行任务并在指定时间内终止任务执行。它通过创建守护线程执行任务，
 * 并提供超时机制来控制任务的执行时间。
 *
 * @see <a href="https://java-design-patterns.com/patterns/two-phase-termination/">Two-Phase Termination Pattern</a>
 * </p>
 */
@Slf4j
public class ThreadExecutor {
    /**
     * 执行任务的线程实例
     */
    private Thread executeThread;

    /**
     * 标识任务是否正在运行的标志位
     */
    private volatile boolean isRunning = false;

    /**
     * 主函数，用于测试ThreadExecutor的功能
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        ThreadExecutor executor = new ThreadExecutor();
        long start = System.currentTimeMillis();

        // 执行一个耗时5秒的任务
        executor.execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        // 尝试在1秒后关闭任务（会导致超时）
        executor.shutdown(1000);
        long end = System.currentTimeMillis();
        log.info("任务执行完成，耗时：{}ms", end - start);
    }

    /**
     * 执行指定的任务
     * <p>
     * 该方法会创建一个新的线程来执行任务，并将任务线程设置为守护线程。
     * 主线程会等待任务线程执行完成。
     * </p>
     *
     * @param task 要执行的任务
     */
    public void execute(Runnable task) {
        // 创建执行线程
        executeThread = new Thread(() -> {
            // 创建并启动子线程执行任务
            Thread childThread = new Thread(task);
            // 设置为守护线程，以便在主程序退出时自动终止
            childThread.setDaemon(true);
            childThread.start();
            try {
                // 等待子线程执行完成
                childThread.join();
                // 标记任务正在运行
                isRunning = true;
            } catch (InterruptedException e) {
                // 处理中断异常
                //throw new RuntimeException(e);
            }
        });
        // 启动执行线程
        executeThread.start();
    }

    /**
     * 关闭执行器，如果任务在指定时间内未完成则中断任务
     *
     * @param mills 超时时间（毫秒）
     */
    public void shutdown(long mills) {
        // 记录开始时间
        long currentTime = System.currentTimeMillis();

        // 循环检查任务是否完成
        while (!isRunning) {
            // 检查是否超时
            if (System.currentTimeMillis() - currentTime > mills) {
                log.info("任务超时，结束任务");
                // 中断执行线程
                executeThread.interrupt();
                break;
            }
        }
        // 重置运行状态
        isRunning = false;
    }
}
