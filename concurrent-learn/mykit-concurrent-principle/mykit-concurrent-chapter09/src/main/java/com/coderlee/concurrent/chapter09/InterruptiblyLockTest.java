package com.coderlee.concurrent.chapter09;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import lombok.extern.slf4j.Slf4j;

/**
 * InterruptiblyLockTest 类用于演示如何使用 `ReentrantLock` 的 `lockInterruptibly` 方法处理线程抢占锁时的中断行为。
 * <p>
 * 该类通过两个线程（threadA 和 threadB）模拟抢占共享资源的场景，并测试当线程在等待锁的过程中被中断时的行为。
 * 主要功能包括：
 * <ul>
 *   <li>启动两个线程尝试抢占锁。</li>
 *   <li>主线程在延迟一段时间后中断这两个线程，观察它们的行为。</li>
 *   <li>展示了 `lockInterruptibly` 在线程被中断时抛出 `InterruptedException` 的特性。</li>
 * </ul>
 */
@Slf4j
public class InterruptiblyLockTest {

    /**
     * 程序入口点，用于启动和测试线程抢占锁的行为。
     * <p>
     * 测试流程如下：
     * <ol>
     *   <li>创建两个线程 threadA 和 threadB，分别调用 {@link #lockAndUnlock()} 方法。</li>
     *   <li>主线程短暂休眠，确保子线程进入锁等待状态。</li>
     *   <li>主线程中断 threadA 和 threadB，触发 `lockInterruptibly` 的中断逻辑。</li>
     *   <li>主线程再次休眠，观察线程处理中断后的表现。</li>
     * </ol>
     *
     * @param args 命令行参数（未使用）
     */
    public static void main(String[] args) {
        InterruptiblyLockTest test = new InterruptiblyLockTest();
        // 创建两个线程，分别命名为 threadA 和 threadB
        Thread threadA = new Thread(() -> test.lockAndUnlock(), "threadA");
        Thread threadB = new Thread(() -> test.lockAndUnlock(), "threadB");
        threadA.start(); // 启动线程A
        threadB.start(); // 启动线程B

        try {
            // 主线程休眠 100 毫秒，确保子线程进入锁等待状态
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            log.error("主线程休眠时被中断: {}", e.getMessage());
        }

        // 中断 threadA 和 threadB
        threadA.interrupt();
        threadB.interrupt();

        try {
            // 主线程再次休眠 2 秒，观察线程处理中断后的表现
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            log.error("主线程休眠时被中断: {}", e.getMessage());
        }
    }

    private Lock lock = new ReentrantLock(); // 定义一个可重入锁，用于保护共享资源

    /**
     * 尝试以可中断的方式抢占锁，并在完成后释放锁。
     * <p>
     * 具体逻辑如下：
     * <ol>
     *   <li>调用 `lockInterruptibly` 方法抢占锁。如果线程在等待锁时被中断，则抛出 `InterruptedException`。</li>
     *   <li>成功抢占锁后，记录日志并检查当前线程是否已被中断。</li>
     *   <li>模拟业务逻辑处理（休眠 1 秒）。</li>
     *   <li>无论是否发生异常，最终都会释放锁。</li>
     * </ol>
     */
    public void lockAndUnlock() {
        try {
            // 使用 lockInterruptibly 方法抢占锁，支持线程中断
            lock.lockInterruptibly();
            log.info("{} 抢占锁成功", Thread.currentThread().getName());

            // 检查当前线程是否已被中断
            if (Thread.currentThread().isInterrupted()) {
                log.info("{} 被中断", Thread.currentThread().getName());
            }

            // 模拟业务逻辑处理
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            // 捕获中断异常，记录日志
            log.error("{} 抢占锁被中断", Thread.currentThread().getName());
        } finally {
            // 确保锁在任何情况下都能被释放
            lock.unlock();
        }
    }
}
