package com.coderlee.juc1.cf;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * FutureTask 基础使用示例类
 * 演示了 FutureTask 的两种构造方式及其使用方法
 *
 * FutureTask 是一种可取消的异步计算任务，它实现了 Future 和 Runnable 接口
 * 可以包装 Callable 或 Runnable 对象，并在线程中执行
 */
@Slf4j
public class FutureDemo {
    public static void main(String[] args) {
        // 使用 Callable 构造 FutureTask
        FutureTask<String> ct = new FutureTask<>(new CasllableTask());
        new Thread(ct).start();
        try {
            // 阻塞等待任务完成并获取结果
            String ctRes = ct.get();
            log.info("ct result is {}", ctRes);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        // 使用 Runnable 构造 FutureTask，指定任务完成后的返回值
        FutureTask<String> rt = new FutureTask<>(new RunableTask(), "runable task is done");
        new Thread(rt).start();
        try {
            // 阻塞等待任务完成并获取预设的返回值
            String rtRes = rt.get();
            log.info("rt result is {}", rtRes);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}

/**
 * 实现 Callable 接口的任务类
 * Callable 与 Runnable 不同之处在于它可以返回结果并抛出异常
 */
@Slf4j
class CasllableTask implements Callable<String> {

    /**
     * 执行具体的业务逻辑
     *
     * @return 返回任务执行结果
     * @throws Exception 可能抛出的异常
     */
    @Override
    public String call() throws Exception {
        log.info("{} callable task is running", Thread.currentThread().getName());
        // 返回执行结果
        return "callable";
    }
}

/**
 * 实现 Runnable 接口的任务类
 * Runnable 不能返回结果，适合不需要返回值的任务
 */
@Slf4j
class RunableTask implements Runnable {

    /**
     * 执行具体的业务逻辑
     * 此方法无返回值
     */
    @Override
    public void run() {
        log.info("{} runnable task is running", Thread.currentThread().getName());
        // 执行具体任务逻辑
    }
}
