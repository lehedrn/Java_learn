package com.coderlee.artconcurrentbook.chapter02;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.extern.slf4j.Slf4j;

/**
 * Counter 类用于演示多线程环境下的计数操作。
 * 该类提供了两种计数方法：一种是普通递增（存在线程安全问题），另一种是基于 CAS 的线程安全递增。
 */
@Slf4j
public class Counter {
    private int i = 0; // 普通计数器变量，非线程安全
    private AtomicInteger atomicI = new AtomicInteger(0); // 线程安全的计数器变量

    public static void main(String[] args) {
        final Counter counter = new Counter();
        List<Thread> ts = new ArrayList<>();
        long start = System.currentTimeMillis();

        // 创建 100 个线程，每个线程对计数器进行 1000 次递增操作
        for (int j = 0; j < 100; j++) {
            Thread thread = new Thread(() -> {
                for (int k = 0; k < 1000; k++) {
                    counter.increment(); // 普通递增操作
                    counter.safeIncrement(); // 线程安全递增操作
                }
            });
            ts.add(thread);
        }

        // 启动所有线程
        ts.forEach(Thread::start);

        // 等待所有线程执行完成
        ts.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // 打印执行时间和计数结果
        log.info("times: {} ms, i = {}, atomicI = {}", System.currentTimeMillis() - start, counter.i, counter.atomicI.get());
    }

    /**
     * 普通递增方法。
     * 由于没有同步机制，此方法在多线程环境下存在线程安全问题。
     */
    public void increment() {
        i++; // 非线程安全的递增操作
    }

    /**
     * 基于 CAS 的线程安全递增方法。
     * 使用 {@link AtomicInteger#compareAndSet(int, int)} 方法实现无锁的线程安全递增。
     */
    public void safeIncrement() {
        for (;;) {
            int current = atomicI.get(); // 获取当前值
            boolean suc = atomicI.compareAndSet(current, ++current); // 尝试更新值
            if (suc) { // 如果更新成功，则退出循环
                break;
            }
        }
    }
}