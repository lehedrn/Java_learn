package com.coderlee.concurrent.chapter16;

import java.util.concurrent.atomic.AtomicReference;

/**
 * MyCasLock 是一个基于 CAS（Compare-And-Swap）机制的锁实现。
 * <p>
 * 该类通过 {@link AtomicReference} 实现了无锁的线程同步机制，
 * 确保在高并发场景下能够安全地进行加锁和解锁操作。
 * <p>
 * 锁的状态由一个 {@link AtomicReference} 维护，初始状态为 {@code null}，
 * 表示锁未被任何线程持有。当某个线程成功获取锁后，{@link AtomicReference}
 * 会更新为当前线程的引用，其他线程将无法获取锁，直到锁被释放。
 * <p>
 * 该实现适用于简单的互斥场景，但不支持重入锁或公平锁。
 */
public class MyCasLock implements CasLock {

    /**
     * 使用 {@link AtomicReference} 维护锁的所有者线程。
     * 初始值为 {@code null}，表示锁未被任何线程持有。
     */
    private AtomicReference<Thread> threadOwner = new AtomicReference<>();

    @Override
    public void lock() {
        Thread current = Thread.currentThread();
        // 自旋尝试获取锁，直到成功为止
        for (;;) {
            // 如果锁未被占用（即 threadOwner 为 null），则尝试将其设置为当前线程
            if (threadOwner.compareAndSet(null, current)) {
                break; // 成功获取锁，退出循环
            }
        }
    }

    @Override
    public void unlock() {
        Thread current = Thread.currentThread();
        // 自旋尝试释放锁，直到成功为止
        for (;;) {
            // 如果锁当前由当前线程持有，则尝试将其设置为 null（释放锁）
            if (threadOwner.compareAndSet(current, null)) {
                break; // 成功释放锁，退出循环
            }
        }
    }
}
