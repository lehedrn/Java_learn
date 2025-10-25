package com.coderlee.concurrent.chapter07;

import java.util.function.Consumer;
import java.util.stream.IntStream;

import lombok.extern.slf4j.Slf4j;

/**
 * SynchronizedBlockTest 类用于演示和比较两种不同的同步机制在多线程环境下的使用效果。
 * 这两种同步机制分别是：`synchronized` 修饰方法和 `synchronized` 同步代码块。
 * 通过分别对两个计数器变量 {@code countA} 和 {@code countB} 进行操作，展示线程安全的实现方式及其性能差异。
 * 
 * @author coderlee
 */
@Slf4j
public class SynchronizedBlockTest {

    private Long countA = 0L; // 计数器 A，用于模拟并发场景中的共享资源。
    private Long countB = 0L; // 计数器 B，用于模拟并发场景中的共享资源。

    /**
     * 使用 `synchronized` 修饰整个方法来保证线程安全。
     * 在该方法中同时对 {@code countA} 和 {@code countB} 进行递增操作。
     * 此种方式会锁住整个方法，导致同一时刻只有一个线程能执行此方法。
     */
    public synchronized void incrementCount1() {
        countA++; // 递增计数器 A。
        countB++; // 递增计数器 B。
    }

    private Object countALock = new Object(); // 锁对象，用于保护对 {@code countA} 的访问。
    private Object countBLock = new Object(); // 锁对象，用于保护对 {@code countB} 的访问。

    /**
     * 使用 `synchronized` 对代码块进行细粒度锁定。
     * 分别对 {@code countA} 和 {@code countB} 使用不同的锁对象进行保护，以减少锁的竞争。
     */
    public void incrementCount() {
        synchronized (countALock) { // 锁定 {@code countALock}，确保对 {@code countA} 的访问是线程安全的。
            countA++;
        }
        synchronized (countBLock) { // 锁定 {@code countBLock}，确保对 {@code countB} 的访问是线程安全的。
            countB++;
        }
    }

    /**
     * 执行指定的消费逻辑，并在多线程环境下对其进行测试。
     *
     * @param consumer 消费逻辑，通常是对某个计数器执行递增操作。
     * @return 返回 {@code countA} 和 {@code countB} 的最终值，格式为 "countA-countB"。
     */
    public String execute(Consumer<Void> consumer) {
        Thread t1 = new Thread(() -> IntStream.range(0, 1000).forEach(i -> consumer.accept(null))); // 创建线程 t1，执行 1000 次消费逻辑。
        Thread t2 = new Thread(() -> IntStream.range(0, 1000).forEach(i -> consumer.accept(null))); // 创建线程 t2，执行 1000 次消费逻辑。

        t1.start(); // 启动线程 t1。
        t2.start(); // 启动线程 t2。

        try {
            t1.join(); // 等待线程 t1 执行完成。
            t2.join(); // 等待线程 t2 执行完成。
        } catch (InterruptedException e) {
            e.printStackTrace(); // 如果线程被中断，打印异常堆栈信息。
        }

        return countA + "-" + countB; // 返回计数器的结果，格式为 "countA-countB"。
    }

    /**
     * 测试主方法，用于对比两种同步方式的效果。
     * 输出结果可以帮助观察同步机制对程序执行的影响。
     *
     * @param args 命令行参数（未使用）。
     */
    public static void main(String[] args) {
        SynchronizedBlockTest test = new SynchronizedBlockTest();
        // 使用 [incrementCount] 方法，在多线程环境下测试其效果。
        String result = test.execute(i -> test.incrementCount());
        log.info("synchronized修饰代码段，result: {}", result); // 打印结果，用于观察同步代码块的效果。

        SynchronizedBlockTest test2 = new SynchronizedBlockTest();
        // 使用 [incrementCount1] 方法，在多线程环境下测试其效果。
        String result2 = test2.execute(i -> test2.incrementCount1());
        log.info("synchronized修饰整个方法，result: {}", result2); // 打印结果，用于观察同步方法的效果。
    }
}
