package com.coderlee.concurrent.threadgroup;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import lombok.extern.slf4j.Slf4j;

/**
 * SafeCount 是一个用于演示多线程环境下线程安全操作的类。
 * 该类包含一个共享变量 [value]，并通过 [nextValue] 方法对其进行递增操作。
 * 与 [UnsafeCount] 不同，[nextValue] 方法使用了 `synchronized` 关键字，
 * 确保在多线程场景下对共享变量的操作是线程安全的，从而避免竞态条件（Race Condition）问题。
 */
public class SafeCount {
    private long value = 0; // 共享变量，初始值为 0

    /**
     * 返回当前 [value] 的值，并将其递增 1。
     * 
     * @return 当前 [value] 的值
     * 注意：此方法使用了 `synchronized` 关键字进行同步，确保多线程环境下的安全性。
     */
    public synchronized long nextValue() {
        return value++; // 线程安全的递增操作
    }
}

@Slf4j
class SafeCountTest {
    /**
     * 主方法，用于启动多个线程测试 [SafeCount] 类的行为。
     * 每个线程都会调用 [nextValue] 方法，并打印当前线程名称和返回值。
     * 由于 [nextValue] 方法使用了 `synchronized` 关键字，
     * 因此可以保证结果的正确性和唯一性。
     */
    public static void main(String[] args) {
        SafeCount safeCount = new SafeCount(); // 创建 SafeCount 实例

        // 使用嵌套循环创建多个线程
        IntStream.range(0, 5).forEach(i -> { // 外层循环控制线程组编号
            IntStream.range(0, 5).forEach(j -> { // 内层循环控制每个线程组内的线程编号
                new Thread(() -> {
                    try {
                        TimeUnit.MILLISECONDS.sleep(300); // 模拟线程执行前的短暂延迟
                    } catch (InterruptedException e) {
                        e.printStackTrace(); // 捕获并打印中断异常
                    }
                    // 打印当前线程名称和调用 nextValue 方法的结果
                    log.info("{} next value is: {}", Thread.currentThread().getName(), safeCount.nextValue());
                }, i + "-" + j).start(); // 启动线程，并设置线程名称为 "i-j"
            });
        });
    }
}