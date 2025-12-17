package com.coderlee.juc1.atomics;

import com.coderlee.juc1.utils.CostTimeUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * 列表求和性能测试演示类
 * 对比多种并发求和技术在大数据量下的性能差异
 */
@Slf4j
public class ListSumDemo {

    /**
     * 列表大小常量: 3亿
     */
    private static final int LIST_SIZE = 30 * 10000000;

    /**
     * 存储待计算数据的列表
     */
    private static final List<Integer> LIST = new ArrayList<>(LIST_SIZE);


    /**
     * 初始化块，在对象创建时填充随机数据到列表中
     */
    static {
        log.info("start to create list data");
        Random random = new Random();
        for (int i = 0; i < LIST_SIZE; i++) {
            // 填充0到LIST_SIZE之间的随机整数
            LIST.add(random.nextInt(LIST_SIZE));
        }
        log.info("list data is created");
    }

    /**
     * 主方法，启动各种求和算法的性能测试
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // JVM 预热，避免首次 parallelStream 影响测试
        warmUp();

        // 方案一：AtomicLong（高竞争，反例）
        CostTimeUtils.calcCostTime(x -> {
            AtomicLong atomicLong = new AtomicLong(0);
            LIST.parallelStream().forEach(atomicLong::addAndGet);
            log.info("AtomicLong result = {}", atomicLong.get());
        }, "AtomicLong + parallelStream");

        // 方案二：LongAdder（分段累加）
        CostTimeUtils.calcCostTime(x -> {
            LongAdder longAdder = new LongAdder();
            LIST.parallelStream().forEach(longAdder::add);
            log.info("LongAdder result = {}", longAdder.sum());
        }, "LongAdder + parallelStream");

        // 方案三：parallelStream 内置归约（推荐）
        CostTimeUtils.calcCostTime(x -> {
            long sum = LIST.parallelStream()
                    .mapToLong(Integer::longValue)
                    .sum();
            log.info("parallelStream sum = {}", sum);
        }, "parallelStream.mapToLong().sum()");
    }

    private static void warmUp() {
        log.info("warm up...");
        for (int i = 0; i < 3; i++) {
            LIST.parallelStream().mapToInt(Integer::intValue).sum();
        }
        log.info("warm up finished");
    }
}

