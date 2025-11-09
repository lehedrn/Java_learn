package com.coderlee.concurrent.design.right;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>类名：RightCounter</p>
 * <p>描述：该类实现了一个线程安全的计数器，主要用于统计访问次数。</p>
 * <p>通过使用 {@link AtomicInteger}，保证了在多线程环境下的操作安全性。</p>
 */
public class RightCounter {

    /**
     * 使用 {@link AtomicInteger} 实现线程安全的计数器。
     * 该变量用于存储当前的访问计数值。
     */
    private AtomicInteger atomicIntegerCounter = new AtomicInteger(0);

    /**
     * 方法名：accessVisit
     * 描述：每次调用该方法时，将计数器的值加1。
     * 该方法是线程安全的，适用于多线程场景下的计数操作。
     */
    public void accessVisit() {
        // 调用 incrementAndGet 方法，原子性地增加计数器的值。
        atomicIntegerCounter.incrementAndGet();
    }

    /**
     * 方法名：getVisitCount
     * 描述：返回当前计数器的值。
     * 该方法是线程安全的，能够正确返回最新的计数值。
     *
     * @return 当前计数器的值
     */
    public int getVisitCount() {
        // 调用 get 方法，获取计数器的当前值。
        return atomicIntegerCounter.get();
    }
}