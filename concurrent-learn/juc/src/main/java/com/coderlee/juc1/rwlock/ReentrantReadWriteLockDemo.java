package com.coderlee.juc1.rwlock;

import com.coderlee.juc1.utils.SleepUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

/**
 * ReentrantReadWriteLock读写锁演示类
 * 展示了ReentrantReadWriteLock相比普通ReentrantLock在读操作上的优势
 */
public class ReentrantReadWriteLockDemo {
    public static void main(String[] args) {
        // lockDemo();      // 演示ReentrantLock的互斥特性
        // rwLockDemo();    // 演示ReentrantReadWriteLock的基本读写特性
        rwLockDemo2();      // 演示ReentrantReadWriteLock中读锁未释放时写锁等待的情况
    }

    /**
     * 演示ReentrantReadWriteLock中读锁未释放时写锁等待的情况
     * 启动多个写线程和长时间读线程，然后在读线程仍在执行时启动新写线程
     * 验证写锁必须等待所有读锁释放后才能获取
     */
    private static void rwLockDemo2() {
        MyResource myResource = new MyResource();

        doWriteAndRead(x -> myResource.writeByRwLock(x, x), myResource::readByRwLock1);

        // 等待1秒，确保上面的读线程已经开始执行
        SleepUtils.sleep(1000);

        // 启动新的写线程，这些写线程需要等待上面的读线程完成后才能执行
        for (int i = 1; i <= 3; i++) {
            int finalI = i;
            new Thread(() -> myResource.writeByRwLock(String.valueOf(finalI), String.valueOf(finalI)), "new write -> " + String.valueOf(i)).start();
        }
    }

    /**
     * 演示ReentrantReadWriteLock的基本读写特性
     * - 读写、写写、写读操作是互斥的
     * - 多个读操作可以并发执行
     */
    private static void rwLockDemo() {
        MyResource myResource = new MyResource();
        doWriteAndRead(x -> myResource.writeByRwLock(x, x), myResource::readByRwLock);
    }

    /**
     * 演示使用普通ReentrantLock进行读写操作
     * 所有操作（读读、读写、写写）都是互斥的，性能较低
     */
    private static void lockDemo() {
        MyResource myResource = new MyResource();
        doWriteAndRead(x -> myResource.write(x, x), myResource::read);
    }

    /**
     * 通用的读写操作执行方法
     * @param writerOpt 写操作函数
     * @param readerOpt 读操作函数
     */
    private static void doWriteAndRead(Consumer<String> writerOpt, Consumer<String> readerOpt) {
        // 启动10个写线程
        for (int i = 1; i <= 10; i++) {
            int finalI = i;
            new Thread(() -> writerOpt.accept(String.valueOf(finalI)), String.valueOf(i)).start();
        }

        // 启动10个读线程
        for (int i = 1; i <= 10; i++) {
            int finalI = i;
            new Thread(() -> readerOpt.accept(String.valueOf(finalI)), String.valueOf(i)).start();
        }
    }
}

/**
 * 资源类，包含共享数据和各种同步方法
 * 提供了使用不同锁机制的读写方法实现
 */
@Slf4j
class MyResource {
    // 共享数据存储
    Map<String, String> map = new HashMap<>();

    // 普通重入锁
    Lock lock = new ReentrantLock();

    // 读写锁
    ReadWriteLock rwLock = new ReentrantReadWriteLock();

    /**
     * 使用读写锁的写操作
     * @param key 键
     * @param value 值
     */
    public void writeByRwLock(String key, String value) {
        rwLock.writeLock().lock();  // 获取写锁
        try {
            log.info("{} 正在写入", Thread.currentThread().getName());
            map.put(key, value);
            SleepUtils.sleep(500);  // 模拟写操作耗时
            log.info("{} 写入完成", Thread.currentThread().getName());
        } finally {
            rwLock.writeLock().unlock();  // 释放写锁
        }
    }

    /**
     * 使用读写锁的标准读操作
     * @param key 键
     */
    public void readByRwLock(String key) {
        rwLock.readLock().lock();  // 获取读锁
        try {
            log.info("{} 正在读取", Thread.currentThread().getName());
            String result = map.get(key);
            SleepUtils.sleep(200);  // 模拟读操作耗时
            log.info("{} 完成读取, result = {}", Thread.currentThread().getName(), result);
        } finally {
            rwLock.readLock().unlock();  // 释放读锁
        }
    }

    /**
     * 使用读写锁的长时读操作（用于演示写锁等待读锁释放）
     * @param key 键
     */
    public void readByRwLock1(String key) {
        rwLock.readLock().lock();  // 获取读锁
        try {
            log.info("{} 正在读取", Thread.currentThread().getName());
            String result = map.get(key);
            // 为了模拟读锁没有完成前，写锁无法获得
            SleepUtils.sleep(2000);  // 较长的读操作时间
            log.info("{} 完成读取, result = {}", Thread.currentThread().getName(), result);
        } finally {
            rwLock.readLock().unlock();  // 释放读锁
        }
    }

    /**
     * 使用普通重入锁的写操作
     * @param key 键
     * @param value 值
     */
    public void write(String key, String value) {
        lock.lock();  // 获取锁
        try {
            log.info("{} 正在写入", Thread.currentThread().getName());
            map.put(key, value);
            SleepUtils.sleep(500);  // 模拟写操作耗时
            log.info("{} 写入完成", Thread.currentThread().getName());
        } finally {
            lock.unlock();  // 释放锁
        }
    }

    /**
     * 使用普通重入锁的读操作
     * @param key 键
     */
    public void read(String key) {
        lock.lock();  // 获取锁
        try {
            log.info("{} 正在读取", Thread.currentThread().getName());
            String result = map.get(key);
            SleepUtils.sleep(200);  // 模拟读操作耗时
            log.info("{} 完成读取, result = {}", Thread.currentThread().getName(), result);
        } finally {
            lock.unlock();  // 释放锁
        }
    }
}
