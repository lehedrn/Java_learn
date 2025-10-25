package com.coderlee.concurrent.chapter07.jol;

import java.util.concurrent.TimeUnit;

import org.openjdk.jol.info.ClassLayout;

import lombok.extern.slf4j.Slf4j;

/**
 * ObjectLockAnalysis 类用于分析 Java 对象在不同锁状态下的内存布局变化。
 * 该类利用 `org.openjdk.jol` 工具库，通过打印对象的内存布局（ClassLayout）来观察：
 * - 普通锁行为对对象头的影响；
 * - 偏向锁（Biased Locking）的行为及其对对象头的影响。
 * 
 * 通过此分析可以更好地理解 JVM 中锁机制的实现细节和优化策略。
 */
@Slf4j
public class ObjectLockAnalysis {

    /**
     * 程序入口，调用不同的方法以展示普通锁和偏向锁的行为。
     * 默认情况下调用 [printBiasLock] 方法。
     */
    public static void main(String[] args) {
        // printNormalLock();
        printBiasLock();
    }

    /**
     * 打印普通锁（Non-Biased Lock）状态下对象头的变化。
     * 该方法展示了以下内容：
     * - 初始未加锁状态下的对象头；
     * - 在 `synchronized` 块中加锁后的对象头变化；
     * - 调用对象的 `hashCode` 方法后，对象头的进一步变化；
     * - 多次进入同步块时的锁升级情况。
     */
    @SuppressWarnings("unused")
    private static void printNormalLock() {
        MyObject obj = new MyObject(); // 创建一个测试对象

        // 打印初始状态的对象头，此时对象处于无锁状态
        log.info("Initial state: \n{}", ClassLayout.parseInstance(obj).toPrintable());

        synchronized (obj) { // 进入同步块，触发锁升级
            // 此时对象处于轻量级锁状态
            log.info("Inside synchronized block (first lock): \n{}", ClassLayout.parseInstance(obj).toPrintable());
            log.info("Object hashcode: {}", obj.hashCode()); // 计算 hashCode，可能影响对象头
            // 计算处于轻量级状态的对象的HashCode值，轻量级锁会膨胀为重量级锁
            log.info("After calling hashCode inside synchronized block: \n{}", ClassLayout.parseInstance(obj).toPrintable());
        }

        synchronized (obj) { 
            // 再次进入同步块，观察锁行为，此时对象处于重量级锁状态
            log.info("Inside synchronized block (second lock): \n{}", ClassLayout.parseInstance(obj).toPrintable());
        }

        // 打印退出同步块后的对象头，此时对象处于重量级状态
        log.info("After synchronized block: \n{}", ClassLayout.parseInstance(obj).toPrintable());
    }

    /**
     * 打印偏向锁（Biased Locking）状态下对象头的变化。
     * 偏向锁会在一定时间后启用，因此需要等待几秒以确保 JVM 偏向锁机制生效。
     * 该方法展示了以下内容：
     * - 偏向锁启用前的对象头；
     * - 在 `synchronized` 块中加锁后，偏向锁的状态变化；
     * - 调用对象的 `hashCode` 方法后，偏向锁被撤销的情况；
     * - 多次进入同步块时的锁行为变化。
     */
    @SuppressWarnings("unused")
    private static void printBiasLock() {
        try {
            // Java中的偏向锁在JVM启动几秒之后才会被激活，
            // 所以程序启动时等待几秒以确保 JVM 偏向锁机制生效
            // 否则会出现一些没必要的锁撤销
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace(); // 异常处理
        }

        MyObject obj = new MyObject(); // 创建一个测试对象

        // 打印初始状态的对象头，此时对象处于偏向锁状态
        log.info("Initial state with Biased Locking enabled: \n{}", ClassLayout.parseInstance(obj).toPrintable());

        synchronized (obj) { 
            // 进入同步块，此时对象处于偏向锁状态
            log.info("Inside synchronized block (first lock, biased): \n{}", ClassLayout.parseInstance(obj).toPrintable());
            log.info("Object hashcode: {}", obj.hashCode()); // 计算 hashCode
            // 计算处于偏向锁状态的对象的HashCode值，偏向锁会膨胀为重量级锁
            log.info("After calling hashCode inside synchronized block (biased revoked): \n{}", ClassLayout.parseInstance(obj).toPrintable());
        }

        synchronized (obj) { 
            // 再次进入同步块，此时对象处于重量级锁状态
            log.info("Inside synchronized block (second lock, after bias revoked): \n{}", ClassLayout.parseInstance(obj).toPrintable());
        }

        // 打印退出同步块后的对象头，此时对象处于重量级状态
        log.info("After synchronized block (final state): \n{}", ClassLayout.parseInstance(obj).toPrintable());
    }
}