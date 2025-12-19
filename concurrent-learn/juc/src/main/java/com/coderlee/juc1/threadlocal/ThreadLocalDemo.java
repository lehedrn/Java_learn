package com.coderlee.juc1.threadlocal;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * ThreadLocal 使用示例
 *
 * 通过模拟房产销售人员的销售统计场景，展示 ThreadLocal 在多线程环境下的应用
 */
@Slf4j
public class ThreadLocalDemo {
    public static void main(String[] args) {
//        demo1();  // 普通共享变量统计
        demo2();    // ThreadLocal 独立统计
    }

    /**
     * 需求2: 5个销售卖完随机数房子，各自独立销售额度，
     * 自己业绩按提成走，分灶吃饭，各个销售自己动手，丰衣足食
     *
     * 使用 ThreadLocal 实现每个销售员独立统计自己的销售业绩
     */
    public static void demo2() {
        Hourse hourse = new Hourse();
        // 创建一个倒计时门闩，等待5个线程执行完毕
        CountDownLatch countDownLatch = new CountDownLatch(5);

        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                try {
                    // 每个销售员随机卖出1-5套房子
                    int size = new Random().nextInt(5) + 1;
                    for (int j = 1; j <= size; j++) {
                        hourse.saleHourse();              // 全局销售总数增加
                        hourse.saleVolumeByThreadLocal(); // 当前销售员个人业绩增加
                    }
                    // 输出当前线程(销售员)的个人销售业绩
                    log.info("[{}] sale {} house", Thread.currentThread().getName(), hourse.saleVolume.get());
                } finally {
                    // 清理 ThreadLocal 变量，防止内存泄漏
                    hourse.saleVolume.remove();
                    // 倒计时减一
                    countDownLatch.countDown();
                }
            }, String.valueOf(i)).start(); // 线程命名为销售员编号
        }

        try {
            // 等待所有销售员完成销售任务
            countDownLatch.await();
        } catch (InterruptedException e) {
            log.error("countDownLatch error", e);
        }
        // 输出总销售数量
        log.info("total sale count: {}", hourse.saleCount);
    }

    /**
     * 需求1：5个销售买房子，集团高层只关心销售总量的准确统计数。
     *
     * 使用普通的同步方法统计全局销售总额
     */
    public static void demo1() {
        Hourse hourse = new Hourse();
        // 创建一个倒计时门闩，等待5个线程执行完毕
        CountDownLatch countDownLatch = new CountDownLatch(5);

        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                try {
                    // 每个销售员随机卖出1-5套房子
                    int size = new Random().nextInt(5) + 1;
                    log.info("[{}] sale {} house", Thread.currentThread().getName(), size);

                    // 循环完成销售操作
                    for (int j = 1; j <= size; j++) {
                        hourse.saleHourse(); // 全局销售总数增加
                    }
                } finally {
                    // 倒计时减一
                    countDownLatch.countDown();
                }
            }, String.valueOf(i)).start(); // 线程命名为销售员编号
        }

        try {
            // 等待所有销售员完成销售任务
            countDownLatch.await();
        } catch (InterruptedException e) {
            log.error("countDownLatch error", e);
        }
        // 输出总销售数量
        log.info("total sale count: {}", hourse.saleCount);
    }

}

/**
 * 房产销售类
 * 包含全局销售计数器和基于 ThreadLocal 的个人销售业绩统计
 */
class Hourse {
    // 全局销售总数，所有线程共享
    int saleCount = 0;

    /**
     * 同步方法，保证全局销售计数的准确性
     * 多线程环境下对共享变量的操作需要同步
     */
    public synchronized void saleHourse() {
        ++saleCount;
    }

    // ThreadLocal 变量，为每个线程维护独立的销售额度
    ThreadLocal<Integer> saleVolume = ThreadLocal.withInitial(() -> 0);

    /**
     * 更新当前线程(销售员)的个人销售业绩
     * 每个线程拥有独立的 saleVolume 值，互不影响
     */
    public void saleVolumeByThreadLocal() {
        saleVolume.set(1 + saleVolume.get());
    }
}
