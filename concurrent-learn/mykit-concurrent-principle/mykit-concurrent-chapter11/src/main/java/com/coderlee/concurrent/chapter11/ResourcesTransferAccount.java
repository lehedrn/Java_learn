package com.coderlee.concurrent.chapter11;

import java.util.stream.IntStream;

import lombok.extern.slf4j.Slf4j;

/**
 * ResourcesTransferAccount 类用于模拟银行账户间的转账操作。
 * <p>
 * 该类通过引入资源申请器 [ResourcesRequester] 来确保并发环境下的资源安全，
 * 避免死锁问题的发生。其核心思想是破坏“请求与保持条件”，即在获取所有所需资源之前不会持有部分资源。
 * </p>
 */
@Slf4j
public class ResourcesTransferAccount {
    // 账户余额，表示当前账户的资金数量
    private long balance;

    // 静态资源申请器，用于管理多个账户之间的资源申请和释放
    private static ResourcesRequester requester;

    static {
        // 初始化资源申请器
        requester = new ResourcesRequester();
    }

    /**
     * 将指定金额从当前账户转移到目标账户。
     * <p>
     * 该方法通过循环申请资源的方式确保同时获取两个账户的锁，避免死锁问题。
     * 在资源申请成功后，执行转账逻辑，并在操作完成后释放资源。
     * </p>
     *
     * @param targetAccount 目标账户，资金将转入该账户
     * @param transferMoney 转账金额，表示需要转移的资金数量
     */
    public void transferMoney(ResourcesTransferAccount targetAccount, long transferMoney) {
        // 循环申请资源，直到成功获取当前账户和目标账户的资源
        while (true) {
            if (requester.applyResources(this, targetAccount)) {
                break; // 成功申请到资源，退出循环
            }
        }
        try {
            // 同步当前账户和目标账户，确保转账操作的原子性
            synchronized (this) {
                synchronized (targetAccount) {
                    if (this.balance >= transferMoney) { // 检查当前账户是否有足够的余额
                        this.balance -= transferMoney; // 扣除当前账户的余额
                        targetAccount.balance += transferMoney; // 增加目标账户的余额
                    }
                }
            }
        } finally {
            // 确保资源在操作完成后被正确释放
            requester.releaseResources(this, targetAccount);
        }
    }

    /**
     * 主方法，用于测试转账逻辑的正确性和并发安全性。
     * <p>
     * 创建两个账户并启动两个线程分别进行双向转账操作。
     * 测试结束后验证总余额是否保持一致，以判断是否存在数据竞争或死锁问题。
     * </p>
     */
    public static void main(String[] args) {
        // 初始化两个账户
        ResourcesTransferAccount accountA = new ResourcesTransferAccount();
        ResourcesTransferAccount accountB = new ResourcesTransferAccount();
        accountA.balance = 1000; // 设置账户 A 的初始余额
        accountB.balance = 1000; // 设置账户 B 的初始余额

        long transferAmount = 200; // 每次转账金额

        // 创建线程 A，模拟从账户 A 向账户 B 转账
        Thread thread1 = new Thread(() -> IntStream.range(0, 5).forEach(i -> accountA.transferMoney(accountB, transferAmount)), "Thread-A-to-B");
        // 创建线程 B，模拟从账户 B 向账户 A 转账
        Thread thread2 = new Thread(() -> IntStream.range(0, 5).forEach(i -> accountB.transferMoney(accountA, transferAmount)), "Thread-B-to-A");

        // 启动线程
        thread1.start();
        thread2.start();

        // 等待两个线程执行完成
        try {
            thread1.join(); // 等待线程 A 完成
        } catch (InterruptedException e) {
            log.error("Thread-A interrupted", e);
        }
        try {
            thread2.join(); // 等待线程 B 完成
        } catch (InterruptedException e) {
            log.error("Thread-B interrupted", e);
        }

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