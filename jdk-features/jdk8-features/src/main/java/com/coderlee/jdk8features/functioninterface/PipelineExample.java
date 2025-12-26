package com.coderlee.jdk8features.functioninterface;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 函数式接口管道操作示例类
 * 演示如何使用Function、Predicate和Consumer接口构建数据处理管道
 * 通过Stream API实现链式数据处理操作
 *
 * @author coderlee
 */
@Slf4j
public class PipelineExample {

    /**
     * 数据处理管道示例
     * 展示如何将Function、Predicate和Consumer组合成处理管道
     * 1. 使用Function进行数据转换（trim操作）
     * 2. 使用Predicate进行数据过滤（长度大于3的单词）
     * 3. 使用Consumer进行数据输出（打印日志）
     */
    @Test
    public void test() {
        // 初始化字符串列表
        List<String> words = Arrays.asList(" apple ", "banana", " cat ", "dog");

        // 定义Function：去除字符串两端空白字符
        Function<String, String> trim = String::trim;
        // 定义Predicate：筛选长度大于3的字符串
        Predicate<String> longWord = word -> word.length() > 3;
        // 定义Consumer：将处理结果输出到日志
        Consumer<String> printer = x -> log.info("word: {}, length: {}", x, x.length());

        // 构建数据处理管道
        words.stream()
                // 应用Function：对每个元素执行trim操作
                .map(trim)
                // 应用Predicate：过滤长度大于3的单词
                .filter(longWord)
                // 应用Consumer：对过滤后的结果执行打印操作
                .forEach(printer);
    }
}
