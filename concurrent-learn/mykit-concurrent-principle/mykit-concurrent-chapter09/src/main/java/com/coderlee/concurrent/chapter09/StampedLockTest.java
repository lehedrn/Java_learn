package com.coderlee.concurrent.chapter09;

import java.util.concurrent.locks.StampedLock;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import lombok.extern.slf4j.Slf4j;

/**
 * StampedLockTest类用于演示StampedLock的基本用法。
 * 通过创建多个线程来模拟对共享资源的读写操作，展示StampedLock的读锁和写锁的获取与释放过程。
 * StampedLock是Java 8中引入的一种新的锁机制，提供了乐观读锁、悲观读锁以及写锁的支持，
 * 在某些场景下可以提供比ReentrantReadWriteLock更高的并发性能。
 */
@Slf4j
public class StampedLockTest {

    public static void main(String[] args) {
        StampedLockTest test = new StampedLockTest();
        // 执行5个线程抢占写锁的操作
        test.execute(5, v -> test.writeLockAndUnlock(), "开始抢占写锁");
        log.info("================================================================");
        // 执行10个线程抢占读锁的操作
        test.execute(10, v -> test.readLockAndUnlock(), "开始抢占读锁");
    }

    /**
     * execute方法用于创建指定数量的线程，并执行给定的操作。
     *
     * @param threadNums 线程的数量
     * @param consumer 每个线程需要执行的操作（如写锁或读锁的获取与释放）
     * @param msg 日志信息，描述当前线程的操作意图
     */
    public void execute(int threadNums, Consumer<Void> consumer, String msg) {
        Thread[] threads = new Thread[threadNums];
        IntStream.range(0, threadNums).forEach(i -> {
            threads[i] = new Thread(() -> {
                log.info("{} {}", Thread.currentThread().getName(), msg);
                consumer.accept(null); // 执行传入的操作
            }, String.valueOf(i));
            threads[i].start(); // 启动线程
        });
        IntStream.range(0, threadNums).forEach(i -> {
            try {
                threads[i].join(); // 等待线程执行完成
            } catch (InterruptedException e) {
                e.printStackTrace(); // 异常处理
            }
        });
    }

    /**
     * writeLockAndUnlock方法用于获取写锁并执行相关逻辑，最后释放写锁。
     * 写锁是独占锁，同一时刻只能有一个线程持有写锁。
     */
    public void writeLockAndUnlock() {
        long stamp = lock.writeLock(); // 获取写锁
        try {
            log.info("{} 抢占写锁成功", Thread.currentThread().getName());
        } finally {
            lock.unlockWrite(stamp); // 释放写锁
            log.info("{} 释放写锁成功", Thread.currentThread().getName());
        }
    }

    /**
     * readLockAndUnlock方法用于获取读锁并执行相关逻辑，最后释放读锁。
     * 读锁是共享锁，允许多个线程同时持有读锁，但不允许写锁介入。
     */
    public void readLockAndUnlock() {
        long stamp = lock.readLock(); // 获取读锁
        try {
            log.info("{} 抢占读锁成功", Thread.currentThread().getName());
        } finally {
            lock.unlockRead(stamp); // 释放读锁
            log.info("{} 释放读锁成功", Thread.currentThread().getName());
        }
    }

    private final StampedLock lock = new StampedLock(); // 定义StampedLock实例，用于控制读写锁
}