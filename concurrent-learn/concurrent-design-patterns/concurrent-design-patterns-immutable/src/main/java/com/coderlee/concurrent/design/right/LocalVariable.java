package com.coderlee.concurrent.design.right;

import lombok.extern.slf4j.Slf4j;

/**
 * 该类演示了局部变量在线程安全中的特性。
 * 局部变量存储在每个线程的栈中，因此它们是线程安全的。
 * 更多关于线程和栈的信息可以参考 {@link Thread} 类的文档。
 */
@Slf4j
public class LocalVariable {

    /**
     * 演示局部变量的基本操作。
     * 由于局部变量的作用域仅限于当前方法调用，因此不会出现线程安全问题。
     */
    public void LocalVariableMethod() {
        // 初始化一个局部变量 count，初始值为 0。
        int count = 0;

        // 对局部变量 count 进行递增操作。
        count++;

        // 打印局部变量 count 的值，验证其行为。
        log.info("count: {}", count);
    }
}