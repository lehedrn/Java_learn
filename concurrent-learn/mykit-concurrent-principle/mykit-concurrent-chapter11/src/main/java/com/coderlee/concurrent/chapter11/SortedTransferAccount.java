package com.coderlee.concurrent.chapter11;

import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;

/**
 * 破坏循环等待条件的银行账户转账实现类。
 * <p>
 * 该类通过有序加锁的方式解决了多线程环境下银行账户转账时可能发生的死锁问题。
 * 具体实现中，通过对账户编号进行排序，确保每次转账操作都按照固定的顺序获取锁，
 * 从而避免了循环等待条件，满足了防止死锁的必要条件之一。
 * </p>
 */
@Slf4j
public class SortedTransferAccount {

    private long no; // 账户编号，用于标识账户并参与锁排序
    private long balance; // 账户余额

    /**
     * 构造函数，初始化账户编号和余额。
     *
     * @param no 账户编号
     * @param balance 初始账户余额
     */
    public SortedTransferAccount(long no, long balance) {
        this.no = no;
        this.balance = balance;
    }

    /**
     * 实现安全的转账操作。
     * <p>
     * 通过有序加锁的方式，确保在并发场景下不会发生死锁。具体逻辑如下：
     * <ol>
     *   <li>根据账户编号对两个账户进行排序，确定先锁定的账户（低编号）和后锁定的账户（高编号）。</li>
     *   <li>按顺序对两个账户依次加锁，执行转账操作。</li>
     *   <li>如果当前账户余额足够，则从当前账户扣除转账金额，并增加到目标账户。</li>
     * </ol>
     * </p>
     *
     * @param targetAccount 目标账户，转账金额将转入该账户
     * @param transferMoney 转账金额
     */
    public void transferMoney(SortedTransferAccount targetAccount, long transferMoney) {
        // 根据账户编号排序，确定加锁顺序
        SortedTransferAccount beforeLockAccount = this;
        SortedTransferAccount afterLockAccount = targetAccount;
        if (this.no > targetAccount.no) { // 如果当前账户编号较大，则调整加锁顺序
            beforeLockAccount = targetAccount;
            afterLockAccount = this;
        }
        // 按顺序加锁，确保不会发生循环等待
        synchronized (beforeLockAccount) {
            synchronized (afterLockAccount) {
                if (this.balance >= transferMoney) { // 检查当前账户余额是否足够
                    this.balance -= transferMoney; // 扣除转账金额
                    targetAccount.balance += transferMoney; // 增加到目标账户
                }
            }
        }
    }

    public static void main(String[] args) {
        // 创建两个账户，分别初始化余额为1000
        SortedTransferAccount accountA = new SortedTransferAccount(1, 1000);
        SortedTransferAccount accountB = new SortedTransferAccount(2, 1000);

        // 创建线程1，模拟从账户A向账户B转账5次，每次200
        Thread thread1 = new Thread(() -> IntStream.range(0, 5).forEach(i -> accountA.transferMoney(accountB, 200)), "Thread-A-to-B");
        // 创建线程2，模拟从账户B向账户A转账5次，每次200
        Thread thread2 = new Thread(() -> IntStream.range(0, 5).forEach(i -> accountB.transferMoney(accountA, 200)), "Thread-B-to-A");

        // 启动两个线程
        thread1.start();
        thread2.start();

        try {
            // 等待线程1执行完成
            thread1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            // 等待线程2执行完成
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 打印最终账户余额
        log.info("Final balance of Account A: {}", accountA.balance);
        log.info("Final balance of Account B: {}", accountB.balance);

        // 验证总余额是否一致
        long totalBalance = accountA.balance + accountB.balance;
        if (totalBalance == 2000) {
            log.info("Total balance is consistent. Test passed!");
        } else {
            log.info("Total balance is inconsistent. Test failed!");
        }
    }
}