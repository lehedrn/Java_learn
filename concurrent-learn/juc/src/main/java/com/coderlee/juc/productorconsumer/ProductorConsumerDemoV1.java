package com.coderlee.juc.productorconsumer;

/**
 * 1.生产者和消费者案例
 * 未添加唤醒等待机制，只使用了synchronized锁
 * 生产者和消费者分别调用 get() 和 sale() 方法操作共享资源（商品数量）。
 * 问题：
 *      如果库存已满或为空，线程不会阻塞，而是直接跳过操作，导致无效循环。
 *      缺乏线程间的协调机制。
 */
public class ProductorConsumerDemoV1 {

    public static void main(String[] args) { 
        ClerkV1 clerk = new ClerkV1();
        ProductorV1 productor = new ProductorV1(clerk);
        ConsumerV1 consumer = new ConsumerV1(clerk);
        new Thread(productor, "生产者A").start();
        new Thread(consumer, "消费者B").start();
    }

}

/**
 * 店员
 */
class ClerkV1 {
    private int product = 0;

    // 进货
    public synchronized void get() {
        if (product >= 10) {
            System.out.println("货已满");
        } else {
            System.out.println(Thread.currentThread().getName() + ":正在进货, 目前数量: " + ++product);
        }
    }

    // 卖货
    public synchronized void sale() {
        if (product <= 0) {
            System.out.println("货已空");
        } else {
            System.out.println(Thread.currentThread().getName() + ":正在卖货, 目前数量: " + --product);
        }
    }
}

/**
 * 生产者
 */
class ProductorV1 implements Runnable {
    private ClerkV1 clerk;

    public ProductorV1(ClerkV1 clerk) {
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
class ConsumerV1 implements Runnable {
    private ClerkV1 clerk;

    public ConsumerV1(ClerkV1 clerk) {
        this.clerk = clerk;
    }
    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            this.clerk.sale();
        }
    }
}