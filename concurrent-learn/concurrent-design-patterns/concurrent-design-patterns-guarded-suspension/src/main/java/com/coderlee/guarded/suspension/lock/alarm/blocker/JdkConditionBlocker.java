package com.coderlee.guarded.suspension.lock.alarm.blocker;

import com.coderlee.guarded.suspension.lock.alarm.action.GuardedAction;
import com.coderlee.guarded.suspension.lock.alarm.predicate.Predicate;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 基于JDK Condition的阻塞器实现
 * <p>
 * 使用ReentrantLock和Condition实现阻塞和唤醒机制。
 *
 * @see com.coderlee.guarded.suspension.lock.alarm.blocker.Blocker
 * @see java.util.concurrent.locks.Condition
 * @see java.util.concurrent.locks.Lock
 */
@Slf4j
public class JdkConditionBlocker implements Blocker {

    /**
     * 锁对象
     */
    private final Lock lock;

    /**
     * 条件对象
     */
    private final Condition condition;

    /**
     * 是否能够获取锁的标志
     */
    private final boolean canGetLock;

    /**
     * 构造函数
     *
     * @param canGetLock 是否能够获取锁
     */
    public JdkConditionBlocker(boolean canGetLock) {
        this(new ReentrantLock(), canGetLock);
    }

    /**
     * 构造函数
     *
     * @param lock 锁对象
     * @param canGetLock 是否能够获取锁
     */
    public JdkConditionBlocker(Lock lock, boolean canGetLock) {
        this.lock = lock;
        this.condition = lock.newCondition();
        this.canGetLock = canGetLock;
    }

    /**
     * 构造函数
     *
     * @param lock 锁对象
     * @param condition 条件对象
     * @param canGetLock 是否能够获取锁
     */
    public JdkConditionBlocker(Lock lock, Condition condition, boolean canGetLock) {
        this.lock = lock;
        this.condition = condition;
        this.canGetLock = canGetLock;
    }

    @Override
    public <V> V callWithGuard(GuardedAction<V> guardedAction) throws Exception {
        lock.lockInterruptibly();
        try {
            // 判断条件是否满足，满足则执行目标动作，不满足则阻塞线程，进入条件队列进行等待
            final Predicate predicate = guardedAction.getPredicate();
            // 判断是否满足条件，不满足条件，则阻塞线程，进入条件队列进行等待。
            // 这里注意的是要使用while循环的方式进行判断，不要使用if判断
            while (!predicate.evaluate()) {
                log.info("连接报警系统的线程进入阻塞等待状态...");
                // 条件不满足，阻塞线程等待
                condition.await();
            }
            // 条件满足，执行目标方法
            log.info("连接报警系统成功，调用目标方法");
            return guardedAction.call();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void signalAfter(Callable<Boolean> stateOperation) throws Exception {
        lock.lockInterruptibly();
        try {
            if (stateOperation.call()) {
                log.info("条件满足，唤醒等待的线程...");
                condition.signal();
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void signal() throws Exception {
        lock.lockInterruptibly();
        try {
            condition.signal();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void broadcastAfter(Callable<Boolean> stateOperation) throws Exception {
        lock.lockInterruptibly();
        try {
            if (stateOperation.call()) {
                condition.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }
}
