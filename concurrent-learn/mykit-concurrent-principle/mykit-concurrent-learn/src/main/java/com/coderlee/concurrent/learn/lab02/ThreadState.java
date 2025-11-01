package com.coderlee.concurrent.learn.lab02;

/**
 * ThreadState 是一个用于演示线程状态的主类。
 * 该类通过创建多个线程并运行不同的任务（{@link WaitingTime}、{@link WaitingState} 和 {@link BlockedThread}），
 * 展示线程在运行过程中可能进入的不同状态，包括等待状态和阻塞状态。
 */
public class ThreadState {

    /**
     * 主方法，程序的入口点。
     * 在该方法中创建了四个线程：
     * - 一个线程运行 {@link WaitingTime} 任务，模拟线程的定时等待行为。
     * - 一个线程运行 {@link WaitingState} 任务，模拟线程的等待状态。
     * - 两个线程运行 {@link BlockedThread} 任务，模拟线程的阻塞状态。
     * 通过观察这些线程的行为，可以更好地理解线程状态的变化。
     * 
     * 运行后，
     * 1. 通过jps查看进程，命令 `jps`
     * 2. 通过jstack查看进程中所有线程的运行状态与调用栈，命令 jstack <进程ID>
     *
     */
    public static void main(String[] args) {
        // 创建并启动一个线程运行 WaitingTime 任务，线程名称为 "WaitingTimeThread"
        new Thread(new WaitingTime(), "WaitingTimeThread").start();

        // 创建并启动一个线程运行 WaitingState 任务，线程名称为 "WaitingStateThread"
        new Thread(new WaitingState(), "WaitingStateThread").start();

        // 创建并启动两个线程运行 BlockedThread 任务，线程名称分别为 "BlockedThread-01" 和 "BlockedThread-02"
        // BlockedThread-01线程会抢到锁，则BlockedThread-02线程会阻塞
        new Thread(new BlockedThread(), "BlockedThread-01").start();
        new Thread(new BlockedThread(), "BlockedThread-02").start();
    }
}