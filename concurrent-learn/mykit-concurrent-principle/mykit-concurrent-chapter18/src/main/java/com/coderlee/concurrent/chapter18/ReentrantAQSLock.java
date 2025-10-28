package com.coderlee.concurrent.chapter18;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * ReentrantAQSLock 是一个基于 AbstractQueuedSynchronizer (AQS) 实现的可重入锁。
 * 该类实现了 {@link Lock} 接口，提供了独占锁的功能，并支持锁的公平性和条件变量。
 * 内部通过继承 AQS 的方式实现锁的获取与释放逻辑。
 */
public class ReentrantAQSLock implements Lock {

    // AQS 同步器的实现类，用于管理锁的状态和线程排队机制
    private AQSSync sync = new AQSSync();

    /**
     * AQSSync 是 ReentrantAQSLock 的内部类，继承了 AbstractQueuedSynchronizer。
     * 它通过重写 AQS 的核心方法来实现锁的获取与释放逻辑。
     */
    private class AQSSync extends AbstractQueuedSynchronizer {

        /**
         * 尝试获取锁。
         * 如果锁未被占用（state == 0），则尝试通过 CAS 操作设置状态并获取锁；
         * 如果当前线程已持有锁，则增加锁的持有计数（state）。
         *
         * @param acquires 获取锁时的增量值（通常为 1）
         * @return 如果成功获取锁，则返回 true；否则返回 false
         */
        @Override
        protected boolean tryAcquire(int acquires) {
            int state = getState(); // 获取当前锁的状态
            Thread current = Thread.currentThread(); // 获取当前线程
            if (state == 0) { // 锁未被占用
                if (compareAndSetState(0, acquires)) { // 使用 CAS 设置状态
                    setExclusiveOwnerThread(current); // 设置当前线程为锁的持有者
                    return true;
                }
            } else if (getExclusiveOwnerThread() == current) { // 当前线程已持有锁
                int nextc = state + acquires; // 增加锁的持有计数
                if (nextc < 0) { // 检查是否超出最大计数值
                    throw new Error("Maximum lock count exceeded");
                }
                setState(nextc); // 更新锁的状态
                return true;
            }
            return false; // 获取锁失败
        }

        /**
         * 尝试释放锁。
         * 如果当前线程不是锁的持有者，则抛出异常；
         * 否则减少锁的持有计数，并在计数为 0 时完全释放锁。
         *
         * @param releases 释放锁时的减量值（通常为 1）
         * @return 如果锁被完全释放，则返回 true；否则返回 false
         */
        @Override
        protected boolean tryRelease(int releases) {
            if (Thread.currentThread() != getExclusiveOwnerThread()) { // 检查当前线程是否持有锁
                throw new IllegalMonitorStateException();
            }
            int status = getState() - releases; // 减少锁的持有计数
            boolean flag = false;
            if (0 == status) { // 如果计数为 0，表示锁完全释放
                flag = true;
                setExclusiveOwnerThread(null); // 清除锁的持有者
            }
            setState(status); // 更新锁的状态
            return flag; // 返回锁是否完全释放
        }

        /**
         * 创建一个新的条件变量。
         *
         * @return 新创建的条件变量对象
         */
        final ConditionObject newCondition() {
            return new ConditionObject(); // 返回 AQS 提供的条件变量实现
        }
    }

    /**
     * 获取锁。如果锁已被其他线程占用，则当前线程会被阻塞，直到成功获取锁。
     */
    @Override
    public void lock() {
        sync.acquire(1); // 调用 AQS 的 acquire 方法获取锁
    }

    /**
     * 获取锁，但允许被中断。
     * 如果当前线程在等待锁的过程中被中断，则抛出 InterruptedException。
     *
     * @throws InterruptedException 如果线程在等待过程中被中断
     */
    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1); // 调用 AQS 的 acquireInterruptibly 方法获取锁
    }

    /**
     * 尝试获取锁，但不会阻塞。
     * 如果锁可用，则立即获取并返回 true；否则返回 false。
     *
     * @return 如果成功获取锁，则返回 true；否则返回 false
     */
    @Override
    public boolean tryLock() {
        return sync.tryAcquire(1); // 调用 AQS 的 tryAcquire 方法尝试获取锁
    }

    /**
     * 尝试在指定时间内获取锁。
     * 如果在超时前成功获取锁，则返回 true；否则返回 false。
     *
     * @param time 等待时间
     * @param unit 时间单位
     * @return 如果成功获取锁，则返回 true；否则返回 false
     * @throws InterruptedException 如果线程在等待过程中被中断
     */
    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1, unit.toNanos(time)); // 调用 AQS 的 tryAcquireNanos 方法尝试获取锁
    }

    /**
     * 释放锁。
     * 如果锁的持有计数为 0，则完全释放锁；否则仅减少计数。
     */
    @Override
    public void unlock() {
        sync.release(1); // 调用 AQS 的 release 方法释放锁
    }

    /**
     * 返回与此锁关联的新条件变量。
     *
     * @return 新创建的条件变量
     */
    @Override
    public Condition newCondition() {
        return sync.newCondition(); // 调用 AQS 的 newCondition 方法创建条件变量
    }
}
