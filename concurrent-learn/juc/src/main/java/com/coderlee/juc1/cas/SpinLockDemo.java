package com.coderlee.juc1.cas;

import com.coderlee.juc1.utils.SleepUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 题目：实现一个自旋锁,复习CAS思想
 * 自旋锁好处：循环比较获取没有类似wait的阻塞。
 *
 * 通过CAS操作完成自旋锁，A线程先进来调用myLock方法自己持有锁5秒钟，B随后进来后发现
 * 当前有线程持有锁，所以只能通过自旋等待，直到A释放锁后B随后抢到。
 */
@Slf4j
public class SpinLockDemo {
    // 使用原子引用存储当前持有锁的线程，初始值为null表示锁未被占用
    private AtomicReference<Thread> atomicReference = new AtomicReference<>();

    public static void main(String[] args) {
        SpinLockDemo spinLockDemo = new SpinLockDemo();

        // 创建线程A，先获取锁并持有5秒
        new Thread(() -> {
            spinLockDemo.lock();
            // 模拟业务逻辑执行时间
            SleepUtils.sleep(5*1000);
            spinLockDemo.unlock();
        }, "A").start();

        // 主线程休眠0.5秒，确保线程A先获取锁
        SleepUtils.sleep(500);

        // 创建线程B，在线程A持有锁期间尝试获取锁
        new Thread(() -> {
            spinLockDemo.lock();
            // 模拟业务逻辑执行时间
            SleepUtils.sleep(1000);
            spinLockDemo.unlock();
        }, "B").start();
    }

    /**
     * 获取锁的方法
     * 使用CAS操作尝试将atomicReference从null设置为当前线程
     * 如果设置失败（说明锁已被其他线程持有），则继续循环尝试直到成功
     */
    public void lock() {
        Thread thread = Thread.currentThread();
        log.info("{} --------------- come in", thread.getName());
        // 自旋等待，直到成功获取锁
        while (!atomicReference.compareAndSet(null, thread)) {
            // CAS失败，继续下一次循环尝试
        }
        log.info("{} --------------- get lock", thread.getName());
    }

    /**
     * 释放锁的方法
     * 使用CAS操作将atomicReference从当前线程设置回null
     */
    public void unlock() {
        Thread thread = Thread.currentThread();
        // 使用CAS操作释放锁，将持有锁的线程引用置为null
        atomicReference.compareAndSet(thread, null);
        log.info("{} --------------- unlock", thread.getName());
    }
}
