package com.coderlee.concurrent.chapter07;

import java.util.function.Consumer;
import java.util.stream.IntStream;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>该类旨在演示在多线程环境下，同步方法和非同步方法对共享变量操作的不同效果。</p>
 * <p>
 * 通过两个线程分别调用同步方法和非同步方法对同一个计数器进行累加操作，
 * 展示了使用synchronized关键字修饰的方法在并发环境下的线程安全性。
 * </p>
 */
@Slf4j
public class SynchronizedTest {

    // 共享计数器变量
    private Long count = 0L;

    /**
     * <p>同步方法，用于对共享变量count进行安全的累加操作。</p>
     * <p>通过synchronized关键字确保同一时刻只有一个线程可以执行该方法，
     * 避免了多线程并发修改共享变量导致的数据不一致问题。</p>
     */
    public synchronized void increamentCount() {
        count++; // 累加操作
    }

    /**
     * <p>非同步方法，用于对共享变量count进行累加操作。</p>
     * <p>由于没有使用任何同步机制，在多线程环境下可能会出现数据竞争和不一致的问题。</p>
     */
    public void increamentCountWithoutSynchronized() {
        count++; // 累加操作，可能存在线程安全问题
    }

    /**
     * <p>通用执行方法，接收一个消费者函数式接口参数。</p>
     * <p>启动两个线程，每个线程执行1000次消费者指定的操作，并等待两个线程执行完毕后返回最终的计数值。</p>
     *
     * @param consumer 指定线程中要执行的操作，接收一个Void类型的参数（实际未使用）
     * @return 最终的计数值
     */
    public Long execute(Consumer<Void> consumer) {
        // 创建第一个线程，执行1000次消费操作
        Thread t1 = new Thread(() -> IntStream.range(0, 1000).forEach(i -> consumer.accept(null)));
        // 创建第二个线程，执行1000次消费操作
        Thread t2 = new Thread(() -> IntStream.range(0, 1000).forEach(i -> consumer.accept(null)));

        t1.start(); // 启动第一个线程
        t2.start(); // 启动第二个线程

        try {
            t1.join(); // 等待第一个线程执行完毕
            t2.join(); // 等待第二个线程执行完毕
        } catch (InterruptedException e) {
            e.printStackTrace(); // 异常处理
        }

        return count; // 返回最终的计数值
    }

    /**
     * <p>执行同步方法版本的计数累加操作。</p>
     * <p>调用通用execute方法，并传入同步累加方法作为消费者操作。</p>
     *
     * @return 执行同步方法后的最终计数值
     */
    public Long executeSynchronizedMethod() {
        return execute(i -> increamentCount());
    }

    /**
     * <p>执行非同步方法版本的计数累加操作。</p>
     * <p>调用通用execute方法，并传入非同步累加方法作为消费者操作。</p>
     *
     * @return 执行非同步方法后的最终计数值
     */
    public Long executeWithoutSynchronizedMethod() {
        return execute(i -> increamentCountWithoutSynchronized());
    }

    /**
     * <p>主方法，用于测试同步和非同步方法的效果。</p>
     * <p>分别创建两个SynchronizedTest实例，调用各自的同步和非同步执行方法，
     * 并打印最终的计数值以展示结果差异。</p>
     *
     * @param args 命令行参数（未使用）
     */
    public static void main(String[] args) {
        SynchronizedTest st1 = new SynchronizedTest();
        Long count1 = st1.executeSynchronizedMethod();
        log.info("execute with synchronized ,final count is :{}", count1);

        SynchronizedTest st2 = new SynchronizedTest();
        Long count2 = st2.executeWithoutSynchronizedMethod();
        log.info("execute without synchronized ,final count is :{}", count2);
    }
}