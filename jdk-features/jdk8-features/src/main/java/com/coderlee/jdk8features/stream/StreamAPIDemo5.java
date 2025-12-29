package com.coderlee.jdk8features.stream;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Slf4j
public class StreamAPIDemo5 {
    @Test
    public void testStreamTypeSpecialization() {
        // 示例 1：使用 Stream<Integer> 进行装箱操作
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        long startTime = System.nanoTime();
        int sum = numbers.stream().mapToInt(Integer::intValue).sum();  // 使用 mapToInt 避免装箱
        long endTime = System.nanoTime();
        log.info("Sum using Stream<Integer>: " + sum + " (Time taken: " + (endTime - startTime) + " ns)");

        // 示例 2：使用 IntStream 来避免装箱
        IntStream intStream = numbers.stream().mapToInt(Integer::intValue);
        intStream.forEach(value -> log.info("Processed value: " + value));
    }

    @Test
    public void testBoxingAndUnboxing() {
        // 装箱和拆箱的示例
        int[] intArray = {1, 2, 3, 4, 5};

        // 自动拆箱
        long startTime = System.nanoTime();
        int sum = Arrays.stream(intArray).sum();
        long endTime = System.nanoTime();
        log.info("Sum using int array: " + sum + " (Time taken: " + (endTime - startTime) + " ns)");

        // 使用 Stream<Integer> 进行装箱
        List<Integer> boxedList = Arrays.asList(1, 2, 3, 4, 5);
        startTime = System.nanoTime();
        int boxedSum = boxedList.stream().mapToInt(Integer::intValue).sum();  // 装箱和拆箱
        endTime = System.nanoTime();
        log.info("Sum using Stream<Integer>: " + boxedSum + " (Time taken: " + (endTime - startTime) + " ns)");
    }

    @Test
    public void testMapToInt() {
        // 使用 mapToInt 方法将 Stream<String> 转换为 IntStream
        List<String> numbers = Arrays.asList("1", "2", "3", "4", "5");

        int sum = numbers.stream()
                .mapToInt(Integer::parseInt)  // 使用 mapToInt 避免装箱
                .sum();
        log.info("Sum using mapToInt: " + sum);
    }

    @Test
    public void testOptionalFilterAndOrElse() {
        // 使用 Optional 的 filter 和 orElse 方法
        Optional<String> name = Optional.of("John");

        // 使用 filter 进行条件判断
        name.filter(n -> n.length() > 3)
                .ifPresent(n -> log.info("Filtered name: " + n));  // 结果：John

        // 如果 Optional 为空，则返回默认值
        Optional<String> emptyName = Optional.empty();
        String defaultName = emptyName.orElse("Guest");
        log.info("Default name: " + defaultName);  // 输出 "Guest"
    }

    @Test
    public void testStreamAndOptionalCombined() {
        // 结合 Stream 和 Optional，处理可能为空的值
        List<Integer> numbers = Arrays.asList(10, 20, 30, 40, 50);

        Optional<Integer> firstLargeNumber = numbers.stream()
                .filter(n -> n > 25)  // 过滤大于 25 的数字
                .findFirst();  // 返回 Optional

        // 使用 Optional 的 ifPresent 进行处理
        firstLargeNumber.ifPresent(n -> log.info("First number greater than 25: " + n));
        if (!firstLargeNumber.isPresent()) {
            log.info("No number greater than 25 found");
        }
    }

    @Test
    public void testOptionalWithMap() {
        // 使用 map 对 Optional 内的值进行转换
        Optional<String> name = Optional.of("john");
        name.map(String::toUpperCase)
                .ifPresent(n -> log.info("Name in uppercase: " + n));  // 输出 "JOHN"
    }

    @Test
    public void testOptionalChaining() {
        // 使用 Optional 链式调用
        Optional<String> name = Optional.of("John");

        // 链式操作
        String result = name.filter(n -> n.length() > 3)
                .map(String::toUpperCase)
                .orElse("Unknown");
        log.info("Processed name: " + result);  // 输出 "JOHN"
    }
}
