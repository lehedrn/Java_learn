/**
 * 转账账户类，用于处理两个账户之间的资金转账操作。
 *
 * 该类实现了避免死锁的转账机制，通过使用 {@link ResourcesRequester} 来申请和释放资源，
 * 确保在多线程环境下进行转账操作时不会出现死锁问题。
 *
 * 该实现采用了忙等待（busy waiting）的方式来获取资源锁，直到成功获取两个账户的资源锁后才执行转账操作。
 *
 * @see ResourcesRequester
 */
package com.coderlee.guarded.suspension.lock.deadlock.demo2;

public class TransferAccount {

    /**
     * 账户余额
     */
    private Integer balance;

    /**
     * 资源请求器，用于申请和释放账户资源
     */
    private ResourcesRequester requester;

    /**
     * 向目标账户转账指定金额
     *
     * 该方法通过循环申请资源锁来避免死锁，确保在转账过程中两个账户都被正确锁定。
     * 转账操作在同步块中执行，保证线程安全。
     *
     * @param target 目标账户
     * @param transferMoney 转账金额
     */
    public void transfer(TransferAccount target, Integer transferMoney) {
        // 循环申请资源，直到成功获取两个账户的锁
        while (!requester.applyResources(this, target)) {
            // 忙等待，直到获取到资源锁
            ;
        }

        try {
            // 锁定当前账户对象
            synchronized (this) {
                // 锁定目标账户对象
                synchronized (target) {
                    // 检查余额是否足够进行转账
                    if (this.balance >= transferMoney) {
                        // 从当前账户扣除转账金额
                        this.balance -= transferMoney;
                        // 向目标账户增加转账金额
                        target.balance += transferMoney;
                    }
                }
            }
        } finally {
            // 释放两个账户的资源锁
            requester.releaseResources(this, target);
        }
    }
}
