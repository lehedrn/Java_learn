package com.coderlee.guarded.suspension.lock.demo;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * GuardedQueue 是一个基于 guarded suspension 模式的线程安全队列实现。
 *
 * 该类使用了 Java 内置的 synchronized 和 wait/notify 机制来实现线程间的协调，
 * 当队列为空时，获取元素的操作会被阻塞直到有新元素加入。
 *
 * @see <a href="https://docs.oracle.com/javase/tutorial/essential/concurrency/guardmeth.html">Guarded Suspension Pattern</a>
 * @see LinkedBlockingQueue
 */
public class GuardedQueue {
    /** 存储数据的队列，使用 LinkedBlockingQueue 实现 */
    private final Queue<Integer> sourceList;

    /**
     * 构造一个新的 GuardedQueue 实例
     * 初始化内部使用的 LinkedBlockingQueue 队列
     */
    public GuardedQueue() {
        this.sourceList = new LinkedBlockingQueue<>();
    }

    /**
     * 从队列中获取一个元素
     * 如果队列为空，则当前线程会等待直到队列中有元素可用
     *
     * @return 队列头部的元素
     * @throws RuntimeException 如果线程在等待过程中被中断
     * @see Object#wait()
     */
    public synchronized Integer get() {
        // 当队列为空时，进入等待状态
        while (sourceList.isEmpty()) {
            try {
                // 等待其他线程调用 notify 或 notifyAll 来唤醒此线程
                this.wait();
            } catch (InterruptedException e) {
                // 如果等待过程中线程被中断，则抛出运行时异常
                throw new RuntimeException(e);
            }
        }
        // 返回队列头部的元素但不移除它
        return sourceList.peek();
    }

    /**
     * 向队列中添加元素
     * @param e 要添加的整数元素
     */
    public synchronized void put(Integer e) {
        // 添加元素到队列尾部
        sourceList.offer(e);
        // 唤醒所有等待的线程
        this.notifyAll();
    }

}
