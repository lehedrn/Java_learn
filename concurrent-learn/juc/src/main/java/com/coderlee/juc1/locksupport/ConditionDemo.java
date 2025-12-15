package com.coderlee.juc1.locksupport;

import com.coderlee.juc1.utils.SleepUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Condition 使用示例类
 * 演示了 Condition 的正确使用方式以及常见错误用法
 */
@Slf4j
public class ConditionDemo {
    public static void main(String[] args) {
        // 正确使用 Condition 的示例（已注释）
//        conditionRight();
        // 错误用法1：未获取锁就调用 Condition 方法（已注释）
//        conditionWrong1();
        // 错误用法2：线程执行顺序问题导致的潜在问题（当前运行）
        conditionWrong2();
    }

    /**
     * 正确使用 Condition 的示例
     * 展示了如何正确地使用 Condition 进行线程间通信
     */
    public static void conditionRight() {
        // 创建可重入锁
        Lock lock = new ReentrantLock();
        // 创建与锁关联的条件对象
        Condition condition = lock.newCondition();

        // 创建等待线程 t1
        new Thread(() -> {
            // 获取锁
            lock.lock();
            try {
                log.info("{} ----> come in", Thread.currentThread().getName());
                // 释放锁并进入等待状态，直到被其他线程唤醒
                condition.await();
                log.info("{} ----> 被唤醒", Thread.currentThread().getName());
            } catch (InterruptedException e) {
                // 处理中断异常
                log.error("{} ----> catch InterruptedException", Thread.currentThread().getName(), e);
            } finally {
                // 释放锁
                lock.unlock();
            }
        }, "t1").start();

        // 主线程休眠1秒，确保 t1 先执行并进入等待状态
        SleepUtils.sleep(1);

        // 创建唤醒线程 t2
        new Thread(() -> {
            // 获取锁
            lock.lock();
            try {
                // 唤醒等待在 condition 上的一个线程
                condition.signal();
                log.info("{} ----> 发送唤醒信号", Thread.currentThread().getName());
            } finally {
                // 释放锁
                lock.unlock();
            }
        }, "t2").start();
    }

    /**
     * 错误用法示例1：没有获取锁就调用 Condition 的方法
     * 这会导致 IllegalMonitorStateException 异常
     */
    public static void conditionWrong1() {
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        new Thread(() -> {
            // 错误：没有获取锁就尝试调用 await()
//            lock.lock();
            try {
                log.info("{} ----> come in", Thread.currentThread().getName());
                condition.await();  // 这里会抛出 IllegalMonitorStateException
                log.info("{} ----> 被唤醒", Thread.currentThread().getName());
            } catch (InterruptedException e) {
                log.error("{} ----> catch InterruptedException", Thread.currentThread().getName(), e);
            } finally {
                // 错误：没有获取锁就不应该释放锁
//                lock.unlock();
            }
        }, "t1").start();

        SleepUtils.sleep(1);

        new Thread(() -> {
            // 错误：没有获取锁就尝试调用 signal()
//            lock.lock();
            try {
                condition.signal();  // 这里会抛出 IllegalMonitorStateException
                log.info("{} ----> 发送唤醒信号", Thread.currentThread().getName());
            } finally {
                // 错误：没有获取锁就不应该释放锁
//                lock.unlock();
            }
        }, "t2").start();
    }

    /**
     * 错误用法示例2：线程执行时机不当
     * t2 线程可能在 t1 线程进入等待状态之前就执行完毕，导致唤醒信号丢失
     */
    public static void conditionWrong2() {
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        new Thread(() -> {
            // t1 线程先休眠1秒，可能导致 t2 先执行完
            SleepUtils.sleep(1);
            lock.lock();
            try {
                log.info("{} ----> come in", Thread.currentThread().getName());
                condition.await();  // 如果 t2 已经发送了信号，则这里永远不会被唤醒
                log.info("{} ----> 被唤醒", Thread.currentThread().getName());
            } catch (InterruptedException e) {
                log.error("{} ----> catch InterruptedException", Thread.currentThread().getName(), e);
            } finally {
                lock.unlock();
            }
        }, "t1").start();

        new Thread(() -> {
            lock.lock();
            try {
                condition.signal();  // 可能在 t1 进入等待前就执行了，信号丢失
                log.info("{} ----> 发送唤醒信号", Thread.currentThread().getName());
            } finally {
                lock.unlock();
            }
        }, "t2").start();
    }
}
