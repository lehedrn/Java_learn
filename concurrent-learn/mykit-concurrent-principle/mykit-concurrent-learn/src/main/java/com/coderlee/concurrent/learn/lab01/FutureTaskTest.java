package com.coderlee.concurrent.learn.lab01;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import lombok.extern.slf4j.Slf4j;

/**
 * FutureTaskTest 是一个用于演示多线程中使用 {@link FutureTask} 的类。
 * <p>
 * 该类展示了如何通过 Lambda 表达式创建一个返回结果的线程任务，并使用 {@link FutureTask} 获取任务的执行结果。
 * 同时，打印了线程名称和任务执行的时间戳，以展示线程的运行状态和结果。
 * </p>
 */
@Slf4j
public class FutureTaskTest {

    /**
     * 程序的入口方法。
     * <p>
     * 在该方法中，创建了一个基于 Lambda 表达式的线程任务，并通过 {@link FutureTask} 包装后启动线程。 启动线程后，调用
     * {@link FutureTask#get()} 方法获取线程任务的执行结果。 如果获取结果过程中发生异常，则会捕获并打印堆栈信息。
     * </p>
     *
     * @param args 命令行参数（未使用）
     */
    public static void main(String[] args) {
        // 使用 Lambda 表达式定义线程任务逻辑
        FutureTask<String> futureTask = new FutureTask<>(() -> 
            "Thread [" + Thread.currentThread().getName() + "] is running at [" + LocalDateTime.now().toString() + "]");

        // 创建并启动线程
        new Thread(futureTask, "future-task-001").start();

        try {
            // 获取线程任务的执行结果
            log.info("futureTask result is:\n{}", futureTask.get());
        } catch (InterruptedException | ExecutionException e) {
            // 捕获并打印异常
            e.printStackTrace();
        }
    }
}