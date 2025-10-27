package com.coderlee.concurrent.chapter09;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

import lombok.extern.slf4j.Slf4j;

/**
 * CasCountIncrement 类演示了如何使用 CAS（Compare-And-Swap）操作来实现线程安全的计数器。
 * 该类通过 Unsafe 类的 compareAndSwapInt 方法实现了无锁的自增操作，避免了传统 synchronized 的开销。
 * 主要用于学习和理解并发编程中 CAS 操作的原理及应用。
 */
@Slf4j
public class CasCountIncrement {
    // 获取 Unsafe 实例以直接操作内存
    private static final Unsafe unsafe = getUnsafe();
    // 线程的数量
    private static final int THREAD_COUNT = 20;
    // 每个线程运行的次数
    private static final int EXECUTE_COUNT_EVERY_THREAD = 500;
    // 自增的计数值，使用 volatile 修饰以保证可见性
    private volatile int count = 0;
    // count 字段在内存中的偏移量，用于 CAS 操作
    private static long countOffset;

    static {
        try {
            // 获取 count 字段的偏移量
            countOffset = unsafe.objectFieldOffset(CasCountIncrement.class.getDeclaredField("count"));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取 Unsafe 实例。
     * 
     * @return Unsafe 实例
     */
    private static Unsafe getUnsafe() {
        Unsafe unsafe = null;
        try {
            // 通过反射获取 Unsafe 的单例对象
            Field singleoneInstanceField = Unsafe.class.getDeclaredField("theUnsafe");
            singleoneInstanceField.setAccessible(true);
            unsafe = (Unsafe) singleoneInstanceField.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return unsafe;
    }

    /**
     * 使用 CAS 操作对 count 值进行自增。
     * 通过循环不断尝试，直到成功完成原子性的自增操作。
     */
    public void incrementCountByCas() {
        // 将 count 的值赋值给 oldCount
        int oldCount = 0;
        do {
            oldCount = count; // 读取当前 count 的值
            // 使用 CAS 操作尝试将 count 从 oldCount 更新为 oldCount + 1
        } while (!unsafe.compareAndSwapInt(this, countOffset, oldCount, oldCount + 1));
    }

    /**
     * 主方法，模拟多线程环境下对 count 的并发自增操作。
     * 使用 CountDownLatch 确保所有线程执行完毕后再输出最终结果。
     *
     * @param args 命令行参数
     * @throws InterruptedException 如果线程等待时被中断
     */
    public static void main(String[] args) throws InterruptedException {
        CasCountIncrement casCountIncrement = new CasCountIncrement();
        // 使用 CountDownLatch 模拟并发环境
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

        // 创建 20 个线程并发执行
        IntStream.range(0, THREAD_COUNT).forEach((i) -> {
            new Thread(() -> {
                // 每个线程执行 500 次自增操作
                IntStream.range(0, EXECUTE_COUNT_EVERY_THREAD).forEach((j) -> {
                    casCountIncrement.incrementCountByCas();
                });
                // 当前线程任务完成，减少 latch 计数
                latch.countDown();
            }).start();
        });

        // 等待所有线程执行完毕
        latch.await();
        // 输出最终的 count 值
        log.info("count 的最终结果为: " + casCountIncrement.count);
    }
}