package com.coderlee.jdk8features.lambda;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Lambda表达式演示类4 - 演示JDK8内置函数式接口的使用
 *
 * 该类通过四个测试方法演示了四种不同的内置函数式接口：
 * - Consumer<T>: 消费型接口，接受一个参数但不返回结果
 * - Supplier<T>: 供给型接口，不接受参数但返回结果
 * - Function<T, R>: 函数型接口，接受一个参数并返回结果
 * - Predicate<T>: 断言型接口，接受一个参数并返回布尔值
 */
@Slf4j
public class LamdaDemo4 {

    /**
     * 测试Consumer接口的使用
     * Consumer接口用于消费型操作，接受一个参数但不返回值
     */
    @Test
    public void test1() {
        happy(1000, money -> log.info("开始消费：{}", money));
    }

    /**
     * 模拟消费行为的方法
     * @param money 消费金额
     * @param consumer 消费行为的实现（函数式接口）
     */
    public void happy(double money, Consumer<Double> consumer) {
        consumer.accept(money);
    }

    /**
     * 测试Supplier接口的使用
     * Supplier接口用于供给型操作，不接受参数但返回结果
     */
    @Test
    public void test2() {
        List<Integer> list = getNumList(10, () -> (int) (Math.random() * 100));
        log.info("{}", list);
    }

    /**
     * 根据指定数量和供给函数生成数字列表
     * @param num 要生成的数字数量
     * @param supplier 供给函数，用于生成数字
     * @return 包含生成数字的列表
     */
    public List<Integer> getNumList(int num, Supplier<Integer> supplier) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            list.add(supplier.get());
        }
        return list;
    }

    /**
     * 测试Function接口的使用
     * Function接口用于函数型操作，接受一个参数并返回结果
     */
    @Test
    public void test3() {
        log.info("{} trim----> {}", "   coderlee  ", strHandler("   coderlee  ", String::trim));
        log.info("{} toUpperCase----> {}", "   coderlee  ", strHandler("   coderlee  ", String::toUpperCase));
    }

    /**
     * 对字符串进行处理的方法
     * @param str 待处理的字符串
     * @param func 字符串处理函数
     * @return 处理后的字符串
     */
    public String strHandler(String str, Function<String, String> func) {
        return func.apply(str);
    }

    /**
     * 测试Predicate接口的使用
     * Predicate接口用于断言型操作，接受一个参数并返回布尔值
     */
    @Test
    public void test4() {
        List<String> list = Arrays.asList("java", "mysql", "oracle", "python", "javascript");
        log.info("{} filter length (5) ---> {}", list, filterStr(list, str -> str.length() >= 6));
    }

    /**
     * 根据条件过滤字符串列表
     * @param list 待过滤的字符串列表
     * @param predicate 过滤条件的实现（函数式接口）
     * @return 过滤后的字符串列表
     */
    public List<String> filterStr(List<String> list, Predicate<String> predicate) {
        List<String> result = new ArrayList<>();
        for (String str : list) {
            if (predicate.test(str)) {
                result.add(str);
            }
        }
        return result;
    }
}
