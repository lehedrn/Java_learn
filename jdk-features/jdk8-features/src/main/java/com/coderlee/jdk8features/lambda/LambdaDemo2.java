package com.coderlee.jdk8features.lambda;

import org.junit.Test;

import java.util.Comparator;
import java.util.function.Consumer;

/**
 * LambdaDemo2 - Lambda表达式语法演示类
 *
 * 本类展示了Java 8中Lambda表达式的基本语法和使用方式
 *
 * Lambda表达式基础语法：(参数列表) -> {方法体}
 * Java 8引入了"->"操作符，称为箭头操作符或Lambda操作符
 * 箭头操作符将Lambda表达式分为两个部分：
 * - 左侧：参数列表
 * - 右侧：Lambda体，即需要执行的功能
 *
 * Lambda表达式的主要语法格式：
 * 1. 无参数，无返回值：() -> System.out.println("")
 * 2. 有一个参数，无返回值：(x) -> System.out.println(x)
 * 3. 若只有一个参数，可省略小括号：x -> System.out.println(x)
 * 4. 多个参数，有返回值，Lambda体多条语句：(int x, int y) -> { ... }
 * 5. Lambda体只有一条语句时，可省略return和大括号：(int x, int y) -> x + y
 * 6. 参数类型可省略，JVM会根据上下文推断： (x, y) -> x + y
 *
 * Lambda表达式需要函数式接口支持：
 * 函数式接口是指只有一个抽象方法的接口，可用@FunctionalInterface注解标识
 */
public class LambdaDemo2 {

    /**
     * 示例1：无参数无返回值的Lambda表达式
     * 对比传统匿名内部类与Lambda表达式的写法
     * 展示了Lambda表达式访问外部变量的特性（变量需为final或effectively final）
     */
    @Test
    public void test1() {
        int version = 8; // JDK 1.8中，变量即使不显式声明为final也能在Lambda中使用

        // 传统匿名内部类写法
        Runnable r = new Runnable() {
            @Override
            public void run() {
                System.out.println("hello world" + version);
            }
        };
        r.run();

        // Lambda表达式写法
        Runnable r1 = () -> System.out.println("hello world" + version);
        r1.run();
    }

    /**
     * 示例2：使用Consumer函数式接口演示Lambda表达式
     * 使用方法引用System.out::println替代Lambda表达式
     */
    @Test
    public void test2() {
        // 使用方法引用
        Consumer<String> consumer = System.out::println;
        consumer.accept("hello world");
    }

    /**
     * 示例4：使用Comparator函数式接口比较Employee对象
     * 多参数、多语句Lambda表达式的完整写法
     * 根据年龄和薪资综合比较两个员工
     */
    @Test
    public void test4() {
        Comparator<Employee> com = (x, y) -> {
            if (x.getAge() > y.getAge() && x.getSalary() > y.getSalary()) {
                return 1;
            } else if (x.getAge() < y.getAge() && x.getSalary() < y.getSalary()) {
                return -1;
            } else {
                return 0;
            }
        };
    }

    /**
     * 示例5：简化版Employee比较器
     * 显式声明参数类型，单行表达式返回结果
     * 根据年龄差判断员工大小关系
     */
    @Test
    public void test5() {
        Comparator<Employee> com = (Employee x, Employee y) -> x.getAge() - y.getAge() > 0 ? 1 : -1;
    }

    /**
     * 示例6：进一步简化版Employee比较器
     * 省略参数类型声明，利用类型推断机制
     */
    @Test
    public void test6() {
        Comparator<Employee> com = (x, y) -> x.getAge() - y.getAge() > 0 ? 1 : -1;
    }

    /**
     * 示例7：自定义函数式接口使用示例
     * 展示如何通过Lambda表达式实现不同的数学运算
     * 演示了高阶函数的概念：接受函数作为参数的函数
     */
    @Test
    public void test7() {
        // 计算100的平方
        System.out.println(opreation(100, (x) -> x * x));
        // 计算100乘以10
        System.out.println(opreation(100, (x) -> x * 10));
        // 计算100乘以100
        System.out.println(opreation(100, (x) -> x * 100));
    }

    /**
     * 高阶函数：接受一个整数和一个函数式接口，对整数执行指定操作
     * @param num 要处理的数字
     * @param myFun 函数式接口，定义对数字的操作
     * @return 处理后的结果
     */
    public Integer opreation(Integer num, MyFun<Integer> myFun) {
        return myFun.getValue(num);
    }
}
