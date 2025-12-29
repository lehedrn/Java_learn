package com.coderlee.jdk8features.stream;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

@Slf4j
public class StreamAPIDemo4 {

    /**
     * 1. 基本的并行流操作示例
     * 展示如何使用 parallelStream() 执行计算任务。
     */
    @Test
    public void testBasicParallelStream() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

        // 使用并行流计算总和
        int sum = numbers.parallelStream()
                .mapToInt(Integer::intValue)
                .sum();

        log.info("Sum of numbers: {}", sum);
        assertEquals(15, sum);  // 1 + 2 + 3 + 4 + 5 = 15
    }

    /**
     * 2. 并行流中的线程同步问题
     * 演示并行流中由于共享状态引起的问题。
     */
    @Test
    public void testThreadSafetyInParallelStream() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        final AtomicInteger sum = new AtomicInteger(0);  // 使用 AtomicInteger 来避免并发问题

        // 错误示例：直接对共享的变量进行操作
        numbers.parallelStream()
                .forEach(n -> sum.addAndGet(n));  // 正确：使用线程安全的 AtomicInteger

        log.info("Sum using thread-safe approach: {}", sum.get());
        assertEquals(15, sum.get());  // 1 + 2 + 3 + 4 + 5 = 15
    }

    /**
     * 3. 在 IO 密集型任务中不适用并行流
     * 演示在 IO 操作中使用并行流可能带来的性能下降。
     */
    @Test
    public void testIOOperationsWithParallelStream() {
        List<String> fileNames = Arrays.asList("file1.txt", "file2.txt", "file3.txt");

        // 假设 readFile() 为耗时的 IO 操作
        long startTime = System.nanoTime();

        fileNames.parallelStream()
                .map(this::readFile)  // 模拟 IO 操作
                .forEach(this::processFile);  // 处理文件

        long duration = System.nanoTime() - startTime;
        log.info("Time taken for IO operations with parallelStream: {} ns", duration);

        // 假设每个文件的处理时间都一样，通常并行流在 IO 密集型任务中效率低下
    }

    // 模拟的 IO 操作
    private String readFile(String fileName) {
        try {
            Thread.sleep(500);  // 模拟文件读取的延迟
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return fileName;
    }

    // 模拟文件处理
    private void processFile(String fileName) {
        log.info("Processing file: {}", fileName);
    }

    /**
     * 4. 并行流中的性能对比
     * 比较并行流和串行流的性能，展示并行流的适用场景。
     */
    @Test
    public void testParallelStreamPerformance() {
        List<Long> largeList = Arrays.asList(new Long[99999999]);
        largeList.replaceAll(i -> 1L);  // Fill the list with 1s

        // 串行流性能
        long serialStart = System.nanoTime();
        long serialSum = largeList.stream().mapToLong(Long::longValue).sum();
        long serialDuration = System.nanoTime() - serialStart;
        log.info("Serial sum: {} in {} ns", serialSum, serialDuration);

        // 并行流性能
        long parallelStart = System.nanoTime();
        long parallelSum = largeList.parallelStream()
                .mapToLong(Long::longValue).sum();
        long parallelDuration = System.nanoTime() - parallelStart;
        log.info("Parallel sum: {} in {} ns", parallelSum, parallelDuration);

        // 比较串行流与并行流的性能
        log.info("Parallel stream is {} faster than serial stream",
                (serialDuration / parallelDuration));
    }

    /**
     * 5. 误区：并行流不适用于所有任务
     * 演示并行流在一些场景下带来的性能问题，如对小数据集进行操作。
     */
    @Test
    public void testParallelStreamOnSmallDataSet() {
        List<Integer> smallList = Arrays.asList(1, 2, 3, 4, 5);

        long serialStart = System.nanoTime();
        int serialSum = smallList.stream().mapToInt(Integer::intValue).sum();
        long serialDuration = System.nanoTime() - serialStart;
        log.info("Serial sum for small list: {} in {} ns", serialSum, serialDuration);

        long parallelStart = System.nanoTime();
        int parallelSum = smallList.parallelStream().mapToInt(Integer::intValue).sum();
        long parallelDuration = System.nanoTime() - parallelStart;
        log.info("Parallel sum for small list: {} in {} ns", parallelSum, parallelDuration);

        // 对小数据集使用并行流通常会导致性能降低
        assertEquals(serialSum, parallelSum);
    }

    /**
     * 6. 误区：并行流的执行顺序不可预知
     * 演示 `forEachOrdered` 如何保持执行顺序。
     */
    @Test
    public void testParallelStreamOrderedExecution() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

        // 错误示例：forEachOrdered 确保顺序，但是会影响性能
        numbers.parallelStream()
                .forEachOrdered(n -> log.info("Processing number: {}", n));  // 确保顺序

        // 需要保证顺序时使用 forEachOrdered()
        // 对于无序集合，forEachOrdered 的性能开销可能会很大
    }

    /**
     * 7. 使用 `forEach` 与 `forEachOrdered` 的区别
     * 演示使用 `forEach` 和 `forEachOrdered` 的区别。
     */
    @Test
    public void testForEachVsForEachOrdered() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

        // 使用 forEach 执行并行流（顺序可能不一致）
        log.info("Using forEach:");
        numbers.parallelStream()
                .forEach(n -> log.info("Processing number: {}", n));

        // 使用 forEachOrdered 执行并行流（保证顺序）
        log.info("Using forEachOrdered:");
        numbers.parallelStream()
                .forEachOrdered(n -> log.info("Processing number: {}", n));
    }

}
