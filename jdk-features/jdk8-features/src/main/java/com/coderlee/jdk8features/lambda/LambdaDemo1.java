package com.coderlee.jdk8features.lambda;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Lambda表达式演示类
 * 该类展示了从传统匿名内部类到Lambda表达式的演进过程，以及Java 8 Stream API的使用
 * 通过员工数据的排序和过滤操作，演示了函数式编程的优势
 */
@Slf4j
public class LambdaDemo1 {

    // 员工列表，包含姓名、年龄和工资信息
    List<Employee> employees = Arrays.asList(
            new Employee("张三", 18, 5000.55),
            new Employee("李四", 29, 6000.66),
            new Employee("王五", 30, 7000.77),
            new Employee("赵六", 41, 8000.88),
            new Employee("孙七", 52, 9000.99)
    );

    /**
     * 使用传统匿名内部类实现Comparator接口进行排序
     * 按年龄降序排列员工
     */
    @Test
    public void test1() {
        log.info("排序前: {}", employees);
        Comparator<Employee> com = new Comparator<Employee>() {
            @Override
            public int compare(Employee o1, Employee o2) {
                return o1.getAge() - o2.getAge() >= 0 ? -1 : 1;
            }
        };
        employees.sort(com);
        log.info("排序后: {}", employees);
    }

    /**
     * 使用Lambda表达式简化Comparator实现
     * 展示了Lambda表达式相比匿名内部类的简洁性
     */
    @Test
    public void test2() {
        log.info("排序前: {}", employees);
        // Lambda表达式实现Comparator
        Comparator<Employee> com = (o1, o2) -> o1.getAge() - o2.getAge() >= 0 ? -1 : 1;
        employees.sort(com);
        log.info("排序后: {}", employees);
    }

    /**
     * 测试按年龄过滤员工
     * 使用传统的面向对象方式实现过滤功能
     */
    @Test
    public void test3() {
        List<Employee> list = filterEmployeesByAge(employees, 35);
        for (Employee employee : list) {
            log.info("{}", employee);
        }
    }

    /**
     * 测试按工资过滤员工
     * 使用传统的面向对象方式实现过滤功能
     */
    @Test
    public void test4() {
        List<Employee> list = filterEmployeesBySalary(employees, 9000.00);
        for (Employee employee : list) {
            log.info("{}", employee);
        }
    }

    /**
     * 使用策略模式和自定义接口实现过滤
     * 通过实现具体的过滤策略类来实现不同条件的过滤
     */
    @Test
    public void test5() {
        log.info("过滤前: {}", employees);
        List<Employee> listFilterByAge = filterEmployeesByPredicate(employees, new EmployeePredicateByAge(35));
        log.info("按照年龄过滤后: {}", listFilterByAge);
        List<Employee> listFilterBySalary = filterEmployeesByPredicate(employees, new EmployeePredicateBySalary(9000.00));
        log.info("按照工资过滤后: {}", listFilterBySalary);
    }

    /**
     * 使用匿名内部类实现过滤
     * 通过实现MyPredicate接口的匿名内部类来定义过滤条件
     */
    @Test
    public void test6() {
        log.info("过滤前: {}", employees);
        List<Employee> listFilterByAge = filterEmployeesByPredicate(employees, new MyPredicate<Employee>() {
            @Override
            public boolean test(Employee employee) {
                return employee.getAge() >= 35;
            }
        });
        log.info("按照年龄过滤后: {}", listFilterByAge);
        List<Employee> listFilterBySalary = filterEmployeesByPredicate(employees, new MyPredicate<Employee>() {
            @Override
            public boolean test(Employee employee) {
                return employee.getSalary() >= 9000.00;
            }
        });
        log.info("按照工资过滤后: {}", listFilterBySalary);
    }

    /**
     * 使用Lambda表达式实现过滤
     * 展示了Lambda表达式如何简化过滤逻辑的实现
     */
    @Test
    public void test7() {
        log.info("过滤前: {}", employees);
        List<Employee> listFilterByAge = filterEmployeesByPredicate(employees, employee -> employee.getAge() >= 35);
        log.info("按照年龄过滤后: {}", listFilterByAge);
        List<Employee> listFilterBySalary = filterEmployeesByPredicate(employees, employee -> employee.getSalary() >= 9000.00);
        log.info("按照工资过滤后: {}", listFilterBySalary);
    }

    /**
     * 使用Stream API实现过滤
     * 展示了Java 8 Stream API的函数式编程特性
     */
    @Test
    public void test8() {
        log.info("过滤前: {}", employees);
        List<Employee> listFilterByAge = employees.stream().filter(employee -> employee.getAge() >= 35).collect(Collectors.toList());
        log.info("按照年龄过滤后: {}", listFilterByAge);
        List<Employee> listFilterBySalary = employees.stream().filter(employee -> employee.getSalary() >= 9000.00).collect(Collectors.toList());
        log.info("按照工资过滤后: {}", listFilterBySalary);
    }

    /**
     * 通用过滤方法
     * 使用策略模式，接受一个过滤器接口作为参数，实现灵活的过滤功能
     *
     * @param employees 员工列表
     * @param myPredicate 过滤条件接口
     * @return 符合过滤条件的员工列表
     */
    public List<Employee> filterEmployeesByPredicate(List<Employee> employees, MyPredicate<Employee> myPredicate) {
        List<Employee> list = new ArrayList<>();
        for (Employee employee : employees) {
            if (myPredicate.test(employee)) {
                list.add(employee);
            }
        }
        return list;
    }

    /**
     * 按年龄过滤员工
     * 传统实现方式，用于对比Lambda表达式的优势
     *
     * @param employees 员工列表
     * @param age 年龄阈值
     * @return 年龄大于等于指定年龄的员工列表
     */
    public List<Employee> filterEmployeesByAge(List<Employee> employees, int age) {
        List<Employee> list = new ArrayList<>();
        for (Employee employee : employees) {
            if (employee.getAge() >= age) {
                list.add(employee);
            }
        }
        return list;
    }

    /**
     * 按工资过滤员工
     * 传统实现方式，用于对比Lambda表达式的优势
     *
     * @param employees 员工列表
     * @param salary 工资阈值
     * @return 工资大于等于指定工资的员工列表
     */
    public List<Employee> filterEmployeesBySalary(List<Employee> employees, double salary) {
        List<Employee> list = new ArrayList<>();
        for (Employee employee : employees) {
            if (employee.getSalary() >= salary) {
                list.add(employee);
            }
        }
        return list;
    }
}
