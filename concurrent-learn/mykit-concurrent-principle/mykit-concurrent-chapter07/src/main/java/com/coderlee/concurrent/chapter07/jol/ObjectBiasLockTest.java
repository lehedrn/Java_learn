package com.coderlee.concurrent.chapter07.jol;

import org.openjdk.jol.info.ClassLayout;

import lombok.extern.slf4j.Slf4j;

/**
 * ObjectBiasLockTest 类用于演示 Java 对象的偏向锁（Biased Locking）机制。
 * 
 * 偏向锁是 Java 虚拟机中的一种优化机制，旨在减少无竞争情况下的同步开销。
 * 默认情况下，偏向锁在 JVM 启动后会延迟几秒才被激活，但可以通过设置 JVM 参数
 * “-XX:+UseBiasedLocking -XX:BiasedLockingStartupDelay=0” 来立即启用偏向锁。
 */
@Slf4j
public class ObjectBiasLockTest {

    /**
     * main 方法用于测试和展示对象头中的偏向锁信息。
     * 
     * 通过创建一个 [MyObject] 实例，并使用 `ClassLayout.parseInstance(obj).toPrintable()` 
     * 打印对象的内存布局，可以观察到对象头中的锁状态变化。
     * 
     */
    // 正常情况下是无锁状态ouput: 
    /*  OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
        0     4        (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
        4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
        8     4        (object header)                           04 db 00 f8 (00000100 11011011 00000000 11111000) (-134161660)
        12     4    int MyObject.count                            0
    Instance size: 16 bytes
    Space losses: 0 bytes internal + 0 bytes external = 0 bytes total */
    // 因为Java中的偏向锁默认在JVM启动几秒之后才会被激活。可以通过设置JVM参数“-XX:+UseBiasedLocking -XX:BiasedLockingStartupDelay=0”来禁止偏向锁延迟
    // output:
    /*  OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
        0     4        (object header)                           05 00 00 00 (00000101 00000000 00000000 00000000) (5)
        4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
        8     4        (object header)                           04 db 00 f8 (00000100 11011011 00000000 11111000) (-134161660)
        12     4    int MyObject.count                            0
    Instance size: 16 bytes
    Space losses: 0 bytes internal + 0 bytes external = 0 bytes total */    
    public static void main(String[] args) {
        // 创建一个 MyObject 实例，用于分析其对象头信息
        MyObject obj = new MyObject();

        // 使用 JOL 工具打印对象的内存布局信息
        // 输出结果中包含对象头、实例数据以及对齐填充等信息
        log.info("{}", ClassLayout.parseInstance(obj).toPrintable());
    }
}