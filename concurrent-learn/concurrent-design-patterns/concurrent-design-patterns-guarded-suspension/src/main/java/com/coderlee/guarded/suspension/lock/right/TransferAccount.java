package com.coderlee.guarded.suspension.lock.right;

/**
 * 转账账户类 - 正确实现示例
 *
 * 该类演示了使用类级别锁实现线程安全的转账操作，是Guarded Suspension模式的正确实现。
 * 通过使用 {@link TransferAccount} 作为同步锁，确保同一时间只有一个线程可以执行转账操作，
 * 避免了多线程环境下的并发问题。
 *
 * <p>
 * 与使用实例锁的方案相比，这种基于类锁的实现更加简单且易于维护，
 * 但是，并发性能极差。
 * 因为锁的是{@link TransferAccount}，会导致所有实例在执行{@link TransferAccount#transfer(TransferAccount, Integer)}方法时，都是互斥的。
 * 换句话说，所有的转账都会变成串行的。
 * </p>
 *
 */
public class TransferAccount {

    /**
     * 账户余额
     */
    private Integer balance;

    /**
     * 执行转账操作
     *
     * 使用 {@link TransferAccount} 作为同步锁，确保转账操作的原子性和线程安全。
     * 只有当源账户余额充足时才会执行转账操作。
     *
     * @param target 目标转账账户
     * @param transferMoney 转账金额
     */
    public void transfer(TransferAccount target, Integer transferMoney) {
        // 使用类锁确保同一时间只有一个线程可以执行转账操作
        synchronized (TransferAccount.class) {
            // 检查当前账户余额是否足够转账
            if (this.balance >= transferMoney) {
                // 从当前账户扣除转账金额
                this.balance -= transferMoney;
                // 向目标账户增加转账金额
                target.balance += transferMoney;
            }
        }
    }

    // 在创建TansferAccount对象时传入同一个balanceLock锁对象的方案，虽然能够解决转账的并发问题，但是却无法在实际项目中被有效的采用
    /*private Integer balance;
    private Object balanceLock;
    private TransferAccount() {}
    public TransferAccount(Object balanceLock) {
        this.balanceLock = balanceLock;
    }
    public void transfer(TransferAccount target, Integer transferMoney) {
        synchronized (this.balanceLock) {
            if (this.balance >= transferMoney) {
                this.balance -= transferMoney;
                target.balance += transferMoney;
            }
        }
    }*/
}
