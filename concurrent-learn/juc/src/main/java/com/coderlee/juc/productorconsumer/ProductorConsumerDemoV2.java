package com.coderlee.juc.productorconsumer;

/**
 * 2.生产者和消费者案例
 * 引入了线程间通信机制，通过 `wait()` 和 `notifyAll()` 解决线程死循环问题。
 * 使用 `if` 判断条件来控制线程的等待与唤醒。
 * 引入了等待唤醒机制，避免会一直出现货已空或者货已满的情况
 * 问题：
 *      如果多个生产者和消费者同时运行，可能会出现虚假唤醒问题（即线程在不满足条件时被唤醒）。
 * @author coderLee
 *
 */
public class ProductorConsumerDemoV2 {
    public static void main(String[] args) { 
        ClerkV2 clerk = new ClerkV2();
        ProductorV2 productor = new ProductorV2(clerk);
        ConsumerV2 consumer = new ConsumerV2(clerk);
        new Thread(productor, "生产者A").start();
        new Thread(consumer, "消费者B").start();
    }

}

/**
 * 店员
 */
class ClerkV2 {
    private int product = 0;

    // 进货
    public synchronized void get() {
        if (product >= 10) {
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
class ProductorV2 implements Runnable {
    private ClerkV2 clerk;

    public ProductorV2(ClerkV2 clerk) {
        this.clerk = clerk;
    }
    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            this.clerk.get();
        }
    }
}

/**
 * 消费者
 */
class ConsumerV2 implements Runnable {
    private ClerkV2 clerk;

    public ConsumerV2(ClerkV2 clerk) {
        this.clerk = clerk;
    }
    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            this.clerk.sale();
        }
    }
}
