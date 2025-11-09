package com.coderlee.concurrent.design.right;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>该类 {@code SynchronizedLockCounter} 演示了两种线程安全的计数器实现方式：
 * 一种是通过显式锁（{@link Lock}）实现，另一种是通过同步方法（{@code synchronized}关键字）实现。
 * </p>
 * <p>此类主要用于对比显式锁和同步方法在并发环境下的使用场景及效果。
 * 显式锁提供了更灵活的锁定机制，而同步方法则更加简洁。</p>
 */
public class SynchronizedLockCounter {

    private int count; // 计数器变量，用于记录当前计数值

    private Lock lock = new ReentrantLock(); // 使用可重入锁实现显式锁机制

    /**
     * 使用显式锁（{@link Lock}）来保证线程安全的计数操作。
     * <p>在此方法中，首先获取锁，然后调用 {@link #add()} 方法进行计数，
     * 最后在 {@code finally} 块中确保锁的释放，避免死锁。</p>
     */
    public void lockMethod() {
        // 获取锁
        lock.lock(); 
        try {
            // 调用非线程安全的方法进行计数
            this.add(); 
        } finally {
            // 确保锁的释放
            lock.unlock(); 
        }
    }

    /**
     * 使用同步方法（{@code synchronized}关键字）来保证线程安全的计数操作。
     * <p>此方法通过同步方法的方式，自动管理锁的获取与释放，简化了代码逻辑。</p>
     */
    public synchronized void synchronizedMethod() {
        // 调用非线程安全的方法进行计数
        this.add(); 
    }

    /**
     * 非线程安全的计数方法，仅用于内部调用。
     * <p>此方法直接对 {@code count} 进行自增操作，未加任何线程安全控制。</p>
     */
    public void add() {
        // 对计数器变量进行自增操作
        count++;
    }
}