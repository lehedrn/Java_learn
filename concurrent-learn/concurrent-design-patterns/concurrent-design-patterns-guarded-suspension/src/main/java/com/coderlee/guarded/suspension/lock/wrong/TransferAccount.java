package com.coderlee.guarded.suspension.lock.wrong;

/**
 * 转账账户类 - 错误实现示例
 * 存在直接业务关系的场景，eg.账户A给账户B转账
 */
public class TransferAccount {

    /**
     * 账户余额
     */
    private Integer balance;

    /**
     * 执行转账操作 - 错误实现示例
     *
     * 该方法演示了不安全的转账操作，在多线程环境下可能会出现以下问题：
     * 1. 缺少同步机制，可能导致竞态条件
     * 2. 没有实现Guarded Suspension模式的核心思想
     * 3. 转账操作不是原子性的
     *
     * @param target 目标账户
     * @param transferMoney 转账金额
     */
    public void transfer(TransferAccount target, Integer transferMoney) {
        // 检查当前账户余额是否足够转账
        if (balance >= transferMoney) {
            // 从当前账户扣除转账金额
            this.balance -= transferMoney;
            // 向目标账户增加转账金额
            target.balance += transferMoney;
        }
    }

    /**
     * 错误的加锁方式
     * 假设存在A、B、C三个账户，A给B转账100，B给C转账100
     * 线程A和线程B同时执行transferSync方法，两个线程都完成转账操作后，B的账户余额可能为300，也可能为100，但是不可能为200。
     * 问题就出现在synchronized(this)这把锁上，这把锁只能保护this.balance资源，而无法保护target.balance资源
     */
    public synchronized void transferSync(TransferAccount target, Integer transferMoney) {
        // 检查当前账户余额是否足够转账
        if (balance >= transferMoney) {
            // 从当前账户扣除转账金额
            this.balance -= transferMoney;
            // 向目标账户增加转账金额
            target.balance += transferMoney;
        }
    }
}
