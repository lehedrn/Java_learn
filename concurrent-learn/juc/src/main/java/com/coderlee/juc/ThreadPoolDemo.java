package com.coderlee.juc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * ThreadPoolDemo 类演示了如何使用 Java 的线程池（ExecutorService）来管理并发任务。
 * 包含两个主要方法：
 * - handler1：展示共享任务的线程池使用，并可能涉及线程安全问题。
 * - handler2：展示通过 Future 获取异步任务结果的线程池使用。
 */
public class ThreadPoolDemo {

    public static void main(String[] args) {
        // 调用 handler2 方法，演示通过 Future 获取异步任务结果的场景
        handler2();
    }

    /**
     * handler2 方法展示了如何使用线程池提交多个任务并通过 Future 获取每个任务的结果。
     * 每个任务计算从 0 到 100 的累加和，并将结果存储在 Future 对象中。
     */
    public static void handler2() {
        // 创建一个固定大小为 5 的线程池
        ExecutorService pool = Executors.newFixedThreadPool(5);
        List<Future<Integer>> futures = new ArrayList<>();

        // 提交 10 个任务到线程池，每个任务计算从 0 到 100 的累加和
        for (int i = 0; i < 10; i++) {
            Future<Integer> future = pool.submit(() -> {
                int sum = 0;
                // 计算从 0 到 100 的累加和
                for (int j = 0; j <= 100; j++) {
                    sum += j;
                }
                return sum; // 返回累加和
            });
            futures.add(future); // 将 Future 对象添加到列表中
        }

        // 遍历所有 Future 对象，获取并打印每个任务的执行结果
        for (Future<Integer> future : futures) {
            try {
                System.out.println(future.get()); // 获取任务结果并打印
            } catch (Exception e) {
                e.printStackTrace(); // 捕获并打印异常
            }
        }

        pool.shutdown(); // 关闭线程池，释放资源
    }

    /**
     * handler1 方法展示了如何使用线程池提交共享任务。
     * 注意：由于任务是共享的，可能会引发线程安全问题。
     */
    public static void handler1() {
        // 创建一个固定大小为 5 的线程池
        ExecutorService pool = Executors.newFixedThreadPool(5);

        // 创建一个共享的 Runnable 任务实例
        ThreadRunnable tr = new ThreadRunnable();

        // 提交 10 次相同的任务到线程池
        for (int i = 0; i < 10; i++) {
            pool.submit(tr); // 提交共享任务
        }

        pool.shutdown(); // 关闭线程池，释放资源
    }
}

/**
 * ThreadRunnable 类实现了 Runnable 接口，定义了一个简单的任务。
 * 该任务会输出当前线程名称和一个递增值 i，直到 i 达到 100。
 */
class ThreadRunnable implements Runnable {
    private int i = 0; // 共享变量 i

    @Override
    public void run() {
        // 循环输出当前线程名称和递增值 i
        while (i <= 100) {
            System.out.println(Thread.currentThread().getName() + " : " + i++);
            // 注意：i 是共享变量，可能会引发线程安全问题
        }
    }
}