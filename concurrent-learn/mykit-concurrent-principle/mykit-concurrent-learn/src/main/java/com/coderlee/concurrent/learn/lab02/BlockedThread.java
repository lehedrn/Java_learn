package com.coderlee.concurrent.learn.lab02;

/**
 * BlockedThread 
 * 一个实现了 {@link Runnable} 接口的类，用于模拟线程阻塞状态。
 * 该类的核心功能是通过 {@link synchronized} 关键字锁住类对象，
 * 并在无限循环中调用 {@link WaitingTime#waitSecond(long)} 方法使线程持续占用锁。
 * 其他线程如果尝试获取同一锁将会进入阻塞状态。
 * 
 * 在synchronized代码块中的while(true)循环中调用TimeUnit.SECONDS.sleep(long)方法来验证线程的BLOCKED状态。
 * 当启动两个BlockedThread线程时，首先启动的线程会处于TIMED_WAITING状态，后启动的线程会处于BLOCKED状态。
 * 
 */
public class BlockedThread implements Runnable {

    /**
     * 实现了 {@link Runnable#run()} 方法，定义了一个无限循环的任务。
     * 在同步块中锁住 {@link BlockedThread.class} 对象，并调用 {@link WaitingTime#waitSecond(long)} 方法，
     * 使当前线程周期性地休眠，同时保持锁的占用。
     */
    @Override
    public void run() {
        synchronized (BlockedThread.class) { // 使用类对象作为锁，确保线程安全
            while (true) { // 无限循环，持续执行任务逻辑
                WaitingTime.waitSecond(100); // 调用静态方法，让线程休眠 100 秒
            }
        }
    }
}