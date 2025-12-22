package com.coderlee.juc1.sync;

import com.coderlee.juc1.utils.SleepUtils;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

/**
 * 演示 synchronized 的不同锁升级过程
 * 包括无锁、偏向锁、轻量级锁(自旋锁)、重量级锁的状态变化
 */
@Slf4j
public class SynchronizedUpDemo {

    public static void main(String[] args) {
        // 可以取消注释不同的方法来观察各种锁状态
//        noLock();           // 无锁状态
//       biasedLock();        // 偏向锁
//         thinLock();        // 轻量级锁
        biase2weight();      // 偏向锁升级到重量级锁的过程
    }

    /**
     * 演示偏向锁升级为重量级锁的过程
     * 当持有偏向锁的对象调用 hashCode() 方法时，会撤销偏向锁并升级为重量级锁
     */
    private static void biase2weight() {
        // 使用vm参数进行立即生效：-XX:+UseBiasedLocking -XX:BiasedLockingStartupDelay=0
//        SleepUtils.sleep(5000);

        // 创建一个新的对象实例
        Object obj = new Object();

        // 打印当前线程名称和对象的内存布局信息（此时应该处于偏向锁状态）
        log.info("{} 此时处于偏向锁状态，对象内存信息: \n{}",
                Thread.currentThread().getName(),
                ClassLayout.parseInstance(obj).toPrintable());

        // 启动新线程尝试获取 obj 的锁
        new Thread(() -> {
            synchronized (obj) {
                // 调用 hashCode() 方法会导致偏向锁被撤销，升级为重量级锁
                obj.hashCode();

                // 打印调用 hashcode 后的对象内存布局信息（此时已升级为重量级锁）
                log.info("{} 调用hashcode后，偏向锁遇到hashcode计算请求，立马撤销偏向锁模式，膨胀为重量级锁，对象内存信息: \n{}",
                        Thread.currentThread().getName(),
                        ClassLayout.parseInstance(obj).toPrintable());
            }
        }, "t1").start();
    }

    /**
     * 演示轻量级锁状态
     * 通过 JVM 参数 -XX:-UseBiasedLocking 关闭偏向锁后，直接进入轻量级锁状态
     */
    private static void thinLock() {
        // 使用vm参数设置关闭偏向锁，立即进入轻量级锁：-XX:-UseBiasedLocking

        // 创建一个新的对象实例
        Object obj = new Object();

        // 打印主线程名称和初始对象内存布局信息
        log.info("{} 对象内存信息: \n{}",
                Thread.currentThread().getName(),
                ClassLayout.parseInstance(obj).toPrintable());

        // 启动新线程获取 obj 的锁
        new Thread(() -> {
            synchronized (obj) {
                // 打印获取锁后的对象内存布局信息（应显示轻量级锁标记）
                log.info("{} 对象内存信息: \n{}",
                        Thread.currentThread().getName(),
                        ClassLayout.parseInstance(obj).toPrintable());
            }
        }, "t1").start();
    }

    /**
     * 演示偏向锁状态
     * JDK 8 中默认开启偏向锁，但有约4秒延迟，可通过 JVM 参数调整
     */
    private static void biasedLock() {
        // intx BiasedLockingStartupDelay = 4000
        // 在jdk8中，偏向锁默认是开启的，并且有4秒的延迟，所以先等待5秒，直到偏向锁生效
        // 也可以使用vm参数进行立即生效：-XX:+UseBiasedLocking -XX:BiasedLockingStartupDelay=0
        // SleepUtils.sleep(5000);

        // 创建一个新的对象实例
        Object obj = new Object();

        // 打印当前线程名称和对象内存布局信息（应显示偏向锁状态）
        log.info("{} 对象内存信息: \n{}",
                Thread.currentThread().getName(),
                ClassLayout.parseInstance(obj).toPrintable());

        // 启动新线程获取 obj 的锁
        new Thread(() -> {
            synchronized (obj) {
                // 打印获取锁后的对象内存布局信息（仍应显示偏向锁状态）
                log.info("{} 对象内存信息: \n{}",
                        Thread.currentThread().getName(),
                        ClassLayout.parseInstance(obj).toPrintable());
            }
        }, "t1").start();
    }

    /**
     * 演示无锁状态
     * 新创建的对象在未加锁前处于无锁状态
     */
    private static void noLock() {
        // 创建一个新的对象实例
        Object obj = new Object();

        // 打印对象的内存布局信息（初始为无锁状态）
        log.info("对象内存信息: \n{}", ClassLayout.parseInstance(obj).toPrintable());

        // 打印对象的哈希码值（十进制、十六进制、二进制形式）
        log.info("10进制：{}", obj.hashCode());
        log.info("16进制：{}", Integer.toHexString(obj.hashCode()));
        log.info("2进制：{}", Integer.toBinaryString(obj.hashCode()));

        // 再次打印对象内存布局信息（哈希码已被存储在对象头中）
        log.info("对象内存信息: \n{}", ClassLayout.parseInstance(obj).toPrintable());
    }
}
