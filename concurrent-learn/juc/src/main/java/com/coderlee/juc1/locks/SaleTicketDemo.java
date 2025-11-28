package com.coderlee.juc1.locks;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 售票演示类
 *
 * 该类演示了使用ReentrantLock实现公平锁和非公平锁的售票场景，
 * 展示多线程环境下资源竞争的处理方式。
 */
public class SaleTicketDemo {

    public static void main(String[] args) {
//        nonFair(); // 非公平锁模式
        fair(); // 公平锁模式
    }

    /**
     * 演示非公平锁的售票过程
     *
     * 创建一个非公平锁的Ticket实例并启动售票线程
     */
    public static void nonFair() {
        Ticket ticket = new Ticket(false); // 创建非公平锁Ticket实例
        saleTicket(ticket); // 启动售票线程
    }

    /**
     * 演示公平锁的售票过程
     *
     * 创建一个公平锁的Ticket实例并启动售票线程
     */
    public static void fair() {
        Ticket ticket = new Ticket(true); // 创建公平锁Ticket实例
        saleTicket(ticket); // 启动售票线程
    }


    /**
     * 启动多个线程模拟售票过程
     *
     * 创建三个线程(A、B、C)，每个线程都会尝试销售55张票
     * @param ticket Ticket对象，包含实际的售票逻辑
     */
    private static void saleTicket(Ticket ticket) {
        // 线程A：负责销售55张票
        new Thread(() -> {
            for (int i = 0; i < 55; i++) {
                ticket.sale(); // 调用售票方法
            }
        }, "A").start();

        // 线程B：负责销售55张票
        new Thread(() -> {
            for (int i = 0; i < 55; i++) {
                ticket.sale(); // 调用售票方法
            }
        }, "B").start();

        // 线程C：负责销售55张票
        new Thread(() -> {
            for (int i = 0; i < 55; i++) {
                ticket.sale(); // 调用售票方法
            }
        }, "C").start();
    }
}


/**
 * 票务管理类
 *
 * 使用ReentrantLock控制对共享资源(number)的访问，
 * 实现线程安全的售票操作
 */
@Slf4j
class Ticket {
    private int number = 50; // 初始票数为50张
    Lock lock; // 锁对象，用于控制并发访问

    /**
     * 构造函数
     *
     * 根据参数决定创建公平锁还是非公平锁
     * @param fair true表示创建公平锁，false表示创建非公平锁
     */
    public Ticket(boolean fair) {
        lock = new ReentrantLock(fair); // 初始化指定类型的锁
    }

    /**
     * 售票方法
     *
     * 在获取锁后检查是否还有余票，如果有则售出一张票，
     * 并输出相关信息到日志
     */
    public void sale() {
        lock.lock(); // 获取锁
        try {
            // 检查是否还有票可售
            if (number > 0) {
                // 记录售票信息：当前线程名、售出的票号、剩余票数
                log.info("{} 卖出第 [{}] 张票，还剩下: {} 张票",
                         Thread.currentThread().getName(), // 当前线程名称
                         number--, // 当前售出的票号(先使用后减1)
                         number); // 剩余票数
            }
        } finally {
            lock.unlock(); // 释放锁，确保即使发生异常也能释放锁
        }
    }
}

