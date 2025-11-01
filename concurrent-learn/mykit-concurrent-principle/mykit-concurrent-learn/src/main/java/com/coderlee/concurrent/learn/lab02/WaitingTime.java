package com.coderlee.concurrent.learn.lab02;

import java.util.concurrent.TimeUnit;

/**
 * WaitingTime 
 * 一个实现了 {@link Runnable} 接口的类，用于在线程中执行周期性等待的任务。
 * 该类的核心功能是通过调用 {@link #waitSecond(long)} 方法，按照指定的时间间隔进行休眠，并在循环中不断重复此操作。
 *
 * 在while(true)循环中调用TimeUnit.SECONDS.sleep(long)方法来验证线程的TIMED_WARTING状态
 */
public class WaitingTime implements Runnable {

    /**
     * 实现了 {@link Runnable#run()} 方法，定义了一个无限循环的任务。
     * 在每次循环中，调用 {@link #waitSecond(long)} 方法使当前线程休眠指定的时间（以秒为单位）。
     */
    @Override
    public void run() {
        while (true) { // 无限循环，持续执行等待逻辑
            waitSecond(200); // 每次循环等待 200 秒
        }
    }

    /**
     * 让当前线程休眠指定的秒数。
     *
     * @param seconds 要休眠的时间，单位为秒
     *                如果休眠过程中被中断，则捕获 {@link InterruptedException} 并打印堆栈信息。
     */
    public static final void waitSecond(long seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds); // 使用 {@link TimeUnit.SECONDS#sleep} 方法实现线程休眠
        } catch (InterruptedException e) {
            e.printStackTrace(); // 捕获中断异常并打印堆栈信息
        }
    }
}
