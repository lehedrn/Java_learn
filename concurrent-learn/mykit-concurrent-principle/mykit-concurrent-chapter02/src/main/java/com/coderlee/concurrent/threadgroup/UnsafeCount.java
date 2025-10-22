package com.coderlee.concurrent.threadgroup;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import lombok.extern.slf4j.Slf4j;

/**
 * UnsafeCount 是一个用于演示多线程环境下非线程安全操作的类。
 * 该类包含一个共享变量 [value]，并通过 [nextValue] 方法对其进行递增操作。
 * 由于递增操作未使用任何同步机制，因此在多线程场景下可能会出现竞态条件（Race Condition）问题，
 * 导致结果不符合预期。
 */
public class UnsafeCount {
    private long value = 0; // 共享变量，初始值为 0

    /**
     * 返回当前 [value] 的值，并将其递增 1。
     * 
     * @return 当前 [value] 的值
     * 注意：此方法非线程安全，可能会在多线程环境下导致数据竞争问题。
     */
    public long nextValue() {
        return value++; // 非线程安全的递增操作
    }
}

@Slf4j
class UnsafeCountTest {
    /**
     * 主方法，用于启动多个线程测试 [UnsafeCount] 类的行为。
     * 每个线程都会调用 [nextValue] 方法，并打印当前线程名称和返回值。
     * 由于 [nextValue] 方法未加锁，因此可能会观察到重复或不符合预期的结果。
     */
    public static void main(String[] args) {
        UnsafeCount unsafeCount = new UnsafeCount(); // 创建 UnsafeCount 实例

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
                    log.info("{} next value is: {}", Thread.currentThread().getName(), unsafeCount.nextValue());
                }, i + "-" + j).start(); // 启动线程，并设置线程名称为 "i-j"
            });
        });
    }
}