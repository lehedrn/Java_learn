package com.coderlee.juc;

import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DeadLockDemo {
    public static void main(String[] args) {
        new DeadLockDemo().dealLock();
    }

    private void dealLock() { 
        Object a = new Object();
        Object b = new Object();
        Thread t1 = new Thread(() -> {
            synchronized(a) {
                log.info("{} 拿到A锁，正在获取B锁", Thread.currentThread().getName());
                synchronized(b) {
                    try {
                        TimeUnit.SECONDS.sleep(1L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    log.info("{} 拿到B锁", Thread.currentThread().getName());
                }
                log.info("{} 释放B锁", Thread.currentThread().getName());
                log.info("{} 释放A锁", Thread.currentThread().getName());
            }
        }, "t1");
        Thread t2 = new Thread(() -> {
            synchronized(b) {
                log.info("{} 拿到B锁，正在获取A锁", Thread.currentThread().getName());
                synchronized(a) {
                    log.info("{} 拿到A锁", Thread.currentThread().getName());
                }
                log.info("{} 释放A锁", Thread.currentThread().getName());
                log.info("{} 释放B锁", Thread.currentThread().getName());
            }
        }, "t2");
        t1.start();
        t2.start();
    }
}
