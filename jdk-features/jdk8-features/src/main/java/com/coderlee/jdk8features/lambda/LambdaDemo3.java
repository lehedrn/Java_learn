package com.coderlee.jdk8features.lambda;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Lambda表达式功能演示类3
 * 该类演示了Lambda表达式的多种用法，包括集合排序、函数式接口的使用等
 */
@Slf4j
public class LambdaDemo3 {
    // 员工列表，用于演示排序功能
    List<Employee> employees = Collections.emptyList();

    /**
     * 测试前的初始化方法，创建员工列表
     */
    @Before
    public void setUp() {
        employees = Arrays.asList(
                new Employee("张三", 18, 5000.55),
                new Employee("李四", 29, 6000.66),
                new Employee("王五", 30, 7000.77),
                new Employee("赵六", 41, 8000.88),
                new Employee("孙七", 52, 9000.99)
        );
    }

    /**
     * 演示使用Lambda表达式对员工列表进行排序
     * 按年龄降序排列，年龄相同时按姓名升序排列
     */
    @Test
    public void test1() {
        log.info("排序前: {}", employees);
        // 使用Lambda表达式实现Comparator接口，对员工按年龄降序排列，年龄相同时按姓名升序排列
        Collections.sort(employees, (e1, e2) -> {
            if (e1.getAge() == e2.getAge()) {
                return e1.getName().compareTo(e2.getName());
            } else {
                return -Integer.compare(e1.getAge(), e2.getAge());
            }
        });
        log.info("排序后: {}", employees);
    }

    /**
     * 演示函数式接口和方法引用的使用
     * 展示字符串处理的不同方式
     */
    @Test
    public void test2() {
        String str = "abcefg";
        // 使用方法引用将字符串转为大写
        log.info("{} upperCase-> {}", str, handlerStr(str, String::toUpperCase));
        // 使用Lambda表达式截取字符串
        log.info("{} substring 2,4 -> {}", str, handlerStr(str, s -> s.substring(2, 4)));
    }

    /**
     * 字符串处理方法，接收字符串和函数式接口作为参数
     * @param str 待处理的字符串
     * @param sf 函数式接口，用于定义字符串处理逻辑
     * @return 处理后的字符串
     */
    public String handlerStr(String str, StrFunc sf) {
        return sf.getValue(str);
    }

    /**
     * 演示泛型函数式接口的使用
     * 展示对Long类型数据进行不同操作的Lambda表达式
     */
    @Test
    public void test3() {
        // 使用Lambda表达式实现加法运算
        log.info("{} + {} = {}", 1, 2, handlerLong(1L, 2L, (l1, l2) -> l1 + l2));
        // 使用Lambda表达式实现减法运算
        log.info("{} - {} = {}", 2, 3, handlerLong(2L, 3L, (l1, l2) -> l1 - l2));
        // 使用Lambda表达式实现乘法运算
        log.info("{} * {} = {}", 3, 4, handlerLong(3L, 4L, (l1, l2) -> l1 * l2));
    }

    /**
     * 长整型数据处理方法，接收两个Long值和函数式接口作为参数
     * @param l1 第一个Long值
     * @param l2 第二个Long值
     * @param of 函数式接口，用于定义对两个Long值的操作逻辑
     * @return 操作结果
     */
    public Long handlerLong(Long l1, Long l2, OptFunc<Long, Long> of) {
        return of.getValue(l1, l2);
    }
}

/**
 * 字符串函数式接口，用于处理字符串操作
 * 该接口接收一个字符串参数，返回处理后的字符串
 */
@FunctionalInterface
interface StrFunc {
    String getValue(String str);
}

/**
 * 操作函数式接口，用于处理两个相同类型参数的操作
 * 该接口接收两个泛型参数，返回另一个泛型类型的结果
 * @param <T> 输入参数的类型
 * @param <R> 返回结果的类型
 */
@FunctionalInterface
interface OptFunc<T, R> {
    R getValue(T t1, T t2);
}
