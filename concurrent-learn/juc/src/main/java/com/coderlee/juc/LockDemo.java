package com.coderlee.juc;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * LockDemo 类用于演示多线程环境下的售票系统实现。
 * 该类通过三种不同的售票实现方式（无锁、显式锁 ReentrantLock、volatile 关键字），
 * 展示了线程安全问题及不同同步机制的效果。
 */
public class LockDemo {

    public static void main(String[] args) {
        // handler1(); // 无锁实现，可能会引发线程安全问题
        handler2(); // 使用 ReentrantLock 实现线程安全
        // handler3(); // 使用 volatile 关键字，仅保证可见性，无法解决原子性问题
    }

    /**
     * handler3 方法使用 Ticket3 类实现售票逻辑。
     * Ticket3 使用 volatile 关键字修饰共享变量 ticket，但未解决线程安全问题。
     */
    public static void handler3() {
        Ticket3 ticket = new Ticket3();
        // 启动三个线程模拟三个售票窗口
        new Thread(ticket, "窗口1").start();
        new Thread(ticket, "窗口2").start();
        new Thread(ticket, "窗口3").start();
    }

    /**
     * handler2 方法使用 Ticket2 类实现售票逻辑。
     * Ticket2 使用 ReentrantLock 显式锁来保证线程安全。
     */
    public static void handler2() {
        Ticket2 ticket = new Ticket2();
        // 启动三个线程模拟三个售票窗口
        new Thread(ticket, "窗口1").start();
        new Thread(ticket, "窗口2").start();
        new Thread(ticket, "窗口3").start();
    }

    /**
     * handler1 方法使用 Ticket 类实现售票逻辑。
     * Ticket 未使用任何同步机制，可能会导致线程安全问题。
     */
    public static void handler1() {
        Ticket ticket = new Ticket();
        // 启动三个线程模拟三个售票窗口
        new Thread(ticket, "窗口1").start();
        new Thread(ticket, "窗口2").start();
        new Thread(ticket, "窗口3").start();
    }

}

/**
 * Ticket 类实现了 Runnable 接口，用于模拟售票逻辑。
 * 该实现未使用任何同步机制，可能会导致线程安全问题（如超卖或余票不一致）。
 */
class Ticket implements Runnable {
    private int ticket = 100; // 共享变量，表示剩余票数

    @Override
    public void run() {
        while (true) {
            if (ticket > 0) { // 检查是否还有余票
                try {
                    Thread.sleep(200L); // 模拟售票耗时
                } catch (InterruptedException e) {
                    // 忽略中断异常
                }
                System.out.println(Thread.currentThread().getName() + " 完成售票，余票为：" + --ticket);
            }
        }
    }
}

/**
 * Ticket2 类实现了 Runnable 接口，用于模拟售票逻辑。
 * 该实现使用 ReentrantLock 显式锁来保证线程安全。
 */
class Ticket2 implements Runnable {

    private int ticket = 100; // 共享变量，表示剩余票数
    private Lock lock = new ReentrantLock(); // 创建 ReentrantLock 实例

    @Override
    public void run() {
        while (true) {
            lock.lock(); // 获取锁，确保同一时间只有一个线程进入临界区
            try {
                if (ticket > 0) { // 检查是否还有余票
                    try {
                        Thread.sleep(200L); // 模拟售票耗时
                    } catch (InterruptedException e) {
                        // 忽略中断异常
                    }
                    System.out.println(Thread.currentThread().getName() + " 完成售票，余票为：" + --ticket);
                }
            } finally {
                lock.unlock(); // 释放锁，确保其他线程可以继续执行
            }
        }
    } 
}

/**
 * Ticket3 类实现了 Runnable 接口，用于模拟售票逻辑。
 * 该实现使用 volatile 关键字修饰共享变量 ticket，但未解决线程安全问题。
 */
class Ticket3 implements Runnable {
    private volatile int ticket = 100; // 使用 volatile 修饰共享变量，保证可见性

    @Override
    public void run() {
        while (true) {
            if (ticket > 0) { // 检查是否还有余票
                try {
                    Thread.sleep(200L); // 模拟售票耗时
                } catch (InterruptedException e) {
                    // 忽略中断异常
                }
                System.out.println(Thread.currentThread().getName() + " 完成售票，余票为：" + --ticket);
            }
        }
    }
}