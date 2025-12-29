package com.coderlee.jdk8features.stream;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
public class StreamAPIDemo1 {
    @Test
    public void testImperativeVsDeclarative() {
        System.out.println("\n=== 1. 命令式 vs 声明式 ===");

        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);

        // 命令式写法
        int imperativeSum = 0;
        for (Integer i : list) {
            if (i % 2 == 0) {
                imperativeSum += i;
            }
        }
        log.info("Imperative sum = {}", imperativeSum);

        // 声明式写法（Stream）
        int declarativeSum = list.stream()
                .filter(i -> i % 2 == 0)
                .mapToInt(Integer::intValue)
                .sum();

        log.info("Declarative sum = {}", declarativeSum);
    }

    @Test
    public void testStreamIsNotCollection() {
        log.info("=== 2. Stream ≠ Collection ===");

        List<Integer> list = Arrays.asList(1, 2, 3);

        Stream<Integer> stream = list.stream();
        log.info("Stream created, but no computation happens.");

        // 只有终止操作才会触发执行
        stream.forEach(i -> log.info("consume: {}", i));
    }

    @Test
    public void demoStreamIsOneShot() {
        log.info("=== 3. Stream 一次性特性 ===");

        Stream<Integer> stream = Stream.of(1, 2, 3);

        stream.forEach(i -> log.info("first consume: {}", i));

        try {
            // 再次使用同一个 Stream
            stream.forEach(i -> log.info("second consume: {}", i));
        } catch (IllegalStateException e) {
            log.error("tream cannot be reused after terminal operation.", e);
        }
    }

    @Test
    public void testLazyEvaluation() {
        log.info("=== 4. 惰性执行（Lazy Evaluation） ===");

        List<Integer> list = Arrays.asList(1, 2, 3, 4);

        Stream<Integer> stream = list.stream()
                .filter(i -> {
                    log.info("filter: {}", i);
                    return i % 2 == 0;
                })
                .map(i -> {
                    log.info("map: {}", i);
                    return i * 10;
                });

        log.info("Before terminal operation");

        // 终止操作触发执行
        stream.forEach(i -> log.info("result: {}", i));
    }

    @Test
    public void testPipelineModel() {
        log.info("=== 5. 流水线模型（Pipeline） ===");

        List<Integer> list = Arrays.asList(1, 2, 3, 4);

        list.stream()
                .filter(i -> {
                    log.info("filter: {}", i);
                    return i % 2 == 0;
                })
                .map(i -> {
                    log.info("map: {}", i);
                    return i * 2;
                })
                .forEach(i ->log.info("forEach -> {}", i));

        log.info("每个元素都是 filter → map → forEach 一次走完");
    }
}
