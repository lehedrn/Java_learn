package com.coderlee.artconcurrentbook.chapter03;

import lombok.extern.slf4j.Slf4j;

/**
 * 顺序一致性：
 * 因为编译器和处理器的优化，可能会导致操作乱序执行，导致结果不符合预期。
 */
@Slf4j
public class ReorderExample {
    int a = 0;
    boolean flag = false;

    public void write() {
        a = 1; // 操作1：对a赋值
        flag = true; // 操作2：对flag赋值
        log.info("Write: flag={}, a={}", flag, a);
    }

    public void read() {
        if (flag) { // 操作3：读取flag
            int i = a * a; // 操作4：读取a并计算
            log.info("Read: i={}", i);
        }
    }
}