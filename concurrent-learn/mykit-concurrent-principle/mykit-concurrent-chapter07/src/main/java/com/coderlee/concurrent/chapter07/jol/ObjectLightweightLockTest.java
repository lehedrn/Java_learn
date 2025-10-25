package com.coderlee.concurrent.chapter07.jol;

import org.openjdk.jol.info.ClassLayout;

import lombok.extern.slf4j.Slf4j;

/**
 * ObjectLightweightLockTest 类用于演示 Java 对象在不同状态下的内存布局，
 * 特别是轻量级锁（Lightweight Locking）机制的表现。
 * 
 * 该类通过使用 `org.openjdk.jol` 工具库分析对象头信息，展示了对象在进入和退出同步块时的状态变化。
 */
@Slf4j
public class ObjectLightweightLockTest {

    /**
     * 主方法，程序入口点。
     * 
     * 1. 创建一个普通的 Java 对象 [MyObject](file:///home/workspace/coderlee/java_projects/Java_learn/concurrent-learn/mykit-concurrent-principle/mykit-concurrent-chapter07/src/main/java/com/coderlee/concurrent/chapter07/jol/MyObject.java#L6-L27)。
     * 2. 使用 `ClassLayout.parseInstance(obj).toPrintable()` 打印对象的初始内存布局。
     * 3. 在同步块中再次打印对象的内存布局，观察轻量级锁的效果。
     * 
     * @param args 命令行参数（未使用）
     */
    public static void main(String[] args) {
        // 创建一个普通对象，用于测试锁机制
        MyObject obj = new MyObject();

        // 打印对象的初始内存布局，此时对象应处于无锁状态
        log.info("\n{}", ClassLayout.parseInstance(obj).toPrintable());

        // 进入同步块，触发轻量级锁机制
        synchronized (obj) {
            // 打印对象在同步块中的内存布局，观察锁状态的变化
            log.info("Inside synchronized block: \n{}", ClassLayout.parseInstance(obj).toPrintable());
        }
    }
}
