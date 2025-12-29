package com.coderlee.jdk8features.stream;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
public class StreamAPIDemo2 {
    private final List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);

    /**
     * 1️⃣ filter 操作：短路、顺序与惰性执行
     */
    @Test
    public void testFilter() {
        log.info("\n=== 测试 filter 操作 ===");

        list.stream()
                .filter(i -> {
                    log.info("filter: " + i);
                    return i % 2 == 0;
                })
                .forEach(i -> log.info("result: " + i));

        log.info("filter 测试结束");
    }

    /**
     * 2️⃣ filter 与短路操作：findFirst 触发短路
     */
    @Test
    public void testFilterWithShortCircuit() {
        log.info("=== 测试 filter 与短路操作 ===");

        Optional<Integer> firstEven = list.stream()
                .filter(i -> {
                    log.info("filter: " + i);
                    return i % 2 == 0;
                })
                .findFirst();

        firstEven.ifPresent(i -> log.info("First even number: " + i));

        log.info("filter 短路测试结束");
    }

    /**
     * 3️⃣ map 操作：一对一映射
     */
    @Test
    public void testMap() {
        log.info("=== 测试 map 操作 ===");

        list.stream()
                .map(i -> {
                    log.info("map: " + i);
                    return i * 10;
                })
                .forEach(i -> log.info("result: " + i));

        log.info("map 测试结束");
    }

    /**
     * 4️⃣ flatMap 操作：一对多展开
     */
    @Test
    public void testFlatMap() {
        log.info("=== 测试 flatMap 操作 ===");

        List<List<Integer>> nestedList = Arrays.asList(
                Arrays.asList(1, 2),
                Arrays.asList(3, 4)
        );

        nestedList.stream()
                .flatMap(innerList -> innerList.stream())
                .forEach(i -> log.info("flatMap result: " + i));

        log.info("flatMap 测试结束");
    }

    /**
     * 5️⃣ distinct 操作：去重操作的状态
     */
    @Test
    public void testDistinct() {
        log.info("=== 测试 distinct 去重操作 ===");

        List<Integer> duplicates = Arrays.asList(1, 2, 2, 3, 3, 4);

        duplicates.stream()
                .distinct()
                .forEach(i -> log.info("distinct result: " + i));

        log.info("distinct 测试结束");
    }

    /**
     * 6️⃣ sorted 操作：排序会打乱流动性
     */
    @Test
    public void testSorted() {
        log.info("=== 测试 sorted 排序操作 ===");

        list.stream()
                .sorted(Comparator.reverseOrder())
                .forEach(i -> log.info("sorted result: " + i));

        log.info("sorted 测试结束");
    }

    /**
     * 7️⃣ limit / skip 操作：顺序影响
     */
    @Test
    public void testLimitSkip() {
        log.info("=== 测试 limit 与 skip 操作 ===");

        // limit 放在前面
        list.stream()
                .limit(3)
                .filter(i -> {
                    log.info("filter with limit: " + i);
                    return i % 2 == 0;
                })
                .forEach(i -> log.info("limit + filter result: " + i));

        // skip 放在前面
        list.stream()
                .skip(2)
                .filter(i -> {
                    log.info("filter with skip: " + i);
                    return i % 2 == 0;
                })
                .forEach(i -> log.info("skip + filter result: " + i));

        log.info("limit / skip 测试结束");
    }

    /**
     * 8️⃣ map 与 flatMap 区别：演示区别
     */
    @Test
    public void testMapVsFlatMap() {
        log.info("=== 测试 map 与 flatMap 区别 ===");

        // map 示例：每个字符串映射成一个数组，结果是 Stream<String[]>
        Arrays.asList("hello", "world").stream()
                .map(s -> s.split(""))
                .forEach(arr -> log.info("map result: " + Arrays.toString(arr)));

        // flatMap 示例：把每个字符串拆开为字符流，结果是 Stream<String>
        Arrays.asList("hello", "world").stream()
                .flatMap(s -> Arrays.stream(s.split("")))
                .forEach(s -> log.info("flatMap result: " + s));

        log.info("map vs flatMap 测试结束");
    }
}
