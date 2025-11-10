package com.coderlee.guarded.suspension.lock.alarm.blocker;

import com.coderlee.guarded.suspension.lock.alarm.action.GuardedAction;

import java.util.concurrent.Callable;

/**
 * 阻塞器接口
 * <p>
 * 定义了Guarded Suspension模式的核心操作接口，
 * 包括带保护条件的方法调用、信号通知等功能。
 *
 * @see com.coderlee.guarded.suspension.lock.alarm.action.GuardedAction
 */
public interface Blocker {

    /**
     * 在保护条件成立时执行目标动作，否则阻塞当前线程，直到保护条件成立
     * @param guardedAction 受保护的动作
     * @param <V> 返回值类型
     * @return 动作执行的结果
     * @throws Exception 执行过程中可能抛出的异常
     */
    <V> V callWithGuard(GuardedAction<V> guardedAction) throws Exception;

    /**
     * 先执行stateOperation，如果返回true则确定唤醒该Blocker上阻塞的一个线程
     * @param stateOperation 状态操作
     * @throws Exception 执行过程中可能抛出的异常
     */
    void signalAfter(Callable<Boolean> stateOperation) throws Exception;

    /**
     * 直接唤醒blocker上阻塞的一个线程
     * @throws Exception 执行过程中可能抛出的异常
     */
    void signal() throws Exception;

    /**
     * 根据stateOperation唤醒blocker上的所有线程
     * @param stateOperation 状态操作
     * @throws Exception 执行过程中可能抛出的异常
     */
    void broadcastAfter(Callable<Boolean> stateOperation) throws Exception;
}
