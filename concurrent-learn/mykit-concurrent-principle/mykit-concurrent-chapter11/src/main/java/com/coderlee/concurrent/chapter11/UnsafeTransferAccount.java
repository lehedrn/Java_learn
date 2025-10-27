package com.coderlee.concurrent.chapter11;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 表示一个银行账户，支持转账操作。
 * </p>
 * <p>
 * 此类实现了一个不安全的转账逻辑，可能会在多线程环境下导致数据一致性问题。 由于同步块仅锁定当前对象
 * (`this`)，而未对目标账户进行锁定，因此在并发场景下可能发生竞态条件。
 * </p>
 */
@Slf4j
public class UnsafeTransferAccount {
    public static void main(String[] args) throws InterruptedException {
        // 初始化两个账户
        UnsafeTransferAccount accountA = new UnsafeTransferAccount();
        UnsafeTransferAccount accountB = new UnsafeTransferAccount();

        accountA.balance = 100000;
        accountB.balance = 100000;

        // 记录初始总余额
        long initialTotalBalance = accountA.balance + accountB.balance;

        // 定义线程数量和转账金额
        int threadCount = 2000;
        long transferAmount = 50;

        // 创建并启动多个线程进行转账操作
        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                try {
                    Thread.sleep((long) (Math.random() * 10)); // 随机延迟 0~10 毫秒
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                accountA.transferMoney(accountB, transferAmount);
            });
            threads[i].start();
        }

        // 等待所有线程执行完毕
        for (Thread thread : threads) {
            thread.join();
        }

        // 打印最终余额
        log.info("Final balance of accountA: " + accountA.balance);
        log.info("Final balance of accountB: " + accountB.balance);

        // 验证总余额是否一致
        long finalTotalBalance = accountA.balance + accountB.balance;
        if (finalTotalBalance == initialTotalBalance) {
            log.info("Total balance is consistent.");
        } else {
            log.info("Total balance is inconsistent!");
            log.info("Initial total balance: " + initialTotalBalance);
            log.info("Final total balance: " + finalTotalBalance);
        }
    }

    private long balance; // 账户余额

    /**
     * <p>
     * 将指定金额从当前账户转账到目标账户。
     * </p>
     * <p>
     * 此方法使用了 `synchronized` 关键字来确保当前账户的操作是线程安全的， 但由于未对目标账户加锁，可能导致在多线程环境下的数据不一致问题。
     * </p>
     *
     * @param targetAccount 目标账户，即接收转账金额的账户
     * @param transferMoney 转账金额，必须为非负数
     */
    public void transferMoney(UnsafeTransferAccount targetAccount, long transferMoney) {
        synchronized (this) { // 锁定当前账户对象，确保当前账户的操作是线程安全的
            if (this.balance >= transferMoney) { // 检查当前账户余额是否足够
                this.balance -= transferMoney; // 从当前账户扣除转账金额
                targetAccount.balance += transferMoney; // 向目标账户增加转账金额
            }
        }
    }

}