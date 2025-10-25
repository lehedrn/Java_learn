package com.coderlee.concurrent.chapter07.jol;

import org.openjdk.jol.info.ClassLayout;

import lombok.extern.slf4j.Slf4j;

/**
 * ObjectHeavyweightLockTest 类用于演示 Java 对象在不同状态下的内存布局变化，特别是对象头中的锁信息。
 * 通过使用 `org.openjdk.jol` 工具，观察对象在普通状态、加锁状态以及调用 `hashCode` 方法后的内存布局变化。
 * 
 * <p>该类的主要目的是帮助开发者理解 JVM 中的重量级锁（Heavyweight Lock）对对象内存布局的影响。</p>
 */
@Slf4j
public class ObjectHeavyweightLockTest {

    /**
     * main 方法是程序的入口，用于展示对象在不同操作下的内存布局变化。
     * 
     * <p>执行步骤如下：</p>
     * <ol>
     *   <li>创建一个普通的 Java 对象 [MyObject]。</li>
     *   <li>打印对象的初始内存布局。</li>
     *   <li>进入同步代码块，观察对象加锁后的内存布局变化。</li>
     *   <li>调用对象的 `hashCode` 方法，进一步观察内存布局的变化。</li>
     * </ol>
     * 
     * @param args 命令行参数（未使用）。
     */
    public static void main(String[] args) {
        // 创建一个普通的 Java 对象 [MyObject]
        MyObject obj = new MyObject();

        // 打印对象的初始内存布局，此时对象处于无锁状态
        log.info("\n{}", ClassLayout.parseInstance(obj).toPrintable());

        // 进入同步代码块，观察对象加锁后的内存布局变化
        synchronized (obj) {
            // 加锁后，此时锁状态会变为轻量级锁
            log.info("\n{}", ClassLayout.parseInstance(obj).toPrintable()); 

            // 计算处于轻量级锁状态的对象的HashCode值，轻量级锁会膨胀为重量级锁
            obj.hashCode();
            log.info("\n{}", ClassLayout.parseInstance(obj).toPrintable()); 
        }
    }
}