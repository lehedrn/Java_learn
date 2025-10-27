package com.coderlee.concurrent.chapter11;

import lombok.extern.slf4j.Slf4j;

/**
 * 该类模拟银行账户之间的转账操作，并展示了死锁的产生过程。
 * <p>
 * 每个账户对象通过 {@code balance} 字段维护余额，使用同步块确保线程安全。
 * 转账操作通过 {@link #transferAmount(DeadLockTransferAccount, long)} 方法实现，
 * 但由于对多个账户对象加锁的顺序不一致，可能导致死锁问题。
 * 
 * @author coderlee
 */
@Slf4j
public class DeadLockTransferAccount {

    /** 账户余额 */
    private long balance;

    /**
     * 在两个账户之间进行金额转账操作。
     * <p>
     * 该方法首先锁定当前账户实例（{@code this}），然后尝试锁定目标账户实例（{@code targetAccount}）。
     * 如果当前账户余额充足，则从当前账户扣除指定金额并添加到目标账户中。
     * 
     * @param targetAccount 目标账户，表示转账的目标
     * @param transferMoney 转账金额，必须为正数
     */
    public void transferAmount(DeadLockTransferAccount targetAccount, long transferMoney) {
        synchronized (this) { // 锁定当前账户实例
            try {
                // 引入随机时间抖动，增加线程交错概率，从而更容易触发死锁
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // 恢复中断状态
            }
            synchronized (targetAccount) { // 锁定目标账户实例
                if (this.balance >= transferMoney) { // 检查余额是否足够
                    this.balance -= transferMoney; // 扣除当前账户余额
                    targetAccount.balance += transferMoney; // 增加目标账户余额
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // 创建两个账户实例
        DeadLockTransferAccount accountA = new DeadLockTransferAccount();
        DeadLockTransferAccount accountB = new DeadLockTransferAccount();

        // 初始化账户余额
        accountA.balance = 1000; // 设置 accountA 的初始余额
        accountB.balance = 1000; // 设置 accountB 的初始余额

        // 线程1：从 accountA 转账到 accountB
        Thread thread1 = new Thread(() -> {
            log.info("尝试从 accountA 转账到 accountB");
            accountA.transferAmount(accountB, 500); // 转账 500 单位金额
            log.info("从 accountA 转账到 accountB 完成");
        });

        // 线程2：从 accountB 转账到 accountA
        Thread thread2 = new Thread(() -> {
            log.info("尝试从 accountB 转账到 accountA");
            accountB.transferAmount(accountA, 300); // 转账 300 单位金额
            log.info("从 accountB 转账到 accountA 完成");
        });

        // 启动两个线程
        thread1.start(); // 启动线程1
        thread2.start(); // 启动线程2

        // 等待两个线程执行完成
        thread1.join(); // 等待线程1结束
        thread2.join(); // 等待线程2结束

        // 打印最终账户余额
        log.info("accountA 最终余额: {}", accountA.balance); // 打印 accountA 的余额
        log.info("accountB 最终余额: {}", accountB.balance); // 打印 accountB 的余额
    }
}