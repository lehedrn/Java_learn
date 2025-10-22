package com.coderlee.juc.productorconsumer;

/**
 * 3.生产者和消费者案例
 * 在 ProductorConsumerDemoV2 的基础上，引入了 Thread.sleep() 模拟生产者进货的时间延迟。
 * 问题：
 *      程序运行一段时间后可能出现线程死锁（卡在“货已满”状态无法继续）。
 *      原因是 `if` 判断可能导致线程在不满足条件时被错误唤醒。
 */
public class ProductorConsumerDemoV3 {
    public static void main(String[] args) { 
        ClerkV3 clerk = new ClerkV3();
        ProductorV3 productor = new ProductorV3(clerk);
        ConsumerV3 consumer = new ConsumerV3(clerk);
        new Thread(productor, "生产者A").start();
        new Thread(consumer, "消费者B").start();
    }

}

/**
 * 店员
 */
class ClerkV3 {
    private int product = 0;

    // 进货
    public synchronized void get() {
        if (product >= 1) {
            System.out.println("货已满");
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(Thread.currentThread().getName() + ":正在进货, 目前数量: " + ++product);
            this.notifyAll();
        }
    }

    // 卖货
    public synchronized void sale() {
        if (product <= 0) {
            System.out.println("货已空");
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(Thread.currentThread().getName() + ":正在卖货, 目前数量: " + --product);
            this.notifyAll();
        }
    }
}

/**
 * 生产者
 */
class ProductorV3 implements Runnable {
    private ClerkV3 clerk;

    public ProductorV3(ClerkV3 clerk) {
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
class ConsumerV3 implements Runnable {
    private ClerkV3 clerk;

    public ConsumerV3(ClerkV3 clerk) {
        this.clerk = clerk;
    }
    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            this.clerk.sale();
        }
    }
}
