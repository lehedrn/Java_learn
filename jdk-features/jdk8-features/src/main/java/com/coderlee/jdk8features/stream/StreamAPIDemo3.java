package com.coderlee.jdk8features.stream;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Slf4j
public class StreamAPIDemo3 {
    // 1. reduce 的三种重载方式的实际应用场景
    @Test
    public void testReduceSum() {
        log.info("1. 使用 reduce() 实现累加操作");
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        Optional<Integer> sum = numbers.stream().reduce(Integer::sum);
        sum.ifPresent(result -> log.info("Sum: {}", result));  // Output: Sum: 15
    }

    @Test
    public void testReduceMax() {
        log.info("\n2. 使用 reduce() 实现最大值求解");
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        Optional<Integer> max = numbers.stream().reduce(Integer::max);
        max.ifPresent(result -> log.info("Max: {}", result));  // Output: Max: 5
    }

    @Test
    public void testReduceStatistics() {
        log.info("\n3. 使用 reduce() 实现统计（例如计算总和、最小值、最大值）");
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        // 求和
        Integer sum = numbers.stream().reduce(0, Integer::sum);
        // 求最小值
        Integer min = numbers.stream().reduce(Integer.MAX_VALUE, Integer::min);
        // 求最大值
        Integer max = numbers.stream().reduce(Integer.MIN_VALUE, Integer::max);

        log.info("Sum: {}, Min: {}, Max: {}", sum, min, max);  // Output: Sum: 15, Min: 1, Max: 5
    }

    // 2. 更详细的 Collector 设计与自定义收集器
    @Test
    public void testCustomCollector() {
        log.info("\n4. 使用自定义 Collector 进行累加");
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        Collector<Integer, AtomicInteger, Integer> sumCollector = Collector.of(
                AtomicInteger::new,       // supplier
                AtomicInteger::addAndGet, // accumulator
                (left, right) -> {        // combiner
                    left.addAndGet(right.get());
                    return left;
                },
                AtomicInteger::get        // finisher
        );
        Integer totalSum = numbers.stream().collect(sumCollector);
        log.info("Total Sum (using custom collector): {}", totalSum);  // Output: Total Sum: 15
    }

