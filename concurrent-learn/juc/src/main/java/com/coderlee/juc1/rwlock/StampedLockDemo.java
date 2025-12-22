package com.coderlee.juc1.rwlock;

import com.coderlee.juc1.utils.SleepUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.StampedLock;

/**
 * StampedLock 使用示例类
 * 演示了 StampedLock 的三种锁模式：写锁、悲观读锁和乐观读锁
 * 展示了 StampedLock 相比传统 ReadWriteLock 的优势
 */
@Slf4j
public class StampedLockDemo {
    /** 共享资源变量 */
    static int number = 1;

    /** StampedLock 实例，用于控制对共享资源的并发访问 */
    static StampedLock stampedLock = new StampedLock();

    public static void main(String[] args) {
        StampedLockDemo resource = new StampedLockDemo();
        // 演示悲观读锁与写锁的并发场景
        readAndWrite(resource);
        // 演示乐观读锁与写锁的交互
        optimisticReadAndWrite(resource, 2000, 4000);
        optimisticReadAndWrite(resource, 5000, 2000);
    }

    /**
     * 演示乐观读锁与写锁的并发场景
     * @param resource 资源对象
     * @param timeout1 第一次睡眠时间（毫秒），用于延迟启动写线程
     * @param timeout2 第二次睡眠时间（毫秒），用于等待所有操作完成
     */
    private static void optimisticReadAndWrite(StampedLockDemo resource, long timeout1, long timeout2) {
        // 启动乐观读线程
        new Thread(resource::tryOptimisticRead, "optimisticReadThread").start();
        // 睡眠指定时间后启动写线程
        SleepUtils.sleep(timeout1);
        new Thread(resource::write, "writeThread").start();
        // 继续睡眠并输出最终结果
        SleepUtils.sleep(timeout2);
        log.info("{} get the number result: {}", Thread.currentThread().getName(), number);
    }

    /**
     * 演示悲观读锁与写锁的并发场景
     * @param resource 资源对象
     */
    private static void readAndWrite(StampedLockDemo resource) {
        // 启动悲观读线程
        new Thread(resource::read, "readThread").start();
        SleepUtils.sleep(1000);
        // 启动写线程
        new Thread(resource::write, "writeThread").start();
        SleepUtils.sleep(4000);
        log.info("{} get the number result: {}", Thread.currentThread().getName(), number);
    }

    /**
     * 写操作 - 获取写锁并修改共享变量
     * 写锁是排他锁，获取后其他读写操作都无法进行
     */
    public void write() {
        // 获取写锁，返回时间戳
        long stamp = stampedLock.writeLock();
        log.info("{} 写线程准备修改", Thread.currentThread().getName());
        try {
            // 修改共享变量
            number = number + 2025;
        } finally {
            // 释放写锁
            stampedLock.unlockWrite(stamp);
        }
        log.info("{} 写线程修改完成", Thread.currentThread().getName());
    }

    /**
     * 悲观读操作 - 获取读锁并读取共享变量
     * 读锁是共享锁，多个读操作可以并发执行，但写操作会被阻塞
     */
    public void read() {
        // 获取悲观读锁
        long stamp = stampedLock.readLock();
        log.info("{} 读线程准备读取，需要4秒", Thread.currentThread().getName());
        // 模拟耗时读取过程
        for (int i = 0; i < 4; i++) {
            SleepUtils.sleep(1000);
            log.info("{} 读线程正在读取中", Thread.currentThread().getName());
        }
        try {
            // 读取共享变量
            int result = number;
            log.info("{} 读线程读取获取的变量值为：{}", Thread.currentThread().getName(), result);
            log.info("写线程没有修改成功，读锁时候写锁无法介入，传统的读写互斥");
        } finally {
            // 释放读锁
            stampedLock.unlockRead(stamp);
        }
    }

    /**
     * 乐观读操作 - 尝试乐观读取并验证数据一致性
     * 乐观读不会阻塞写操作，通过时间戳验证机制保证数据一致性
     */
    public void tryOptimisticRead() {
        // 尝试获取乐观读锁（无阻塞）
        long stamp = stampedLock.tryOptimisticRead();
        // 立即读取共享变量
        int result = number;
        log.info("{} 4秒前stampedLock.validate方法值(true无修改，false有修改): {}",
                 Thread.currentThread().getName(), stampedLock.validate(stamp));

        // 模拟耗时读取过程，在此期间可能有写操作发生
        for (int i = 0; i < 4; i++) {
            SleepUtils.sleep(1000);
            log.info("{} 读线程正在读取中... [{}] 秒后stampedLock.validate方法值(true无修改，false有修改): {}",
                     Thread.currentThread().getName(), i, stampedLock.validate(stamp));
        }

        // 验证乐观读期间是否有写操作发生
        if (!stampedLock.validate(stamp)) {
            log.info("{} 读线程在4秒内被写线程修改了变量值，乐观读锁无法获取，需要获取悲观读锁",
                     Thread.currentThread().getName());
            // 如果有写操作发生，则升级为悲观读锁
            stamp = stampedLock.readLock();
            try {
                log.info("{} 读线程获取悲观读锁成功", Thread.currentThread().getName());
                // 重新读取最新的值
                result = number;
                log.info("{} 读线程获取的变量值为：{}", Thread.currentThread().getName(), result);
            } finally {
                // 释放悲观读锁
                stampedLock.unlockRead(stamp);
            }
        }
        log.info("{} 读线程获取的变量值为：{}", Thread.currentThread().getName(), result);
    }
}
