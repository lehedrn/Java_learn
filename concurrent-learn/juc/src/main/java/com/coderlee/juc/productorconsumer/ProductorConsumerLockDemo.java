package com.coderlee.juc.productorconsumer;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 6.生产者和消费者案例
 * 使用 `ReentrantLock` 和 `Condition` 替代 `synchronized` 和 `wait`/`notify`。
 * 提供了更灵活的线程同步机制。
 * 通过 `lock.lock()` 和 `lock.unlock()` 显式管理锁。
 * 使用 `Condition` 实现线程间的精准通知（如 `condition.await()` 和 `condition.signalAll()`）。
 * 相较于 `synchronized`，提供了更高的灵活性和性能。
 */
public class ProductorConsumerLockDemo {

    public static void main(String[] args) { 
        ClerkLock clerk = new ClerkLock();
        ProductorLock productor = new ProductorLock(clerk);
        ConsumerLock consumer = new ConsumerLock(clerk);
        new Thread(productor, "生产者A").start();
        new Thread(consumer, "消费者B").start();
        new Thread(productor, "生产者C").start();
        new Thread(consumer, "消费者D").start();
    }
}

/**
 * 店员
 */
class ClerkLock {
    private int product = 0;

    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    // 进货
    public void get() {
        lock.lock();
        try {
            while (product >= 1) {
                System.out.println("货已满");
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } 
            System.out.println(Thread.currentThread().getName() + ":正在进货, 目前数量: " + ++product);
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    // 卖货
    public void sale() {
        lock.lock();
        try { 
            while (product <= 0) {
                System.out.println("货已空");
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } 
            System.out.println(Thread.currentThread().getName() + ":正在卖货, 目前数量: " + --product);
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }
}

/**
 * 生产者
 */
class ProductorLock implements Runnable {
    private ClerkLock clerk;

    public ProductorLock(ClerkLock clerk) {
        this.clerk = clerk;
    }
    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            try {
                Thread.sleep(200L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.clerk.get();
        }
    }
}

/**
 * 消费者
 */
class ConsumerLock implements Runnable {
    private ClerkLock clerk;

    public ConsumerLock(ClerkLock clerk) {
        this.clerk = clerk;
    }
    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            this.clerk.sale();
        }
    }
}
