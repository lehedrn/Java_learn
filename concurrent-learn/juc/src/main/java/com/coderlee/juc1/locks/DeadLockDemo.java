package com.coderlee.juc1.locks;

import com.coderlee.juc1.utils.SleepUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 死锁演示类
 *
 * 该类演示了多线程环境下由于资源获取顺序不当导致的死锁现象。
 * 两个线程分别持有一个对象锁，并试图获取对方持有的锁，从而造成相互等待，
 * 形成无法继续执行的状态。
 */
@Slf4j
public class DeadLockDemo {

    /**
     * 程序入口点
     * 创建两个线程并启动，演示死锁情况的发生
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 定义两个共享资源对象作为锁
        final Object objA = new Object();
        final Object objB = new Object();

        // 线程A：先获取objA锁，再尝试获取objB锁
        new Thread(() -> {
            // 获取objA对象锁
            synchronized (objA) {
                // 输出当前线程名称及想要获取的锁信息
                log.info("{} 自己持有锁{}，希望获得锁{}", Thread.currentThread().getName(), "A", "B");
                // 模拟耗时操作，增加死锁概率
                SleepUtils.sleep(1000);
                // 尝试获取objB对象锁（此时会被阻塞）
                synchronized (objB) {
                    // 如果能执行到这里说明成功获取到了objB锁
                    log.info("{} 成功拿到锁 {}", Thread.currentThread().getName(), "B");
                }
            }
        }, "A").start();

        // 线程B：先获取objB锁，再尝试获取objA锁
        new Thread(() -> {
            // 获取objB对象锁
            synchronized (objB) {
                // 输出当前线程名称及想要获取的锁信息
                log.info("{} 自己持有锁{}，希望获得锁{}", Thread.currentThread().getName(), "B", "A");
                // 模拟耗时操作，增加死锁概率
                SleepUtils.sleep(1000);
                // 尝试获取objA对象锁（此时会被阻塞）
                synchronized (objA) {
                    // 如果能执行到这里说明成功获取到了objA锁
                    log.info("{} 成功拿到锁 {}", Thread.currentThread().getName(), "A");
                }
            }
        }, "B").start();
    }
}
