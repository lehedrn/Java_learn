package com.coderlee.concurrent.learn.lab01;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import lombok.extern.slf4j.Slf4j;

/**
 * CallableTest 是一个用于演示多线程中使用 {@link Callable} 接口的类。
 * <p>
 * 该类展示了三种创建线程任务的方式：
 * <ul>
 *   <li>使用静态内部类实现 {@link Callable} 接口</li>
 *   <li>使用匿名类实现 {@link Callable} 接口</li>
 *   <li>使用 Lambda 表达式实现 {@link Callable} 接口</li>
 * </ul>
 * 同时，通过 {@link FutureTask} 获取线程任务的执行结果，并打印主线程和各个新线程的名称，以区分它们的执行顺序和线程标识。
 * </p>
 */
@Slf4j
public class CallableTest {

    /**
     * 程序的入口方法。
     * <p>
     * 在该方法中，分别通过以下方式创建并启动线程：
     * <ol>
     *   <li>使用静态内部类 {@link MyCallableTask} 创建线程</li>
     *   <li>调用 {@link #createThreadByCallableAnnoymousClass()} 方法，使用匿名类创建线程</li>
     *   <li>调用 {@link #createThreadByCallableLambda()} 方法，使用 Lambda 表达式创建线程</li>
     * </ol>
     * 最后，打印主线程的名称以区分主线程和新线程。
     * </p>
     *
     * @param args 命令行参数（未使用）
     */
    public static void main(String[] args) {
        // 使用静态内部类创建并启动线程
        CallableTest callableTest = new CallableTest();
        callableTest.execute(new FutureTask<>(new MyCallableTask()), "interface impl");

        // 使用匿名类创建并启动线程
        callableTest.execute(callableTest.createThreadByCallableAnnoymousClass(), "annoymousclass impl");

        // 使用 Lambda 表达式创建并启动线程
        callableTest.execute(callableTest.createThreadByCallableLambda(), "lambda impl");

        // 打印主线程名称
        log.info("主线程名称=======>> {}", Thread.currentThread().getName());
    }

    /**
     * 执行指定的线程任务，并获取其返回值。
     * <p>
     * 该方法接收一个 {@link FutureTask} 对象和线程名称，启动线程后通过 {@link FutureTask#get()} 方法获取线程任务的执行结果。
     * 如果获取结果过程中发生异常，则会捕获并打印堆栈信息。
     * </p>
     *
     * @param futureTask 线程任务对象
     * @param threadName 线程名称
     */
    private void execute(FutureTask<String> futureTask, String threadName) {
        // 创建线程并启动
        Thread t = new Thread(futureTask, threadName);
        t.start();

        try {
            // 获取线程任务的执行结果
            log.info("从子线程[{}]中获取到的数据为=======>> {}", t.getName(), futureTask.get());
        } catch (Exception e) {
            // 捕获并打印异常
            e.printStackTrace();
        }
    }

    /**
     * 静态内部类，用于实现 {@link Callable} 接口。
     * <p>
     * 该类定义了一个简单的线程任务，任务逻辑为打印当前线程的名称并返回当前时间。
     * </p>
     */
    private static class MyCallableTask implements Callable<String> {

        /**
         * 定义线程启动后执行的任务逻辑。
         * <p>
         * 该方法会在调用 {@link FutureTask#get()} 方法时由 JVM 自动调用，
         * 并返回当前时间的字符串表示形式。
         * </p>
         *
         * @return 当前时间的字符串表示形式
         * @throws Exception 如果任务执行过程中发生异常
         */
        @Override
        public String call() throws Exception {
            // 打印当前线程的名称
            log.info("[{}] callable task is running by callable impl...", Thread.currentThread().getName());
            return LocalDateTime.now().toString();
        }
    }

    /**
     * 使用匿名类实现 {@link Callable} 接口创建线程任务。
     * <p>
     * 该方法返回一个新创建的 {@link FutureTask} 对象，线程任务逻辑为打印当前线程的名称并返回当前时间。
     * </p>
     *
     * @return 新创建的线程任务对象
     */
    public FutureTask<String> createThreadByCallableAnnoymousClass() {
        return new FutureTask<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                // 打印当前线程的名称
                log.info("[{}] callable task is running by callable annoymousclass...", Thread.currentThread().getName());
                return LocalDateTime.now().toString();
            }
        });
    }

    /**
     * 使用 Lambda 表达式实现 {@link Callable} 接口创建线程任务。
     * <p>
     * 该方法返回一个新创建的 {@link FutureTask} 对象，线程任务逻辑为打印当前线程的名称并返回当前时间。
     * </p>
     *
     * @return 新创建的线程任务对象
     */
    public FutureTask<String> createThreadByCallableLambda() {
        return new FutureTask<>(() -> {
            // 打印当前线程的名称
            log.info("[{}] callable task is running by callable lambda...", Thread.currentThread().getName());
            return LocalDateTime.now().toString();
        });
    }
}