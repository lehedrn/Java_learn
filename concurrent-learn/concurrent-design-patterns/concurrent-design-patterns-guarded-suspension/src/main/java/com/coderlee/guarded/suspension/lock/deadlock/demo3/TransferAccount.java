/**
 * 转账账户类，用于处理两个账户之间的资金转账操作。
 *
 * 该类通过按固定顺序获取锁的方式来避免死锁问题。每个账户都有唯一的ID，
 * 在转账时总是先锁定ID较小的账户，再锁定ID较大的账户，从而确保锁的获取顺序一致，
 * 避免循环等待导致的死锁。
 *
 * @see <a href="https://docs.oracle.com/javase/tutorial/essential/concurrency/deadlock.html">Deadlock in Java</a>
 */
package com.coderlee.guarded.suspension.lock.deadlock.demo3;

public class TransferAccount {

    /**
     * 账户唯一标识符
     */
    private Integer id;

    /**
     * 账户余额
     */
    private Integer balance;

    /**
     * 向目标账户转账指定金额
     *
     * 该方法通过按ID顺序获取锁的方式避免死锁。无论转账方向如何，
     * 总是先锁定ID较小的账户，再锁定ID较大的账户。
     *
     * @param target 目标账户
     * @param transferMoney 转账金额
     */
    public void transfer(TransferAccount target, Integer transferMoney) {
        // 初始化前后账户顺序
        TransferAccount beforeAccount = this;
        TransferAccount afterAccount = target;

        // 根据账户ID确定锁定顺序，确保ID小的账户先被锁定
        if (this.id > target.id) {
            beforeAccount = target;
            afterAccount = this;
        }

        // 按照确定的顺序依次锁定账户
        synchronized (beforeAccount) {
            synchronized (afterAccount) {
                // 检查余额是否足够进行转账
                if (this.balance >= transferMoney) {
                    // 从当前账户扣除转账金额
                    this.balance -= transferMoney;
                    // 向目标账户增加转账金额
                    target.balance += transferMoney;
                }
            }
        }
    }
}
