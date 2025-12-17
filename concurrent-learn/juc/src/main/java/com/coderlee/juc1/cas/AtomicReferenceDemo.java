package com.coderlee.juc1.cas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicReference;

/**
 * AtomicReference 使用示例类
 * 演示如何使用 AtomicReference 实现线程安全的对象引用操作
 */
@Slf4j
public class AtomicReferenceDemo {
    /**
     * 主方法 - 演示 AtomicReference 的基本用法
     * 展示了 compareAndSet 方法的 CAS 操作特性
     */
    public static void main(String[] args) {
        // 创建一个 AtomicReference 对象，用于原子性地更新 User 引用
        AtomicReference<User> atomicReference = new AtomicReference<>();

        // 创建两个 User 对象实例
        User z3 = new User("z3", 15);
        User l4 = new User("l4", 20);

        // 设置初始值为 z3
        atomicReference.set(z3);

        log.info("first update");
        // 第一次尝试 CAS 操作：期望当前值为 z3，更新为 l4
        // 由于当前值确实是 z3，所以更新成功
        log.info("z3 update to l4 , is success: {}, reference result: {}",
                 atomicReference.compareAndSet(z3, l4), atomicReference.get());

        log.info("second update");
        // 第二次尝试 CAS 操作：期望当前值仍为 z3，但实际已经是 l4
        // 由于期望值与实际值不匹配，更新失败，引用值保持不变
        log.info("z3 update to l4 , is success: {}, reference result: {}",
                 atomicReference.compareAndSet(z3, l4), atomicReference.get());
    }
}

/**
 * 用户实体类
 * 包含用户名和年龄属性
 */
@Data
@ToString
@AllArgsConstructor
class User {
    /**
     * 用户名
     */
    String username;

    /**
     * 年龄
     */
    int age;
}
