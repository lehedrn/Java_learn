package com.coderlee.juc.productorconsumer;

/**
 * 4.生产者和消费者案例
 * 复现了 ProductorConsumerDemoV3 中的死锁问题，并尝试通过增加更多的生产者和消费者线程来暴露问题。
 * 问题：
 *      通过多个生产者和消费者并发，虚假唤醒问题仍然存在，程序可能陷入不可恢复的状态。
 */
public class ProductorConsumerDemoV4 {

    public static void main(String[] args) {
        // handler1();
        handler2();
    }

    /**
     * 但是如果出现多个生产者和消费者又会出现新的问题（虚假唤醒）
     */
    public static void handler2() { 
        ClerkV4 clerk = new ClerkV4();
        ProductorV4 productor = new ProductorV4(clerk);
        ConsumerV4 consumer = new ConsumerV4(clerk);
        new Thread(productor, "生产者A").start();
        new Thread(consumer, "消费者B").start();
        new Thread(productor, "生产者C").start();
        new Thread(consumer, "消费者D").start();
    }

    /**
     * 解决了3.生产者和消费者案例中线程卡死在运行一段时间后一直卡在货已满无法卖货的状态
     */
    public static void handler1() { 
        ClerkV4 clerk = new ClerkV4();
        ProductorV4 productor = new ProductorV4(clerk);
        ConsumerV4 consumer = new ConsumerV4(clerk);
        new Thread(productor, "生产者A").start();
        new Thread(consumer, "消费者B").start();
    }

}

/**
 * 店员
 */
class ClerkV4 {
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
        } 
        System.out.println(Thread.currentThread().getName() + ":正在进货, 目前数量: " + ++product);
        this.notifyAll();
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
        } 
        System.out.println(Thread.currentThread().getName() + ":正在卖货, 目前数量: " + --product);
        this.notifyAll();
    }
}

/**
 * 生产者
 */
class ProductorV4 implements Runnable {
    private ClerkV4 clerk;

    public ProductorV4(ClerkV4 clerk) {
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
class ConsumerV4 implements Runnable {
    private ClerkV4 clerk;

    public ConsumerV4(ClerkV4 clerk) {
        this.clerk = clerk;
    }
    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            this.clerk.sale();
        }
    }
}
