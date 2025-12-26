package com.coderlee.jdk8features.functioninterface;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * JDK 8 函数式接口使用示例类
 * 演示 Function、Consumer、Predicate 和 Supplier 四个核心函数式接口的用法
 *
 * @author coderlee
 */
@Slf4j
public class BaiseExample {

    /**
     * Function接口示例：接收一个参数并返回一个结果，支持函数组合
     * 演示了使用 andThen 方法进行函数组合，将字符串转换为长度，再将长度转回字符串
     */
    @Test
    public void testFunction() {
        // 定义一个Function：将字符串转换为整数（长度）
        Function<String, Integer> toLength = String::length;
        // 定义一个Function：将整数转换为字符串
        Function<Integer, String> intToString = Object::toString;
        // 使用andThen方法组合两个函数，先计算长度再转为字符串
        Function<String, String> pipline = toLength.andThen(intToString);

        log.info("{}", pipline.apply("hello coderlee"));
    }

    /**
     * Consumer接口示例：接收一个参数但不返回结果，支持操作组合
     * 演示了使用 andThen 方法组合多个消费操作
     */
    @Test
    public void testConsumer() {
        // 定义一个Consumer：打印字符串
        Consumer<String> print = log::info;
        // 定义一个Consumer：将字符串转为大写后打印
        Consumer<String> shout = s -> log.info("{}", s.toUpperCase());

        // 使用andThen方法组合两个消费操作：先打印再大写打印
        Consumer<String> combined = print.andThen(shout);

        combined.accept("hello coderlee");
    }

    /**
     * Predicate接口示例：接收一个参数并返回布尔值，支持条件组合
     * 演示了使用 and 方法组合多个条件判断
     */
    @Test
    public void testPredicate() {
        // 定义一个Predicate：判断数字是否为正数
        Predicate<Integer> isPositive = x -> x > 0;
        // 定义一个Predicate：判断数字是否为偶数
        Predicate<Integer> isEven = x -> x % 2 == 0;

        // 使用and方法组合两个条件：既为正数又为偶数
        Predicate<Integer> positiveAndEven = isPositive.and(isEven);
        log.info("[{}] is positive and even, result is : {}", -1, positiveAndEven.test(-1));
        log.info("[{}] is positive and even, result is : {}", 4, positiveAndEven.test(4));
        log.info("[{}] is positive and even, result is : {}", 5, positiveAndEven.test(5));
    }

    /**
     * Supplier接口示例：不接收参数但返回一个结果
     * 演示了如何使用Supplier获取随机数
     */
    @Test
    public void testSupplier() {
        // 定义一个Supplier：提供随机数
        Supplier<Double> randomSupplier = Math::random;
        log.info("random number is : {}", randomSupplier.get());
        log.info("random number is : {}", randomSupplier.get());
    }

    /**
     * Supplier与Stream结合示例：演示如何使用Supplier生成Stream数据
     * 使用Stream.generate方法结合Supplier生成指定数量的随机数
     */
    @Test
    public void testStreamSupplier() {
        // 定义一个Supplier：提供随机数
        Supplier<Double> randomSupplier = Math::random;
        // 使用Supplier生成Stream，限制为10个元素，并对每个元素执行操作
        Stream.generate(randomSupplier)
                .limit(10)
                .forEach(x -> log.info("random number is : {}", x));
    }
}
