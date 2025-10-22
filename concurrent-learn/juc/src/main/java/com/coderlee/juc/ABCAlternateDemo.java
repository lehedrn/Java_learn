package com.coderlee.juc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

/**
 * ABCAlternateDemo 类用于演示多线程协作的场景。 具体功能是实现三个线程（A、B、C）按照顺序交替打印各自的名称。
 * 通过 ReentrantLock 和 Condition 实现了线程间的精确同步和交替执行
 */
public class ABCAlternateDemo {

    public static void main(String[] args) {
        // 创建一个 AlternateDemo 对象，指定线程数量为 3
        AlternateDemo alternateDemo = new AlternateDemo(3);

        // 启动线程 A，负责执行索引为 0 的任务
        new Thread(() -> {
            IntStream.range(0, 10).forEach(i -> {
                alternateDemo.loop(0, i, 1); // 调用 loop 方法完成打印任务
            });
        }, "A").start();

        // 启动线程 B，负责执行索引为 1 的任务
        new Thread(() -> {
            IntStream.range(0, 10).forEach(i -> {
                alternateDemo.loop(1, i, 1); // 调用 loop 方法完成打印任务
            });
        }, "B").start();

        // 启动线程 C，负责执行索引为 2 的任务
        new Thread(() -> {
            IntStream.range(0, 10).forEach(i -> {
                alternateDemo.loop(2, i, 1); // 调用 loop 方法完成打印任务
                System.out.println("==========================================");
            });
        }, "C").start();
    }
}

/**
 * AlternateDemo 类封装了线程间的协作逻辑。 使用 ReentrantLock 和 Condition 实现线程同步与交替执行。
 */
class AlternateDemo {

    private final int threadCount; // 线程总数
    private int currentIndex = 0; // 当前应该执行的线程索引
    private final Lock lock = new ReentrantLock(); // 用于线程同步的锁对象
    private final Condition[] conditions; // 每个线程对应的 Condition 对象数组

    /**
     * 构造方法，初始化线程数量和 Condition 数组。
     *
     * @param threadCount 线程总数
     */
    public AlternateDemo(int threadCount) {
        this.threadCount = threadCount;
        this.conditions = new Condition[threadCount]; // 初始化 Condition 数组
        for (int i = 0; i < threadCount; i++) {
            conditions[i] = lock.newCondition(); // 为每个线程创建一个独立的 Condition 对象
        }
    }

    /**
     * loop 方法实现线程的按序执行逻辑。
     *
     * @param index     当前线程的索引
     * @param totalLoop 总的循环次数
     * @param loopNum   每次循环需要打印的次数
     */
    public void loop(int index, int totalLoop, int loopNum) {
        lock.lock(); // 获取锁，确保线程安全
        try {
            // 如果当前线程不是应该执行的线程，则进入等待状态
            while (currentIndex != index) {
                conditions[index].await(); // 当前线程等待
            }

            // 执行打印操作
            for (int i = 0; i < loopNum; i++) {
                System.out.println(Thread.currentThread().getName() + "\t" + i + "\t" + totalLoop);
            }

            // 更新当前索引，设置为下一个线程的索引
            currentIndex = (currentIndex + 1) % threadCount;

            // 唤醒下一个线程
            conditions[currentIndex].signal();
        } catch (InterruptedException e) {
            e.printStackTrace(); // 捕获中断异常并打印堆栈信息
        } finally {
            lock.unlock(); // 释放锁，确保资源正确释放
        }
    }
}