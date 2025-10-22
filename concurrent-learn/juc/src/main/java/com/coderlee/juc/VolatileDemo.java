package com.coderlee.juc;

/**
 * 一、VolatileDemo 类用于演示 Java 中 `volatile` 关键字的作用，以及与 `synchronized` 的对比。
 * 
 * 主要功能：
 * 1. **演示线程间变量的可见性问题**：通过主线程和子线程对共享变量的操作，展示多线程环境下变量可见性的重要性。
 * 2. **对比 `volatile` 和 `synchronized` 的使用场景**：分别使用普通变量、`synchronized` 同步块和 `volatile` 关键字实现线程间通信，分析它们的特点和适用场景。
 * 
 * 注意事项：
 * - `volatile` 关键字可以保证共享变量在多线程环境下的可见性，但不具备互斥性和原子性。
 * - `synchronized` 提供了更强的同步机制，包括互斥性和可见性，但相对开销较大。
 * 
 * @author coderLee
 * @createTime 2021/8/23 23:09
 */
public class VolatileDemo {

    public static void main(String[] args) {
        // doOrigin(); // 演示普通变量的线程可见性问题
        // doBySynchronized(); // 使用 synchronized 解决线程可见性问题
        doVolatile(); // 使用 volatile 解决线程可见性问题
    }

    /**
     * 使用 `volatile` 关键字解决线程可见性问题。
     * 
     * 场景描述：
     * - 创建一个 ThreadDemo1 实例并启动新线程。
     * - 主线程通过轮询的方式检查共享变量 flag 的值。
     * - 由于 flag 被声明为 `volatile`，子线程对 flag 的修改会立即同步到主内存，主线程能够感知到最新的值。
     * 
     * 结果：
     * - 主线程会在 flag 被修改后退出循环，并打印分隔符。
     */
    private static void doVolatile() {
        ThreadDemo1 td = new ThreadDemo1();
        new Thread(td).start();
        while (true) {
            if (td.isFlag()) {
                System.out.println("======================");
                break;
            }
        }
    }

    /**
     * 使用 `synchronized` 关键字解决线程可见性问题。
     * 
     * 场景描述：
     * - 创建一个 ThreadDemo 实例并启动新线程。
     * - 主线程通过 `synchronized` 锁定共享对象 `td`，确保每次读取 flag 时都能从主内存中获取最新值。
     * 
     * 结果：
     * - 主线程会在 flag 被修改后退出循环，并打印分隔符。
     */
    @SuppressWarnings("unused")
    private static void doBySynchronized() {
        ThreadDemo td = new ThreadDemo();
        new Thread(td).start();
        while (true) {
            synchronized (td) {
                if (td.isFlag()) {
                    System.out.println("======================");
                    break;
                }
            }
        }
    }

    /**
     * 演示普通变量在线程间的可见性问题。
     * 
     * 场景描述：
     * - 创建一个 ThreadDemo 实例并启动新线程。
     * - 主线程通过轮询的方式检查共享变量 flag 的值。
     * - 由于 flag 未被声明为 `volatile` 或使用其他同步机制，主线程可能无法感知到子线程对 flag 的修改。
     * 
     * 问题分析：
     * 1. **缺少 `volatile` 关键字**：主线程可能使用其本地缓存中的旧值，而不会从主内存中重新读取最新的 flag 值。
     * 2. **线程可见性问题**：在多线程环境中，一个线程对共享变量的修改可能对其他线程不可见。
     * 
     * 结果：
     * - 如果没有额外的同步机制，主线程可能会永远读取到 flag 的初始值 `false`，导致 `while` 循环无法退出。
     */
    @SuppressWarnings("unused")
    private static void doOrigin() {
        ThreadDemo td = new ThreadDemo();
        new Thread(td).start();
        while (true) {
            if (td.isFlag()) {
                System.out.println("======================");
                break;
            }
        }
    }
}

/**
 * 实现 `Runnable` 接口的类，用于在独立线程中修改变量 flag。
 * 
 * 特点：
 * - flag 是普通变量，未使用 `volatile` 或其他同步机制。
 * - 子线程对 flag 的修改可能对主线程不可见，导致线程可见性问题。
 */
class ThreadDemo implements Runnable {

    private boolean flag = false;

    @Override
    public void run() {
        try {
            Thread.sleep(2000L); // 模拟耗时操作
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        flag = true; // 修改共享变量
        System.out.println("flag is " + this.isFlag());
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}

/**
 * 实现 `Runnable` 接口的类，用于在独立线程中修改共享变量 flag。
 * 
 * 特点：
 * - flag 被声明为 `volatile`，确保其对所有线程可见。
 * - 子线程对 flag 的修改会立即同步到主内存，主线程能够感知到最新的值。
 */
class ThreadDemo1 implements Runnable {

    private volatile boolean flag = false; // 使用 volatile 确保可见性

    @Override
    public void run() {
        try {
            Thread.sleep(2000L); // 模拟耗时操作
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        flag = true; // 修改共享变量
        System.out.println("flag is " + this.isFlag());
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}