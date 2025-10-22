package com.coderlee.juc;

/**
 * CASDemo 类用于演示基于 Compare-And-Swap (CAS) 算法的简单实现。
 * 
 * 该类的核心意图是展示如何通过 CAS 操作来实现线程安全的值更新操作。
 * CAS 是一种无锁算法，用于在多线程环境下解决并发问题。它通过比较内存中的当前值与预期值是否相等，
 * 来决定是否将新值写入内存。如果相等，则更新成功；否则，更新失败。
 * 
 * 主要包含以下内容：
 * 1. CompareAndSwap 类：封装了 CAS 的核心逻辑，包括获取当前值 get、执行 CAS 操作 compareAndSwap 和判断设置结果 compareAndSet。
 *    - get() 方法返回当前值，使用 `synchronized` 保证线程安全。
 *    - compareAndSwap() 方法尝试用新值替换旧值，并返回操作前的旧值。
 *    - compareAndSet() 方法用于判断 CAS 操作是否成功。
 * 
 * 2. main 方法：模拟多个线程并发调用 CAS 操作的场景。
 *    - 每个线程会先获取当前值，然后尝试通过 CAS 更新为一个随机值。
 *    - 输出每次 CAS 操作的结果（成功或失败）。
 * 
 * 本代码旨在帮助理解 CAS 的基本原理及其在线程安全中的应用，适用于学习和教学场景。
 */
public class CASDemo {

    /**
     * 主函数，程序入口。
     * 创建10个线程，每个线程尝试通过CAS操作更新CompareAndSwap实例的值。
     * 
     * @param args 命令行参数，未使用。
     */
    public static void main(String[] args) {
        CompareAndSwap cas = new CompareAndSwap();

        // 创建10个线程，每个线程执行CAS操作并打印结果
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int expectedValue = cas.get(); // 获取当前值
                    // 尝试将当前值更新为一个随机数，并打印操作是否成功
                    boolean b = cas.compareAndSet(expectedValue, (int) (Math.random() * 101));
                    System.out.println(b);
                }
            }).start();
        }
    }
}

/**
 * CompareAndSwap类实现了简单的CAS（Compare-And-Swap）机制。
 * 提供了获取当前值、执行CAS操作以及判断CAS操作是否成功的功能。
 */
class CompareAndSwap { 
    private int value;

    /**
     * 获取当前值。
     * 
     * @return 当前值。
     */
    public synchronized int get() {
        return value;
    }

    /**
     * 执行CAS操作：如果当前值等于期望值，则将其更新为新值。
     * 
     * @param expectedValue 期望的当前值。
     * @param newValue      新值。
     * @return 更新前的值。
     */
    public synchronized int compareAndSwap(int expectedValue, int newValue) {
        int oldValue = value;
        if (oldValue == expectedValue) {
            this.value = newValue; // 如果期望值匹配，则更新为新值
        }
        return oldValue;
    }

    /**
     * 判断CAS操作是否成功：如果当前值等于期望值，则更新为新值并返回true，否则返回false。
     * 
     * @param expectedValue 期望的当前值。
     * @param newValue      新值。
     * @return 如果CAS操作成功则返回true，否则返回false。
     */
    public synchronized boolean compareAndSet(int expectedValue, int newValue) {
        return expectedValue == compareAndSwap(expectedValue, newValue);
    }
}