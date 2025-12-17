package com.coderlee.juc1.atomics;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * AtomicIntegerFieldUpdater 原子整数字段更新器演示类
 * 展示如何使用AtomicIntegerFieldUpdater对对象的特定字段进行原子操作
 */
@Slf4j
public class AtomicIntegerFieldUpdaterDemo {
    // 定义线程数量常量
    private static final int THREAD_COUNT = 10;

    /**
     * 主方法，演示多线程环境下对银行账户余额的原子性操作
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 创建银行账户实例
        BankAccount bankAccount = new BankAccount();
        // 使用CountDownLatch确保所有线程执行完毕
        CountDownLatch countDownLatch = new CountDownLatch(THREAD_COUNT);

        // 启动10个线程
        for (int i = 0; i < THREAD_COUNT; i++) {
            new Thread(() -> {
                // 每个线程对账户余额执行1000次递增操作
                for (int j = 1; j <= 1000; j++) {
                    bankAccount.incrementMoney();
                }
                // 线程执行完成后计数器减1
                countDownLatch.countDown();
            }, String.valueOf(i)).start();
        }

        try {
            // 等待所有线程执行完成
            countDownLatch.await();
        } catch (InterruptedException e) {
            log.error("error:", e);
        }

        // 输出最终银行余额，预期值为10000(10线程*1000次操作)
        log.info("银行余额：{}", bankAccount.getMoney());
    }
}

/**
 * 银行账户类
 * 演示使用AtomicIntegerFieldUpdater对特定字段进行原子操作
 */
class BankAccount {
    // 银行名称
    String bankName = "CCB";

    // 银行余额字段，使用volatile保证可见性
    private volatile int money = 0;

    // 原子整数字段更新器，声明为static final以提高性能
    // 用于对BankAccount类的"money"字段进行原子操作
    private static final AtomicIntegerFieldUpdater<BankAccount> UPDATER =
            AtomicIntegerFieldUpdater.newUpdater(BankAccount.class, "money");

    /**
     * 原子性地递增银行余额
     * 使用AtomicIntegerFieldUpdater实现线程安全的字段更新
     */
    public void incrementMoney() {
        // 对当前实例的money字段进行原子递增操作
        UPDATER.getAndIncrement(this);
    }

    /**
     * 获取当前银行余额
     * @return 当前余额值
     */
    public int getMoney() {
        return money;
    }
}
