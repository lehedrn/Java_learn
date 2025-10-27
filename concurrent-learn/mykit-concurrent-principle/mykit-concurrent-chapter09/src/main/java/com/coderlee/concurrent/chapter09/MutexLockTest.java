package com.coderlee.concurrent.chapter09;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.IntStream;

import lombok.extern.slf4j.Slf4j;

/**
 * 互斥锁/排它锁演示
 * MutexLockTest 类用于演示如何使用 `ReadWriteLock` 中的写锁（`writeLock`）来实现线程间的互斥操作。
 * 本类通过创建多个线程尝试获取写锁，展示锁的抢占与释放过程，并模拟简单的业务处理逻辑。
 */
@Slf4j
public class MutexLockTest {

    /**
     * 程序入口方法。
     * 创建 5 个线程，每个线程尝试获取写锁并执行 {@link #lockAndUnlock()} 方法。
     * 
     * @param args 命令行参数（未使用）
     */
    public static void main(String[] args) {
        MutexLockTest test = new MutexLockTest();
        IntStream.range(0, 5).forEach(i -> {
            new Thread(() -> {
                log.info("{} 开始抢占锁", Thread.currentThread().getName());
                test.lockAndUnlock(); // 调用核心方法进行锁操作
            }, "Thread-" + i).start();
        });
    }

    /**
     * 获取写锁并模拟业务处理，最后释放锁。
     * <p>
     * 该方法展示了以下步骤：
     * <ol>
     *   <li>获取写锁</li>
     *   <li>模拟业务逻辑处理</li>
     *   <li>确保在 finally 块中释放锁以避免死锁</li>
     * </ol>
     */
    public void lockAndUnlock() {
        lock.lock(); // 获取写锁
        try {
            log.info("{} 抢占锁成功", Thread.currentThread().getName());
            // 模拟业务处理，线程休眠 1 秒
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            // 捕获线程中断异常
            log.error("{} 被中断", Thread.currentThread().getName());
        } finally {
            lock.unlock(); // 确保锁在任何情况下都被释放
            log.info("{} 锁释放成功", Thread.currentThread().getName());
        }
    }

    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock(); // 定义读写锁实例
    private Lock lock = readWriteLock.writeLock(); // 获取写锁实例用于互斥操作
}
