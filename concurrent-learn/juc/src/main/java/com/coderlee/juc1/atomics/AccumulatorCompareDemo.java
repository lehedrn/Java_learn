package com.coderlee.juc1.atomics;

import com.coderlee.juc1.utils.CostTimeUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 并发累加器性能对比演示类
 * 比较synchronized、AtomicInteger、LongAdder和LongAccumulator在高并发场景下的性能差异
 */
@Slf4j
public class AccumulatorCompareDemo {
    // 定义常量：1万次操作
    private static final int _1W = 10000;
    // 定义线程数量
    private static final int THREAD_NUMBER = 50;

    /**
     * 程序入口点
     * 分别测试四种不同的线程安全计数方式，并记录每种方式的执行时间
     */
    public static void main(String[] args) {
        ClickNumber clickNumber = new ClickNumber();

        // 使用同步方法进行计数
        CostTimeUtils.calcCostTime(x -> runClick(y -> clickNumber.clickBySynchronized(), clickNumber::getNumber, "click by synchronized"), "click by synchronized");

        // 使用AtomicInteger进行计数
        CostTimeUtils.calcCostTime(x -> runClick(y -> clickNumber.clickByAtomicInteger(), clickNumber::getAtomicInteger, "click by atomicInteger"), "click by atomicInteger");

        // 使用LongAdder进行计数
        CostTimeUtils.calcCostTime(x -> runClick(y -> clickNumber.clickByLongAdder(), clickNumber::getLongAdder, "click by longadder"), "click by longadder");

        // 使用LongAccumulator进行计数
        CostTimeUtils.calcCostTime(x -> runClick(y -> clickNumber.clickByLongAccumulator(), clickNumber::getLongAccumulator, "click by longaccumulator"), "click by longaccumulator");
    }

    /**
     * 多线程运行点击操作的方法
     * @param opt 执行的操作（消费型函数）
     * @param result 获取结果的方法（供应型函数）
     * @param msg 描述信息
     */
    public static void runClick(Consumer<Void> opt, Supplier<Long> result, String msg) {
        // 创建CountDownLatch用于等待所有线程完成
        CountDownLatch countDownLatch = new CountDownLatch(THREAD_NUMBER);

        // 启动指定数量的线程
        for (int i = 1; i <= THREAD_NUMBER; i++) {
            new Thread(() -> {
                try {
                    // 每个线程执行100万次操作
                    for (int j = 1; j <= 100 * _1W; j++) {
                        opt.accept(null);
                    }
                } finally {
                    // 每个线程完成后减少计数器
                    countDownLatch.countDown();
                }
            }, String.valueOf(i)).start();
        }

        try {
            // 等待所有线程完成
            countDownLatch.await();
        } catch (InterruptedException e) {
            log.info("countDownLatch interrupted", e);
        }

        // 输出最终结果
        log.info("{} result: {}", msg, result.get());
    }
}

/**
 * 点击计数器类
 * 提供四种不同的线程安全计数实现方式
 */
class ClickNumber {
    // 基本整型变量，配合synchronized使用
    private int number;

    /**
     * 使用synchronized关键字保证线程安全的计数方法
     */
    public synchronized void clickBySynchronized() {
        number++;
    }

    /**
     * 获取number值
     * @return 当前number值
     */
    public long getNumber() {
        return this.number;
    }

    // 使用AtomicInteger实现原子操作
    private final AtomicInteger atomicInteger = new AtomicInteger(0);

    /**
     * 使用AtomicInteger进行原子递增操作
     */
    public void clickByAtomicInteger() {
        atomicInteger.getAndIncrement();
    }

    /**
     * 获取AtomicInteger当前值
     * @return AtomicInteger当前值
     */
    public long getAtomicInteger() {
        return atomicInteger.get();
    }

    // 使用LongAdder实现高性能计数
    private final LongAdder longAdder = new LongAdder();

    /**
     * 使用LongAdder进行递增操作
     */
    public void clickByLongAdder() {
        longAdder.increment();
    }

    /**
     * 获取LongAdder当前总和
     * @return LongAdder累计值
     */
    public long getLongAdder() {
        return longAdder.sum();
    }

    // 使用LongAccumulator实现累积操作
    private final LongAccumulator longAccumulator = new LongAccumulator((x, y)->x+y,0);

    /**
     * 使用LongAccumulator进行累积操作（每次加1）
     */
    public void clickByLongAccumulator() {
        longAccumulator.accumulate(1);
    }

    /**
     * 获取LongAccumulator当前值
     * @return LongAccumulator当前值
     */
    public long getLongAccumulator() {
        return longAccumulator.get();
    }
}
