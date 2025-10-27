package com.coderlee.concurrent.chapter11;

/**
 * 线程安全的账户转账类。
 * <p>
 * 该类用于模拟银行账户，并提供线程安全的转账功能。
 * 转账操作通过同步机制保证并发环境下的数据一致性。
 * 但是，将多个转账操作串行化，会导致性能下降。
 * </p>
 */
public class SafeTransferAccount {

    /** 账户余额，单位为货币最小单位（如分）。 */
    private long balance;

    /**
     * 将指定金额从当前账户转移到目标账户。
     * <p>
     * 该方法使用类级别的锁（{@code synchronized (SafeTransferAccount.class)}）来确保转账操作的原子性，
     * 避免多个线程同时操作导致的数据不一致问题。
     *
     * @param targetAccount 目标账户，不能为 {@code null}
     * @param transferMoney 转账金额，必须为非负值
     * @throws IllegalArgumentException 如果转账金额为负值
     * @throws NullPointerException 如果目标账户为 {@code null}
     */
    public void transferMoney(SafeTransferAccount targetAccount, long transferMoney) {
        if (transferMoney < 0) {
            throw new IllegalArgumentException("转账金额不能为负值");
        }
        if (targetAccount == null) {
            throw new NullPointerException("目标账户不能为空");
        }

        // 使用类级别的锁保证转账操作的线程安全性
        synchronized (SafeTransferAccount.class) {
            // 检查当前账户余额是否足够进行转账
            if (this.balance >= transferMoney) {
                this.balance -= transferMoney; // 扣除当前账户余额
                targetAccount.balance += transferMoney; // 增加目标账户余额
            }
        }
    }
}
