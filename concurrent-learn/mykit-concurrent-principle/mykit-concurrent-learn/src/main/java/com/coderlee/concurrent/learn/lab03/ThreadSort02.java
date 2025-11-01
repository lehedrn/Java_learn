package com.coderlee.concurrent.learn.lab03;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>演示如何通过 {@link Thread#join()} 方法确保线程的执行顺序。</p>
 *
 * <p>该类通过创建三个线程并调用每个线程的 {@link Thread#join()} 方法，确保线程按照启动顺序依次执行完毕。
 * 每个线程在启动后会打印自己的名称，并在主线程中记录线程的状态信息。</p>
 * 
 */
@Slf4j
public class ThreadSort02 {

    /**
     * <p>程序入口方法。</p>
     *
     * <p>创建并启动三个线程，分别为 "thread-lee-01"、"thread-lee-02" 和 "thread-lee-03"。
     * 在每个线程启动后，调用其 {@link Thread#join()} 方法，确保当前线程执行完毕后再启动下一个线程。
     * 同时，通过调用 {@link #logThreadInfo(Thread, String)} 方法记录线程的状态信息。</p>
     *
     * @throws InterruptedException 如果当前线程在等待过程中被中断，则抛出此异常
     */
    public static void main(String[] args) throws InterruptedException {
        // 创建线程 t1，线程名称为 "thread-lee-01"
        Thread t1 = new Thread(() -> log.info("线程名称: [{}]", Thread.currentThread().getName()), "thread-lee-01");

        // 创建线程 t2，线程名称为 "thread-lee-02"
        Thread t2 = new Thread(() -> log.info("线程名称: [{}]", Thread.currentThread().getName()), "thread-lee-02");

        // 创建线程 t3，线程名称为 "thread-lee-03"
        Thread t3 = new Thread(() -> log.info("线程名称: [{}]", Thread.currentThread().getName()), "thread-lee-03");

        // 启动线程 t1
        t1.start();
        logThreadInfo(t1, "t1线程启动");

        // 调用 t1.join()，确保 t1 执行完毕后再继续
        t1.join();
        logThreadInfo(t1, "t1线程结束");

        // 启动线程 t2
        t2.start();
        logThreadInfo(t2, "t2线程启动");

        // 调用 t2.join()，确保 t2 执行完毕后再继续
        t2.join();
        logThreadInfo(t2, "t2线程结束");

        // 启动线程 t3
        t3.start();
        logThreadInfo(t3, "t3线程启动");

        // 调用 t3.join()，确保 t3 执行完毕后再继续
        t3.join();
        logThreadInfo(t3, "t3线程结束");
    }

    /**
     * <p>记录线程的状态信息。</p>
     *
     * <p>该方法用于打印指定线程的状态信息，包括子线程和主线程的名称及状态。
     * 通过日志分隔符增强可读性。</p>
     *
     * @param thread 当前需要记录状态的线程对象
     * @param message 自定义的日志消息，用于描述当前操作
     */
    private static void logThreadInfo(Thread thread, String message) {
        log.info("=================================================>>>");
        log.info(message);
        log.info("子线程[{}]的状态: [{}]", thread.getName(), thread.getState());
        log.info("主线程[{}]的状态: [{}]", Thread.currentThread().getName(), Thread.currentThread().getState());
        log.info("=================================================<<<\n");
    }
}