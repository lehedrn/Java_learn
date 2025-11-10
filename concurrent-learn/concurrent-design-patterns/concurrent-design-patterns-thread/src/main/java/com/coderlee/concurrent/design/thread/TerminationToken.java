package com.coderlee.concurrent.design.thread;

import lombok.Getter;
import lombok.Setter;

import java.lang.ref.WeakReference;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程终止令牌类
 * <p>
 * 用于协调多个线程的终止操作。该类维护了一个共享的终止状态标志，
 * 并跟踪需要执行的任务数量。同时管理一组协调终止的线程引用。
 * </p>
 *
 * @see Termination 线程终止接口
 */
public class TerminationToken {
    /**
     * 终止标志，当设置为true时表示需要关闭所有关联的线程
     */
    @Getter
    @Setter
    protected volatile boolean toShutdown = false;

    /**
     * 未执行任务计数器
     * <p>
     * 使用原子整数来跟踪尚未执行的任务数量
     * </p>
     */
    public final AtomicInteger noExecuteTaskCount = new AtomicInteger(0);

    /**
     * 协调线程队列
     * <p>
     * 存储与该令牌关联的所有需要协调终止的线程引用，使用弱引用避免内存泄漏
     * </p>
     */
    private final Queue<WeakReference<Termination>> coordinatedThreads;

    /**
     * 默认构造函数
     * <p>
     * 初始化协调线程队列
     * </p>
     */
    public TerminationToken() {
        this.coordinatedThreads = new ConcurrentLinkedQueue<>();
    }

    /**
     * 注册需要协调终止的线程
     * <p>
     * 将指定的线程添加到协调线程队列中
     * </p>
     *
     * @param termination 需要注册的线程终止对象
     */
    public void register(Termination termination) {
        this.coordinatedThreads.add(new WeakReference<>(termination));
    }

    /**
     * 通知线程终止
     * <p>
     * 当一个线程终止时，通知其他所有注册的线程也进行终止操作
     * </p>
     *
     * @param thread 已经终止的线程对象
     */
    public void notifyThreadTermination(Termination thread) {
        WeakReference<Termination> wrThread;
        Termination otherThread;

        // 轮询所有协调线程并通知它们终止
        while ((wrThread = coordinatedThreads.poll()) != null) {
            otherThread = wrThread.get();

            // 确保引用仍然有效且不是当前线程本身
            if (otherThread != null && otherThread != thread) {
                otherThread.terminate();
            }
        }
    }
}
