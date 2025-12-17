package com.coderlee.juc1.atomics;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 原子整数操作演示类
 * 展示AtomicInteger在多线程环境中的线程安全特性
 */
@Slf4j
public class AtomicIntegerDemo {
    // 定义线程数量常量
    private static final int THREAD_COUNT = 50;

    public static void main(String[] args) {
        // 创建MyNumber实例，包含共享的AtomicInteger变量
        MyNumber myNumber = new MyNumber();

        // 使用CountDownLatch确保所有线程执行完毕后再输出结果
        CountDownLatch countDownLatch = new CountDownLatch(THREAD_COUNT);

        // 启动50个线程
        for (int i = 0; i < THREAD_COUNT; i++) {
            new Thread(() -> {
                // 每个线程对共享变量执行1000次递增操作
                for (int j = 1; j <= 1000; j++) {
                    myNumber.addPlusPlus();
                }
                // 线程执行完成后计数器减1
                countDownLatch.countDown();
            }, String.valueOf(i)).start();
        }

        try {
            // 等待所有线程执行完成
            countDownLatch.await();
        } catch (InterruptedException e) {
            log.error("countDownLatch error", e);
        }

        // 输出最终结果，预期值为50000(50线程*1000次操作)
        log.info("atomicInteger.get() = {}", myNumber.atomicInteger.get());
    }
}

/**
 * 包装AtomicInteger的工具类
 * 提供线程安全的整数递增操作
 */
class MyNumber {
    // 原子整数变量，默认初始值为0
    AtomicInteger atomicInteger = new AtomicInteger(0);

    /**
     * 线程安全的递增操作方法
     * 相当于++i操作，先递增再返回新值
     */
    public void addPlusPlus() {
        atomicInteger.getAndIncrement();
    }
}
