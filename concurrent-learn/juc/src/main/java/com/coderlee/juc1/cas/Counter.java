package com.coderlee.juc1.cas;

import com.coderlee.juc1.utils.SleepUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 计数器类，演示了两种线程安全的计数实现方式：
 * 1. 基于 AtomicInteger 的 CAS 操作（无锁）
 * 2. 基于 synchronized 关键字的加锁操作
 */
@Slf4j
public class Counter {

    /**
     * 使用 AtomicInteger 实现的线程安全计数器
     * 利用 CAS（Compare-And-Swap）机制保证原子性，避免使用锁
     */
    private AtomicInteger atomicInteger = new AtomicInteger(0);

    /**
     * 对 atomicInteger 进行递增操作
     * 使用 incrementAndGet 方法实现原子自增
     */
    public void atomicIncrement() {
        atomicInteger.incrementAndGet();
    }

    /**
     * 获取 atomicInteger 当前值
     * @return 当前计数值
     */
    public int getAtomicInteger() {
        return atomicInteger.get();
    }

    /**
     * 使用 volatile 修饰的整型变量，配合 synchronized 实现线程安全
     * volatile 保证可见性，synchronized 保证原子性
     */
    private volatile int volatileInteger = 0;

    /**
     * 对 volatileInteger 进行递增操作
     * 使用 synchronized 关键字确保同一时刻只有一个线程能执行此操作
     */
    public synchronized void volatileIncrement() {
        volatileInteger++;
    }

    /**
     * 获取 volatileInteger 当前值
     * @return 当前计数值
     */
    public int getVolatileInteger() {
        return volatileInteger;
    }

    /**
     * 主方法，用于启动测试程序
     * 分别测试 atomicInteger 和 volatileInteger 在多线程环境下的表现
     */
    public static void main(String[] args) {
        Counter counter = new Counter();
        // 测试基于 AtomicInteger 的计数器
        run(x -> counter.atomicIncrement(), counter::getAtomicInteger, "atomicInteger");
        // 测试基于 synchronized 的计数器
        run(x -> counter.volatileIncrement(), counter::getVolatileInteger, "volatileInteger");
    }

    /**
     * 启动多个线程对指定操作进行并发执行，并输出最终结果
     * @param opt 要执行的操作（如递增）
     * @param result 获取结果的方法
     * @param msg  日志消息标识符
     */
    public static void run(Consumer<Void> opt, Supplier<Integer> result, String msg) {
        // 创建并启动10个线程
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                // 每个线程执行1000次操作
                for (int j = 1; j <= 1000; j++) {
                    opt.accept(null);
                }
            }, String.valueOf(i)).start();
        }
        // 等待所有线程执行完毕
        SleepUtils.sleep(3*1000);
        // 输出最终结果
        log.info("{} = {}", msg, result.get());
    }
}

