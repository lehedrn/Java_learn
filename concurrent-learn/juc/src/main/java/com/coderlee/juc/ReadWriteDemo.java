package com.coderlee.juc;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.IntStream;

/**
 * ReadWriteDemo 类演示了如何使用 `ReadWriteLock` 实现线程安全的读写操作。
 * 主要场景：多个线程同时读取共享资源，但只有一个线程可以修改资源。
 * 写写/读写    需要互斥
 * 读读         不需要互斥
 */
public class ReadWriteDemo {
    public static void main(String[] args) {
        // 创建 RWDemo 实例，用于管理共享变量 number 的读写操作
        RWDemo rw = new RWDemo();

        // 启动一个写线程，随机生成一个整数并更新共享变量 number
        new Thread(() -> {
            rw.set((int) (Math.random() * 100 + 1));
        }, "Write-thread").start();

        // 启动 200 个读线程，每个线程读取共享变量 number 的值
        IntStream.range(0, 200).forEach(i -> {
            new Thread(() -> {
                rw.get();
            }, "Read-thread-" + i).start();
        });
    }
}

/**
 * RWDemo 类封装了一个共享变量 number 和其对应的读写操作。
 * 使用 ReentrantReadWriteLock 实现读写锁，确保线程安全。
 */
class RWDemo {

    private int number = 0; // 共享变量，存储当前值
    private ReadWriteLock lock = new ReentrantReadWriteLock(); // 读写锁实例

    /**
     * 读取共享变量 number 的值。
     * 多个线程可以同时读取（共享锁），但会阻塞写操作。
     */
    public void get() {
        lock.readLock().lock(); // 获取读锁
        try {
            // 打印当前线程名称和读取到的 number 值
            System.out.println(Thread.currentThread().getName() + " : reader number : " + number);
        } finally {
            lock.readLock().unlock(); // 确保读锁释放，避免死锁
        }
    }

    /**
     * 更新共享变量 number 的值。
     * 写操作是独占的，同一时间只有一个线程可以执行写操作。
     *
     * @param number 新的值，用于更新共享变量
     */
    public void set(int number) {
        lock.writeLock().lock(); // 获取写锁
        try {
            this.number = number; // 更新共享变量
            // 打印当前线程名称和写入的 number 值
            System.out.println(Thread.currentThread().getName() + " : writer number : " + number);
        } finally {
            lock.writeLock().unlock(); // 确保写锁释放，避免死锁
        }
    }
}