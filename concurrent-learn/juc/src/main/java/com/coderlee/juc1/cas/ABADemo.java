package com.coderlee.juc1.cas;

import com.coderlee.juc1.utils.SleepUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * ABA问题演示与解决方案示例类
 *
 * ABA问题：线程1准备用CAS将变量从A改为B，在此之前线程2将变量从A改为C，
 * 然后又改回A，此时线程1仍认为变量没有变化，CAS操作成功，但实际上中间发生了变化。
 */
@Slf4j
public class ABADemo {

    // 使用AtomicInteger演示ABA问题 - 无法检测到中间的变化
    static AtomicInteger atomicInteger = new AtomicInteger(100);

    // 使用AtomicStampedReference解决ABA问题 - 通过版本戳来检测变化
    static AtomicStampedReference<Integer> stampedReference = new AtomicStampedReference<>(100, 1);

    public static void main(String[] args) {
        // 演示ABA问题
        abaProblem();

        // 展示如何解决ABA问题
        resolveABA();
    }

    /**
     * 使用AtomicStampedReference解决ABA问题
     * 通过引入版本戳(时间戳)机制来检测变量是否真正未发生变化
     */
    public static void resolveABA() {
        new Thread(() -> {
            log.info("{} 1st stamp: {}", Thread.currentThread().getName(),  stampedReference.getStamp());
            SleepUtils.sleep(500);

            // 将值从100改为101，版本戳+1
            stampedReference.compareAndSet(100, 101,  stampedReference.getStamp(),  stampedReference.getStamp() + 1);
            log.info("{} 2nd stamp: {}", Thread.currentThread().getName(), stampedReference.getStamp());

            // 再将值从101改回100，版本戳再次+1
            stampedReference.compareAndSet(101, 100,  stampedReference.getStamp(),  stampedReference.getStamp() + 1);
            log.info("{} 3rd stamp: {}", Thread.currentThread().getName(), stampedReference.getStamp());
        }, "A").start();

        new Thread(() -> {
            log.info("{} 1st stamp: {}", Thread.currentThread().getName(), stampedReference.getStamp());
            SleepUtils.sleep(1000);

            // 尝试将值从100改为2022，但因为版本戳已变化，所以操作会失败
            boolean b = stampedReference.compareAndSet(100, 2022, stampedReference.getStamp(), stampedReference.getStamp() + 1);
            log.info("{} 2nd stamp: {}, opt is success? : {}", Thread.currentThread().getName(), stampedReference.getStamp(), b);
        }, "B").start();
    }

    /**
     * 演示ABA问题的存在
     * 使用普通的AtomicInteger无法感知中间状态的变化
     */
    public static void abaProblem() {
        new Thread(() -> {
            // 线程A先将值从100改为101
            atomicInteger.compareAndSet(100, 101);
            SleepUtils.sleep(10);

            // 然后再改回100，完成一次ABA过程
            atomicInteger.compareAndSet(101, 100);
        }, "A").start();

        new Thread(() -> {
            SleepUtils.sleep(200);

            // 线程B尝试将值从100改为2022，虽然实际值确实是100，但中间已经发生了变化
            // CAS操作仍然成功，这就是ABA问题
            log.info("opt [100 -> 2022], opt is success? : {}, result is : {}",
                    atomicInteger.compareAndSet(100, 2022), atomicInteger.get());
        }, "B").start();
    }
}
