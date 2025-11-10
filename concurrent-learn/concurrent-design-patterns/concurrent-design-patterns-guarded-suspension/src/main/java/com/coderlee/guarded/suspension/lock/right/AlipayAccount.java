package com.coderlee.guarded.suspension.lock.right;

/**
 * 支付宝账户类
 * 该类演示了没有直接业务关系的场景——使用细粒度锁机制实现线程安全的账户操作。
 * 通过分离不同的资源锁（余额锁和密码锁），提高了并发性能
 */
public class AlipayAccount {

    /**
     * 账户余额
     */
    private Integer balance;

    /**
     * 账户密码
     */
    private String password;

    /**
     * 余额操作的专用锁对象
     */
    private final Object balanceLock = new Object();

    /**
     * 密码操作的专用锁对象
     */
    private final Object passwordLock = new Object();

    /**
     * 支付操作
     *
     * 使用balanceLock确保支付操作的线程安全。
     * 只有当余额充足时才会执行扣款操作。
     *
     * @param money 需要支付的金额
     */
    public void pay(Integer money) {
        // 使用专门的余额锁进行同步
        synchronized (balanceLock) {
            // 检查余额是否足够支付
            if (this.balance >= money) {
                // 执行扣款操作
                this.balance -= money;
            }
        }
    }

    /**
     * 获取账户余额
     *
     * 使用balanceLock确保读取余额的线程安全。
     *
     * @return 当前账户余额
     */
    public Integer getBalance() {
        // 使用专门的余额锁进行同步
        synchronized (balanceLock) {
            // 返回当前余额
            return this.balance;
        }
    }

    /**
     * 更新账户密码
     *
     * 使用passwordLock确保更新密码的线程安全。
     *
     * @param password 新的账户密码
     */
    public void updatePassword(String password) {
        // 使用专门的密码锁进行同步
        synchronized (passwordLock) {
            // 更新账户密码
            this.password = password;
        }
    }

    /**
     * 获取账户密码
     *
     * 使用passwordLock确保读取密码的线程安全。
     *
     * @return 当前账户密码
     */
    public String getPassword() {
        // 使用专门的密码锁进行同步
        synchronized (passwordLock) {
            // 返回当前密码
            return this.password;
        }
    }
}
