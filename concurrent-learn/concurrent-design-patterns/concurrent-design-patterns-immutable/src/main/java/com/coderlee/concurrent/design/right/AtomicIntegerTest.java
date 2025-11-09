package com.coderlee.concurrent.design.right;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>类名：AtomicIntegerTest</p>
 * <p>描述：该类展示了如何使用 {@link AtomicInteger} 来实现线程安全的计数器。</p>
 * <p>{@link AtomicInteger} 是 Java 提供的一个原子类，可以在不使用显式锁的情况下保证操作的原子性，
 * 适用于多线程环境下的计数场景。</p>
 */
public class AtomicIntegerTest {

    /**
     * 定义一个原子整型变量，用于存储计数值。
     * 使用 {@link AtomicInteger} 可以避免多线程并发操作时的数据竞争问题。
     */
    private AtomicInteger atomicCount = new AtomicInteger(0);

    /**
     * 增加计数值的方法。
     * <p>调用 {@link AtomicInteger#incrementAndGet()} 方法对计数值进行原子递增操作。
     * 该方法是线程安全的，无需额外同步。</p>
     */
    public void add() {
        // 调用 incrementAndGet 方法对 atomicCount 进行原子递增
        atomicCount.incrementAndGet();
    }
}