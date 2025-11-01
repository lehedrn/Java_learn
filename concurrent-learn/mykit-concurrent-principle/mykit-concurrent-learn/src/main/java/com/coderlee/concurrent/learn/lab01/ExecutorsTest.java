package com.coderlee.concurrent.learn.lab01;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import lombok.extern.slf4j.Slf4j;

/**
 * ExecutorsTest 类用于演示如何使用 {@link ExecutorService} 创建线程池并提交任务。
 * <p>
 * 该类展示了以下功能：
 * <ul>
 *   <li>创建固定大小的线程池。</li>
 *   <li>使用 {@link ExecutorService#submit(Runnable)} 提交无返回值的任务。</li>
 *   <li>使用 {@link ExecutorService#submit(Callable)} 提交有返回值的任务，并获取结果。</li>
 *   <li>使用 {@link ExecutorService#execute(Runnable)} 执行任务。</li>
 *   <li>正确关闭线程池以释放资源。</li>
 * </ul>
 * 
 * <h3>{@code execute} 和 {@code submit} 的区别</h3>
 * <p>
 * 在 Java 并发编程中，{@link ExecutorService#execute(Runnable)} 和 {@link ExecutorService#submit(Callable)} 是两种常见的任务提交方式，它们的主要区别如下：
 * </p>
 * <p>
 * 1. <b>方法签名与参数类型：</b><br>
 *    - {@code execute} 方法仅支持 {@link Runnable} 类型的任务，且无返回值（返回类型为 {@code void}）。<br>
 *    - {@code submit} 方法支持 {@link Runnable} 和 {@link Callable} 类型的任务，并返回一个 {@link Future} 对象，用于获取任务执行结果或状态。
 * </p>
 * <p>
 * 2. <b>返回值：</b><br>
 *    - {@code execute} 方法不提供任何返回值，适用于无需获取任务结果的场景。<br>
 *    - {@code submit} 方法返回一个 {@link Future} 对象，可以通过调用 {@link Future#get()} 获取任务的结果（对于 {@link Callable} 任务）或检查任务的状态（对于 {@link Runnable} 任务）。
 * </p>
 * <p>
 * 3. <b>异常处理：</b><br>
 *    - 使用 {@code execute} 方法时，如果任务执行过程中抛出异常，异常不会被捕获，可能会导致线程终止，开发者需要通过其他方式（如日志记录）捕获异常。<br>
 *    - 使用 {@code submit} 方法时，任务中的异常会被封装到 {@link Future} 对象中，调用 {@link Future#get()} 时会抛出 {@link ExecutionException}，开发者可以通过捕获该异常来处理任务中的错误。
 * </p>
 * <p>
 * 4. <b>适用场景：</b><br>
 *    - {@code execute} 方法适用于简单的异步任务执行，尤其是不需要返回值的场景。<br>
 *    - {@code submit} 方法适用于需要获取任务执行结果、处理任务异常或更精细控制任务状态的场景。
 * </p>
 */
@Slf4j
public class ExecutorsTest {
    /**
     * 线程池实例，用于执行并发任务。
     * <p>
     * 使用 {@link Executors#newFixedThreadPool(int)} 创建一个固定大小为 3 的线程池。
     */
    private static ExecutorService threadPool;

    static {
        // 初始化线程池，设置核心线程数为 3
        threadPool = Executors.newFixedThreadPool(3);
    }

    /**
     * 主方法，程序入口。
     * <p>
     * 该方法演示了以下操作：
     * <ol>
     *   <li>记录主线程名称。</li>
     *   <li>提交一个无返回值的任务到线程池。</li>
     *   <li>提交一个有返回值的任务到线程池，并获取线程名称作为结果。</li>
     *   <li>使用 {@link ExecutorService#execute(Runnable)} 执行另一个任务。</li>
     *   <li>调用 {@link ExecutorService#shutdown()} 方法关闭线程池。</li>
     * </ol>
     *
     * @param args 命令行参数（未使用）。
     */
    public static void main(String[] args) {
        // 记录主线程名称
        log.info("主线程名称==========>> [{}]", Thread.currentThread().getName());

        // 提交一个无返回值的任务到线程池
        threadPool.submit(() -> log.info("新创建的线程名称=============> [{}]", Thread.currentThread().getName()));

        try {
            // 提交一个有返回值的任务到线程池，并获取线程名称作为结果
            Future<String> future = threadPool.submit(() -> {
                log.info("新创建的线程名称=============> [{}]", Thread.currentThread().getName());
                return Thread.currentThread().getName();
            });
            log.info("从子线程中获取到的数据为===>> [{}]", future.get());
        } catch (InterruptedException | ExecutionException e) {
            // 捕获异常并打印堆栈信息
            e.printStackTrace();
        }

        // 使用 execute 方法执行任务
        threadPool.execute(() -> {
            log.info("新创建的线程名称===>> " + Thread.currentThread().getName());
        });

        // 关闭线程池以释放资源
        threadPool.shutdown();
    }
}