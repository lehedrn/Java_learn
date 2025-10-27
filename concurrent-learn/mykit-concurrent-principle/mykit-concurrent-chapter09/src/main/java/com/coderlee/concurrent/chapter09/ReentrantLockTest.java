package com.coderlee.concurrent.chapter09;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import lombok.extern.slf4j.Slf4j;

/**
 * ReentrantLockTest 类用于演示和对比 `ReentrantLock` 和 `synchronized` 的行为差异。
 * <p>
 * 该类通过多个方法展示了锁的可重入性以及锁释放的顺序，分别使用了以下三种方式：
 * <ul>
 *     <li>基于 {@link java.util.concurrent.locks.ReentrantLock} 的显式锁机制。</li>
 *     <li>基于 `synchronized` 关键字的隐式锁机制。</li>
 *     <li>基于 `synchronized` 方法和代码块混合使用的锁机制。</li>
 * </ul>
 * 每种方式都通过多线程的抢占与释放锁操作，验证其在嵌套锁定和解锁时的行为一致性。
 */
@Slf4j
public class ReentrantLockTest {

    /**
     * 主方法，用于执行不同锁机制的测试。
     * <p>
     * 分别调用以下三种方法进行测试：
     * <ul>
     *     <li>{@link #lockAndUnlock()}：基于 `ReentrantLock` 的显式锁。</li>
     *     <li>{@link #lockAndUnlockBySync()}：基于 `synchronized` 的隐式锁。</li>
     *     <li>{@link #lockAndUnlockBySync2()}：基于 `synchronized` 方法和代码块的混合锁。</li>
     * </ul>
     *
     * @param args 命令行参数（未使用）。
     */
    public static void main(String[] args) {
        ReentrantLockTest test = new ReentrantLockTest();

        log.info("====================lock===============================");
        // 测试基于 ReentrantLock 的锁机制
        test.execute(2, v -> test.lockAndUnlock());

        log.info("====================sync===============================");
        // 测试基于 synchronized 的锁机制
        test.execute(2, v -> test.lockAndUnlockBySync());

        log.info("====================sync2===============================");
        // 测试基于 synchronized 方法和代码块的混合锁机制
        test.execute(2, v -> test.lockAndUnlockBySync2());
    }

    /**
     * 执行多线程任务，用于测试锁机制。
     * <p>
     * 创建指定数量的线程，并在每个线程中执行传入的操作。
     * 线程启动后会依次加入主线程的等待队列，确保所有线程执行完毕后再继续。
     *
     * @param threadNums 线程数量。
     * @param consumer   每个线程需要执行的操作。
     */
    public void execute(int threadNums, Consumer<Void> consumer) {
        Thread[] threads = new Thread[2];
        IntStream.range(0, 2).forEach(i -> {
            threads[i] = new Thread(() -> {
                log.info("{} 开始抢占锁", Thread.currentThread().getName());
                consumer.accept(null); // 执行传入的操作
            }, "Thread-" + i);
            threads[i].start();
        });
        IntStream.range(0, 2).forEach(i -> {
            try {
                threads[i].join(); // 等待线程结束
            } catch (InterruptedException e) {
                log.error("线程等待异常", e);
            }
        });
    }

    /**
     * 使用 {@link java.util.concurrent.locks.ReentrantLock} 实现可重入锁的抢占与释放。
     * <p>
     * 该方法展示了 `ReentrantLock` 的可重入特性，即同一个线程可以多次获取锁，
     * 并且需要按照获取的顺序逐次释放锁。
     */
    public void lockAndUnlock() {
        lock.lock(); // 第一次获取锁
        log.info("{} 第1次抢占锁成功", Thread.currentThread().getName());
        lock.lock(); // 第二次获取锁
        log.info("{} 第2次抢占锁成功", Thread.currentThread().getName());
        try {
            // 模拟业务逻辑操作
        } finally {
            lock.unlock(); // 第一次释放锁
            log.info("{} 第1次锁释放成功", Thread.currentThread().getName());
            lock.unlock(); // 第二次释放锁
            log.info("{} 第2次锁释放成功", Thread.currentThread().getName());
        }
    }

    /**
     * 使用 `synchronized` 关键字实现可重入锁的抢占与释放。
     * <p>
     * 该方法展示了 `synchronized` 的可重入特性，即同一个线程可以多次进入同步代码块，
     * 并且锁会在同步代码块结束时自动释放。
     */
    public void lockAndUnlockBySync() {
        synchronized (this) { // 第一次进入同步代码块
            log.info("{} 第1次抢占锁成功", Thread.currentThread().getName());
            synchronized (this) { // 第二次进入同步代码块
                log.info("{} 第2次抢占锁成功", Thread.currentThread().getName());
            }
            log.info("{} 第1次释放锁成功", Thread.currentThread().getName());
        }
        log.info("{} 第2次释放锁成功", Thread.currentThread().getName());
    }

    /**
     * 使用 `synchronized` 方法和代码块混合实现可重入锁的抢占与释放。
     * <p>
     * 该方法展示了 `synchronized` 方法和代码块的混合使用场景，
     * 验证锁的可重入性和释放顺序。
     */
    public synchronized void lockAndUnlockBySync2() {
        log.info("{} 第1次抢占锁成功", Thread.currentThread().getName());
        synchronized (this) { // 进入同步代码块
            log.info("{} 第2次抢占锁成功", Thread.currentThread().getName());
        }
        log.info("{} 第1次释放锁成功", Thread.currentThread().getName());
    }

    private Lock lock = new ReentrantLock(); // 定义一个可重入锁实例
}
