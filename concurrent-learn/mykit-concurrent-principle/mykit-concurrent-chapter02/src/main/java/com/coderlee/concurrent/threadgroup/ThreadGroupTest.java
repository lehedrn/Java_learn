package com.coderlee.concurrent.threadgroup;

/**
 * ThreadGroupTest 类用于演示 Java 中线程组（ThreadGroup）的基本用法。
 * <p>
 * 该类创建了一个自定义的线程组，并在该线程组中启动两个线程，分别打印线程组名称和线程名称。
 * 通过该示例，可以了解线程组如何组织和管理线程，以及如何获取线程和线程组的相关信息。
 * </p>
 */
public class ThreadGroupTest {

    /**
     * 主方法，程序入口。
     * <p>
     * 1. 创建一个名为 "threadGroupTest" 的线程组。
     * 2. 在该线程组中创建并启动两个线程（t1 和 t2）。
     * 3. 每个线程会打印其所属的线程组名称和自身的线程名称。
     * </p>
     *
     * @param args 命令行参数（未使用）。
     */
    public static void main(String[] args) {
        // 创建一个名为 "threadGroupTest" 的线程组
        ThreadGroup threadGroup = new ThreadGroup("threadGroupTest");

        // 创建线程 t1，并将其加入到指定的线程组中
        Thread t1 = new Thread(threadGroup, () -> {
            // 获取当前线程的线程组名称和线程名称，并打印
            String groupName = Thread.currentThread().getThreadGroup().getName();
            String threadName = Thread.currentThread().getName();
            System.out.println("线程组名称：" + groupName + "，线程名称：" + threadName);
        }, "t1"); // 线程名称为 "t1"

        // 创建线程 t2，并将其加入到指定的线程组中
        Thread t2 = new Thread(threadGroup, () -> {
            // 获取当前线程的线程组名称和线程名称，并打印
            String groupName = Thread.currentThread().getThreadGroup().getName();
            String threadName = Thread.currentThread().getName();
            System.out.println("线程组名称：" + groupName + "，线程名称：" + threadName);
        }, "t2"); // 线程名称为 "t2"

        // 启动线程 t1 和 t2
        t1.start();
        t2.start();
    }
}