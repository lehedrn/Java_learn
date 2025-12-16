package com.coderlee.juc1.volatiles;

import com.coderlee.juc1.utils.SleepUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * volatile关键字可见性演示类
 *
 * 该类用于演示Java中volatile关键字的作用，特别是其对变量可见性的保证。
 * 通过对比普通boolean变量与volatile boolean变量在多线程环境中的表现，
 * 展示volatile如何解决线程间变量修改不可见的问题。
 */
@Slf4j
public class VolatileSeeDemo {
    // 普通boolean变量，不具有volatile特性，在多线程环境下修改可能对其他线程不可见
    static boolean flag = true;

    // volatile boolean变量，具有可见性保证，修改后对所有线程立即可见
    static volatile boolean v_flag = true;

    public static void main(String[] args) {
//        runWithFlag();        // 取消注释此行可观察普通变量的不可见性问题
        runWithVolatileFlag();  // 执行volatile变量的可见性演示
    }

    /**
     * 通用运行方法，用于创建测试线程并执行变量可见性测试
     *
     * @param condition 条件提供者，返回循环判断条件
     * @param opt 操作消费者，执行变量修改操作
     * @param conditionName 条件名称，用于日志输出
     */
    public static void run(Supplier<Boolean> condition, Consumer<Void> opt, String conditionName) {
        // 创建并启动子线程，该线程会持续检查condition条件
        new Thread(() -> {
            log.info("{} -----> come in", Thread.currentThread().getName());
            // 循环检查条件，当条件变为false时退出循环
            while (condition.get()) {
                // 空循环体，仅用于演示变量可见性
            }
            log.info("{} -----> {} 被设置为false，程序停止", Thread.currentThread().getName(), conditionName);
        }, "t1").start();

        // 主线程休眠20毫秒，确保子线程已经启动并进入循环
        SleepUtils.sleep(20);

        // 主线程执行变量修改操作
        opt.accept(null);
        log.info("{} -----> 修改 {} 被设置为 {}", Thread.currentThread().getName(), conditionName, condition.get());
    }

    /**
     * 使用普通boolean变量进行可见性测试
     * 在多核CPU环境下，由于缓存一致性问题，子线程可能无法感知主线程对flag的修改
     */
    public static void runWithFlag() {
        run(() -> flag, x -> flag = false, "flag");
    }

    /**
     * 使用volatile boolean变量进行可见性测试
     * 由于volatile关键字的保证，子线程能够立即感知主线程对v_flag的修改
     */
    public static void runWithVolatileFlag() {
        run(() -> v_flag, x -> v_flag = false, "v_flag");
    }
}
