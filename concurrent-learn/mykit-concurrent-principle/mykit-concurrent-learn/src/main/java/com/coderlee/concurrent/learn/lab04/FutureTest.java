package com.coderlee.concurrent.learn.lab04;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import lombok.extern.slf4j.Slf4j;

/**
 * FutureTest类用于演示如何使用`Future`接口获取异步任务的执行结果。
 * 通过线程池提交一个异步任务，并在主线程中等待任务完成以获取结果。
 */
@Slf4j
public class FutureTest {

    /**
     * 主方法，程序入口点。
     * <p>
     * 该方法展示了以下内容：
     * 1. 创建一个单线程的线程池。
     * 2. 提交一个实现了`Callable`接口的任务到线程池。
     * 3. 使用`Future.get()`方法阻塞主线程，直到异步任务完成并返回结果。
     * 4. 最后关闭线程池以释放资源。
     *
     * @param args 命令行参数（未使用）。
     */
    public static void main(String[] args) {
        // 创建一个单线程的线程池，用于执行异步任务
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        try {
            // 提交一个Callable任务，该任务返回一个字符串作为结果
            Future<String> future = executorService.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    // 模拟任务逻辑，返回当前时间戳的字符串
                    return "测试Future获取异步结果，时间[" + LocalDateTime.now().toString() + "]";
                }
            });

            // 阻塞主线程，等待异步任务完成并获取结果
            log.info("future result is: {}", future.get());
        } catch (InterruptedException | ExecutionException e) {
            // 捕获可能的异常，例如线程中断或任务执行异常
            e.printStackTrace();
        } finally {
            // 确保线程池被正确关闭，避免资源泄漏
            executorService.shutdown();
        }
    }
}