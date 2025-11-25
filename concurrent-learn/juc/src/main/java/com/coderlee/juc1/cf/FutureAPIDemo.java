package com.coderlee.juc1.cf;

import com.coderlee.juc1.utils.SleepUtils;
import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * FutureAPIDemo - 演示 FutureTask API 使用方法和特性的示例类
 *
 * 本类通过三个不同的演示方法(demo1, demo2, demo3)展示了 FutureTask 的核心API使用方式，
 * 包括阻塞获取结果、超时获取结果以及轮询检查任务状态等方式，帮助理解 Future 模式的应用。
 */
@Slf4j
public class FutureAPIDemo {

    public static void main(String[] args) {
        // 主方法，调用不同的演示方法
//        demo1();  // 阻塞等待结果演示
//        demo2();  // 超时等待结果演示
        demo3();    // 轮询检查任务状态演示
    }

    /**
     * demo3 - 演示通过轮询方式检查任务执行状态
     *
     * 创建一个耗时5秒的任务，启动后主线程通过循环轮询 `isDone()` 方法检查任务是否完成，
     * 如果未完成则每隔500ms输出等待信息，完成后获取并输出结果。
     */
    private static void demo3() {
        // 创建 FutureTask 实例，包装一个耗时5秒的任务
        FutureTask<String> futureTask = new FutureTask<>(() -> {
            log.info("{} ---- come in", Thread.currentThread().getName());  // 记录任务线程进入日志
            SleepUtils.sleep(5000);  // 模拟耗时操作
            return "task over";  // 返回任务完成标识
        });
        Thread t = new Thread(futureTask);  // 创建线程执行 FutureTask
        t.start();  // 启动线程
        log.info("{} ------ 忙其他任务 ", Thread.currentThread().getName());  // 记录主线程继续处理其他任务

        // 循环轮询检查任务是否完成
        while (true) {
            if (futureTask.isDone()) {  // 检查任务是否已完成
                try {
                    log.info("task is over, result is {}", futureTask.get());  // 获取任务执行结果
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);  // 处理获取结果时的异常
                }
                break;  // 任务完成，退出循环
            } else {
                SleepUtils.sleep(500);  // 等待500ms后再次检查
                log.info("task is runing ,please wait");  // 输出等待提示信息
            }
        }
    }

    /**
     * demo2 - 演示带超时时间的获取任务结果
     *
     * 创建一个耗时5秒的任务，但设置最多等待3秒获取结果，超时将抛出 TimeoutException 异常。
     */
    private static void demo2() {
        // 创建 FutureTask 实例，包装一个耗时5秒的任务
        FutureTask<String> futureTask = new FutureTask<>(() -> {
            log.info("{} ---- come in", Thread.currentThread().getName());  // 记录任务线程进入日志
            SleepUtils.sleep(5000);  // 模拟耗时操作
            return "task over";  // 返回任务完成标识
        });
        Thread t = new Thread(futureTask);  // 创建线程执行 FutureTask
        t.start();  // 启动线程
        log.info("{} ------ 忙其他任务 ", Thread.currentThread().getName());  // 记录主线程继续处理其他任务

        // 限定时间获取任务结果，超时时间为3秒
        try {
            log.info("{} ------ get task result is {}", Thread.currentThread().getName(), futureTask.get(3, TimeUnit.SECONDS));
        } catch (Exception e) {
            throw new RuntimeException(e);  // 捕获并重新抛出超时等异常
        }
    }

    /**
     * demo1 - 演示阻塞方式获取任务结果
     *
     * 创建一个耗时5秒的任务，调用get()方法会一直阻塞直到任务完成并返回结果。
     */
    private static void demo1() {
        // 创建 FutureTask 实例，包装一个耗时5秒的任务
        FutureTask<String> futureTask = new FutureTask<>(() -> {
            log.info("{} ---- come in", Thread.currentThread().getName());  // 记录任务线程进入日志
            SleepUtils.sleep(5000);  // 模拟耗时操作
            return "task over";  // 返回任务完成标识
        });
        Thread t = new Thread(futureTask);  // 创建线程执行 FutureTask
        t.start();  // 启动线程
        log.info("{} ------ 忙其他任务 ", Thread.currentThread().getName());  // 记录主线程继续处理其他任务

        // 阻塞等待获取任务结果，直到任务完成
        try {
            log.info("{} ------ task result is {}", Thread.currentThread().getName(), futureTask.get());
        } catch (Exception e) {
            throw new RuntimeException(e);  // 处理获取结果时的异常
        }
    }

}
