package com.coderlee.guarded.suspension.lock.deadlock.demo1;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 转账账户类，演示死锁问题的示例代码
 *
 * 该类通过{@link java.util.concurrent.locks.ReentrantLock}实现转账功能，
 * 但在当前实现中存在死锁风险。当两个线程同时尝试进行相互转账时，
 * 可能出现互相等待对方释放锁的情况。
 *
 * @see java.util.concurrent.locks.ReentrantLock
 */
public class TansferAccount {

    /** 当前账户的锁 */
    private Lock thisLock = new ReentrantLock();

    /** 目标账户的锁 */
    private Lock targetLock = new ReentrantLock();

    /** 账户余额 */
    private Integer balance;

    /**
     * 转账方法，从当前账户向目标账户转账指定金额
     *
     * 此方法存在死锁风险：当线程A试图从账户X转账到账户Y的同时，
     * 线程B试图从账户Y转账到账户X时，可能出现死锁情况。
     *
     * @param target 目标账户
     * @param transferMoney 转账金额
     */
    public void transfer(TansferAccount target, Integer transferMoney) {
        // 尝试获取当前账户的锁
        boolean isThisLock = thisLock.tryLock();
        if (isThisLock) {
            try {
                // 尝试获取目标账户的锁
                boolean isTargetLock = targetLock.tryLock();
                if (isTargetLock) {
                    try {
                        // 检查余额是否足够转账
                        if (this.balance >= transferMoney) {
                            // 执行转账操作：减少当前账户余额
                            this.balance -= transferMoney;
                            // 增加目标账户余额
                            target.balance += transferMoney;
                        }
                    } finally {
                        // 释放目标账户的锁
                        targetLock.unlock();
                    }
                }
            } finally {
                // 释放当前账户的锁
                thisLock.unlock();
            }
        }
    }
}
