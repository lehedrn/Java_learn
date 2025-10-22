package com.coderlee.juc.productorconsumer;

/**
 * 5.生产者和消费者案例
 * 解决4.生产者和消费者案例中虚假唤醒的问题
 * 为了避免虚假唤醒问题，应用总是使用while循环进行判断，而不要使用if进行判断
 */
public class ProductorConsumerDemoV5 {

    public static void main(String[] args) {
        handler();
    }

    public static void handler() { 
        ClerkV5 clerk = new ClerkV5();
        ProductorV5 productor = new ProductorV5(clerk);
        ConsumerV5 consumer = new ConsumerV5(clerk);
        new Thread(productor, "生产者A").start();
        new Thread(consumer, "消费者B").start();
        new Thread(productor, "生产者C").start();
        new Thread(consumer, "消费者D").start();
    }


}

/**
 * 店员
 */
class ClerkV5 {
    private int product = 0;

    // 进货
    public synchronized void get() {
        while (product >= 1) {
            System.out.println("货已满");
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } 
        System.out.println(Thread.currentThread().getName() + ":正在进货, 目前数量: " + ++product);
        this.notifyAll();
    }

    // 卖货
    public synchronized void sale() {
        while (product <= 0) {
            System.out.println("货已空");
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } 
        System.out.println(Thread.currentThread().getName() + ":正在卖货, 目前数量: " + --product);
        this.notifyAll();
    }
}

/**
 * 生产者
 */
class ProductorV5 implements Runnable {
    private ClerkV5 clerk;

    public ProductorV5(ClerkV5 clerk) {
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
class ConsumerV5 implements Runnable {
    private ClerkV5 clerk;

    public ConsumerV5(ClerkV5 clerk) {
        this.clerk = clerk;
    }
    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            this.clerk.sale();
        }
    }
}
