package com.coderlee.concurrent.learn.lab02;

/**
 * WaitingState 
 * 一个实现了 {@link Runnable} 接口的类，用于在线程中模拟等待状态。
 * 该类的核心功能是通过 {@link Object#wait()} 方法让线程在同步块中进入等待状态，并在无限循环中持续执行此逻辑。
 * 
 * 这个类无论创建多少个实例，synchronized锁都是同一个，并且线程会处于等待状态。
 * 接下来，在synchronized中使用当前类的Class对象的wait()方法，来验证线程的WAITING状态。
 */
public class WaitingState implements Runnable {

    /**
     * 实现了 {@link Runnable#run()} 方法，定义了一个无限循环的任务。
     * 在每次循环中，通过同步块和 {@link Object#wait()} 方法让当前线程进入等待状态。
     * 如果等待被中断，则捕获 {@link InterruptedException} 并打印堆栈信息。
     */
    @Override
    public void run() {
        while (true) { // 无限循环，持续执行等待逻辑
            synchronized (WaitingState.class) { // 使用类对象作为锁，确保线程安全
                try {
                    WaitingState.class.wait(); // 调用 {@link Object#wait()} 方法，使线程进入等待状态
                } catch (InterruptedException e) {
                    e.printStackTrace(); // 捕获中断异常并打印堆栈信息
                }
            }
        }
    }
}