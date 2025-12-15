package com.coderlee.juc1.locksupport;

import com.coderlee.juc1.utils.SleepUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 演示基于Object监视器的线程等待/唤醒机制
 * 展示如何使用 synchronized + wait()/notify() 实现线程间通信
 * 包含正确使用方式和常见错误示例
 */
@Slf4j
public class SyncWaitNotifyDemo {
    public static void main(String[] args) {
        // 执行正确的等待/唤醒示例
//        syncWaitNotify();
        // 执行缺少同步块的错误示例
//        syncWaitNotifyWrong1();
        // 执行唤醒早于等待的错误示例
        syncWaitNotifyWrong2();
    }

    /**
     * 演示了传统的基于Object监视器的线程等待/唤醒机制
     * 使用 synchronized + wait()/notify() 实现线程间通信
     */
    public static void syncWaitNotify() {
        // 创建用于线程同步的对象锁
        Object objectLock = new Object();

        // 创建并启动等待线程 t1
        new Thread(() -> {
            // 获取对象锁
            synchronized (objectLock) {
                log.info("{} --- come in", Thread.currentThread().getName());
                try {
                    log.info("{} --- wait begin", Thread.currentThread().getName());
                    // 释放锁并进入等待状态，直到被其他线程唤醒
                    objectLock.wait();
                    log.info("{} --- wait end", Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    // 处理中断异常情况
                    log.error("{} --- wait error", Thread.currentThread().getName(), e);
                }
                // 被唤醒后继续执行
                log.info("{} --- 被唤醒", Thread.currentThread().getName());
            }
        }, "t1").start();

        // 主线程休眠1秒，确保t1先获取到锁并进入等待状态
        SleepUtils.sleep(1);

        // 创建并启动唤醒线程 t2
        new Thread(() -> {
            // 获取相同的对象锁
            synchronized (objectLock) {
                log.info("{} --- 发起唤醒线程t1通知", Thread.currentThread().getName());
                // 唤醒所有在该对象上等待的线程
                objectLock.notifyAll();
            }
        }, "t2").start();
    }

    /**
     * 错误示例1：演示wait和notify方法必须要在同步代码块或者方法里面使用
     * 如果不在synchronized块中调用会抛出IllegalMonitorStateException异常
     */
    public static void syncWaitNotifyWrong1() {
        // 创建用于线程同步的对象锁
        Object objectLock = new Object();

        // 创建并启动等待线程 t1（缺少synchronized同步块）
        new Thread(() -> {
            log.info("{} --- come in", Thread.currentThread().getName());
            try {
                log.info("{} --- wait begin", Thread.currentThread().getName());
                // 错误：没有在synchronized块中调用wait()
                objectLock.wait();
                log.info("{} --- wait end", Thread.currentThread().getName());
            } catch (InterruptedException e) {
                log.error("{} --- wait error", Thread.currentThread().getName(), e);
            }
            log.info("{} --- 被唤醒", Thread.currentThread().getName());
        }, "t1").start();

        // 主线程休眠1秒
        SleepUtils.sleep(1);

        // 创建并启动唤醒线程 t2（缺少synchronized同步块）
        new Thread(() -> {
            log.info("{} --- 发起唤醒线程t1通知", Thread.currentThread().getName());
            // 错误：没有在synchronized块中调用notifyAll()
            objectLock.notifyAll();
        }, "t2").start();
    }

    /**
     * 错误示例2：演示wait和notify方法必须先wait再notify才是正确的
     * 如果先执行notify再执行wait，唤醒信号会丢失，导致线程永远等待
     */
    public static void syncWaitNotifyWrong2() {
        // 创建用于线程同步的对象锁
        Object objectLock = new Object();

        // 创建并启动等待线程 t1
        new Thread(() -> {
            // 延迟1秒执行，使得notify操作先于wait执行
            SleepUtils.sleep(1);
            // 获取对象锁
            synchronized (objectLock) {
                log.info("{} --- come in", Thread.currentThread().getName());
                try {
                    log.info("{} --- wait begin", Thread.currentThread().getName());
                    // 等待唤醒（但此时唤醒信号已经发出并丢失）
                    objectLock.wait();
                    log.info("{} --- wait end", Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    log.error("{} --- wait error", Thread.currentThread().getName(), e);
                }
                log.info("{} --- 被唤醒", Thread.currentThread().getName());
            }
        }, "t1").start();

        // 创建并启动唤醒线程 t2
        new Thread(() -> {
            // 立即获取对象锁并发送唤醒信号
            synchronized (objectLock) {
                log.info("{} --- 发起唤醒线程t1通知", Thread.currentThread().getName());
                // 发送唤醒信号（但此时t1还未开始等待）
                objectLock.notifyAll();
            }
        }, "t2").start();
    }
}
