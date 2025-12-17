package com.coderlee.juc1.cas;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * CAS (Compare-And-Swap) 操作演示类
 *
 * CAS 是一种无锁的并发控制机制，它包含三个操作数：
 * - 内存位置(V)
 * - 预期值(A)
 * - 新值(B)
 * 只有当内存位置V的值等于预期值A时，才会将内存位置V的值更新为新值B，否则不更新。
 * 整个比较和替换操作是原子性的。
 */
@Slf4j
public class CASDemo {

    public static void main(String[] args) {
        // 创建一个初始值为5的原子整数
        AtomicInteger atomicInt = new AtomicInteger(5);

        // 第一次尝试CAS操作：期望值为5，更新为2025
        // 由于当前值确实是5，所以操作成功
        log.info("将atomicInt从5修改成2025, 是否成功: {}, 最终结果: {}",
                 atomicInt.compareAndSet(5, 2025), atomicInt.get());

        // 第二次尝试CAS操作：期望值仍为5，但当前值已经是2025，所以操作失败
        log.info("将atomicInt从5修改成2025, 是否成功: {}, 最终结果: {}",
                 atomicInt.compareAndSet(5, 2025), atomicInt.get());
    }
}
