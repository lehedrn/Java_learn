package com.coderlee.jdk8features.functioninterface;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.function.BinaryOperator;
import java.util.function.IntConsumer;
import java.util.function.UnaryOperator;

/**
 * JDK 8 函数式接口高级示例类
 * 演示特定类型的函数式接口使用方法，包括原始类型特化接口和操作符接口
 *
 * @author coderlee
 */
@Slf4j
public class AdvExample {

    /**
     * IntConsumer接口示例：专门处理int类型参数的消费接口
     * 避免了装箱拆箱操作，提高性能
     */
    @Test
    public void testIntConsumer() {
        // 定义一个IntConsumer：接收int参数并打印日志
        IntConsumer consumer = x -> log.info("intconsumer: {}", x);
        // 调用accept方法消费int值
        consumer.accept(2025);
    }

    /**
     * Operator接口示例：操作符接口，用于执行特定类型的操作
     * UnaryOperator：一元操作符，接收和返回相同类型的值
     * BinaryOperator：二元操作符，接收两个相同类型的参数，返回相同类型的结果
     */
    @Test
    public void testOperator() {
        // 定义一个UnaryOperator：计算整数的平方
        UnaryOperator<Integer> square = x -> x * x;
        // 定义一个BinaryOperator：计算两个整数的和
        BinaryOperator<Integer> sum = Integer::sum;

        // 测试平方操作
        log.info("square: {}", square.apply(5));
        // 测试求和操作
        log.info("sum: {}", sum.apply(5, 10));
    }
}