    @Test
    public void testCustomCollectorWithFilter() {
        log.info("\n5. 使用自定义 Collector 进行带过滤条件的批量求和");
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);
        Collector<Integer, AtomicInteger, Integer> filteredSumCollector = Collector.of(
                AtomicInteger::new,
                (atomicInt, value) -> { if (value % 2 == 0) atomicInt.addAndGet(value); },
                (left, right) -> { left.addAndGet(right.get()); return left; },
                AtomicInteger::get
        );
        Integer filteredSum = numbers.stream().collect(filteredSumCollector);
        log.info("Filtered Sum (only even numbers): {}", filteredSum);  // Output: Filtered Sum: 12
    }

    // 3. Collectors 提供的高级功能
    @Test
    public void testCollectorsAdvanced() {
        log.info("\n6. 使用 Collectors 提供的高级功能");

        List<String> words = Arrays.asList("apple", "banana", "kiwi", "orange");

        // 6.1. groupingBy
        Map<Integer, List<String>> groupedByLength = words.stream()
                .collect(Collectors.groupingBy(String::length));
        log.info("Grouped by Length: {}", groupedByLength);  // Output: {5=[apple], 6=[banana, orange], 4=[kiwi]}

        // 6.2. partitioningBy
        Map<Boolean, List<String>> partitionedByLength = words.stream()
                .collect(Collectors.partitioningBy(word -> word.length() > 5));
        log.info("Partitioned by Length > 5: {}", partitionedByLength);

        // 6.3. counting
        long count = words.stream().collect(Collectors.counting());
        log.info("Count of words: {}", count);  // Output: Count of words: 4

        // 6.4. summingInt
        int totalLength = words.stream().collect(Collectors.summingInt(String::length));
        log.info("Total length of words: {}", totalLength);  // Output: Total length of words: 23

        // 6.5. joining
        String joinedWords = words.stream().collect(Collectors.joining(", "));
        log.info("Joined words: {}", joinedWords);  // Output: apple, banana, kiwi, orange
    }

    // 4. 性能优化与并行流
    @Test
    public void testParallelStreamOptimization() {
        log.info("\n7. 性能优化：并行流中的收集");

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // 性能差异：顺序流 vs 并行流
        long startTime = System.nanoTime();
        Map<Integer, List<Integer>> groupedByModulo = numbers.stream()
                .collect(Collectors.groupingBy(n -> n % 2));
        long endTime = System.nanoTime();
        log.info("Sequential Stream Time: {} ns", endTime - startTime);

        startTime = System.nanoTime();
        Map<Integer, List<Integer>> parallelGroupedByModulo = numbers.parallelStream()
                .collect(Collectors.groupingBy(n -> n % 2));
        endTime = System.nanoTime();
        log.info("Parallel Stream Time: {} ns", endTime - startTime);

        // 对于这种简单的分组操作，通常并行流的性能提升有限，但在大数据集时效果明显。
    }

    @Test
    public void testPerformanceOptimization() {
        log.info("\n8. 使用 Collectors.toMap() 进行性能优化");

        // 模拟大数据量
        List<Integer> largeNumbers = new ArrayList<>();
        for (int i = 1; i <= 1_000_000; i++) {
            largeNumbers.add(i);
        }

        // 使用并行流和 toMap 收集器来提升性能
        long startTime = System.nanoTime();
        Map<Integer, Integer> numberToSquare = largeNumbers.parallelStream()
                .collect(Collectors.toMap(Function.identity(), n -> n * n));
        long endTime = System.nanoTime();
        log.info("Parallel toMap Time: {} ns", endTime - startTime);
    }

    // 5. 实际案例分析
    @Test
    public void testCaseGroupBy() {
        log.info("\n9. 实际案例：使用 collect() 进行复杂的多级分组操作");

        List<Order> orders = Arrays.asList(
                new Order(1, "Electronics", 500),
                new Order(2, "Clothing", 200),
                new Order(3, "Electronics", 300),
                new Order(4, "Clothing", 150)
        );

        Map<String, Map<String, List<Order>>> ordersGrouped = orders.stream()
                .collect(Collectors.groupingBy(Order::getCategory,
                        Collectors.groupingBy(order -> order.getPrice() > 200 ? "Expensive" : "Cheap")));

        log.info("Grouped Orders: {}", ordersGrouped);
    }

    @Test
    public void testReduceComplexAggregation() {
        log.info("\n10. 使用 reduce() 进行复杂的聚合");

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

        // 聚合：先求和，再乘以一个常数
        Integer result = numbers.stream()
                .reduce(0, (total, num) -> total + num) * 2; // 2 * (1+2+3+4+5)
        log.info("Aggregated Result (sum * 2): {}", result);  // Output: Aggregated Result: 30
    }

    // 6. 总结与最佳实践
    @Test
    public void testBestPractices() {
        log.info("\n11. 总结与最佳实践");

        // 总结：简单累加时使用 reduce，复杂的收集操作或修改状态时使用 collect
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        Integer sum = numbers.stream().reduce(0, Integer::sum);
        log.info("Best Practice: Simple Sum: {}", sum);  // Output: Simple Sum: 15

        // 在并行流中，使用无状态的 Collector 来避免并发问题
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David");
        Set<String> uniqueNames = names.parallelStream().collect(Collectors.toSet());
        log.info("Best Practice: Parallel Stream with Set: {}", uniqueNames);  // Output: Set: [Alice, Bob, Charlie, David]
    }

    // Order 类（模拟实际的业务场景）
    static class Order {
        private final int id;
        private final String category;
        private final int price;

        public Order(int id, String category, int price) {
            this.id = id;
            this.category = category;
            this.price = price;
        }

        public int getId() {
            return id;
        }

        public String getCategory() {
            return category;
        }

        public int getPrice() {
            return price;
        }

        @Override
        public String toString() {
            return "Order{id=" + id + ", category='" + category + "', price=" + price + '}';
        }
    }
}