package com.coderlee.concurrent.learn.lab03;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>演示线程启动顺序的不确定性。</p>
 * 
 * <p>该类通过创建三个线程并直接调用 {@link Thread#start()} 方法，
 * 展示了多线程环境下线程执行顺序无法保证的现象。
 * 每个线程在启动后会打印自己的名称，但由于线程调度的不确定性，
 * 打印顺序可能与线程启动顺序不一致。</p>
 */
@Slf4j
public class Threadsort01 {

    /**
     * <p>程序入口方法。</p>
     * 
     * <p>创建并启动三个线程，分别为 "thread-lee-01"、"thread-lee-02" 和 "thread-lee-03"。
     * 每个线程在运行时会打印自己的名称，但由于线程调度的原因，
     * 实际打印顺序可能与线程启动顺序不同。</p>
     *
     * @param args 命令行参数（未使用）
     */
    public static void main(String[] args) {
        // 创建线程 t1，线程名称为 "thread-lee-01"
        Thread t1 = new Thread(() -> log.info("线程名称: [{}]", Thread.currentThread().getName()), "thread-lee-01");

        // 创建线程 t2，线程名称为 "thread-lee-02"
        Thread t2 = new Thread(() -> log.info("线程名称: [{}]", Thread.currentThread().getName()), "thread-lee-02");

        // 创建线程 t3，线程名称为 "thread-lee-03"
        Thread t3 = new Thread(() -> log.info("线程名称: [{}]", Thread.currentThread().getName()), "thread-lee-03");

        // 启动线程 t1
        t1.start();

        // 启动线程 t2
        t2.start();

        // 启动线程 t3
        t3.start();
    }
}