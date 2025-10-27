package com.coderlee.concurrent.chapter11;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

import lombok.extern.slf4j.Slf4j;

/**
 * LockTransferAccount 类用于演示如何使用显式锁（`Lock`）来实现线程安全的账户转账操作。
 * <p>
 * 该类通过使用两个显式锁（[thisLock] 和 [targetAccountLock]）来避免死锁问题，主要破坏了死锁的“不可剥夺条件”。
 * 如果当前账户或目标账户的锁无法获取，则不会进入等待状态，而是直接释放已持有的锁并退出。
 * </p>
 */
@Slf4j
public class LockTransferAccount {
    private long balance; // 当前账户余额
    private Lock thisLock = new ReentrantLock(); // 当前账户的锁
    private Lock targetAccountLock = new ReentrantLock(); // 目标账户的锁

    /**
     * 实现从当前账户向目标账户转账的功能。
     * <p>
     * 转账操作分为以下几个步骤：
     * <ol>
     *   <li>尝试获取当前账户的锁。</li>
     *   <li>如果成功获取当前账户的锁，则尝试获取目标账户的锁。</li>
     *   <li>如果两个锁都成功获取，则检查当前账户余额是否足够。</li>
     *   <li>如果余额充足，则执行转账操作，更新当前账户和目标账户的余额。</li>
     *   <li>无论是否成功完成转账，都会确保释放所有已获取的锁。</li>
     * </ol>
     * </p>
     *
     * @param targetAccount 目标账户，转账的目标
     * @param transferMoney 转账金额，单位为货币的最小单位（如分）
     */
    public void transferMone(LockTransferAccount targetAccount, long transferMoney) {
        try {
            // 尝试获取当前账户的锁
            if (thisLock.tryLock()) {
                try {
                    // 尝试获取目标账户的锁
                    if (targetAccountLock.tryLock()) {
                        // 检查当前账户余额是否足够进行转账
                        if (this.balance >= transferMoney) {
                            // 执行转账：减少当前账户余额，增加目标账户余额
                            this.balance -= transferMoney;
                            targetAccount.balance += transferMoney;
                        }
                    }
                } finally {
                    // 确保释放目标账户的锁
                    targetAccountLock.unlock();
                }
            }
        } finally {
            // 确保释放当前账户的锁
            thisLock.unlock();
        }
    }

    /**
     * 可用于测试转账逻辑。
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) throws InterruptedException {
        // 创建两个账户：accountA 和 accountB
        LockTransferAccount accountA = new LockTransferAccount();
        LockTransferAccount accountB = new LockTransferAccount();

        // 初始化账户余额
        accountA.balance = 1000; // 账户A初始余额为1000
        accountB.balance = 1000; // 账户B初始余额为1000

        // 定义转账金额
        long transferAmount = 200;

        // 创建两个线程，模拟并发转账
        Thread thread1 = new Thread(() -> IntStream.range(0, 5).forEach(i -> accountA.transferMone(accountB, transferAmount)),  "Thread-A-to-B");

        Thread thread2 = new Thread(() -> IntStream.range(0, 5).forEach(i -> accountB.transferMone(accountA, transferAmount)), "Thread-B-to-A");

        // 启动线程
        thread1.start();
        thread2.start();

        // 等待两个线程执行完成
        thread1.join();
        thread2.join();

        // 打印最终账户余额
        log.info("Final balance of Account A: {}", accountA.balance);
        log.info("Final balance of Account B: {}", accountB.balance);

        // 验证总余额是否保持一致
        long totalBalance = accountA.balance + accountB.balance;
        if (totalBalance == 2000) {
            log.info("Total balance is consistent. Test passed!");
        } else {
            log.info("Total balance is inconsistent. Test failed!");
        }
    }
}
