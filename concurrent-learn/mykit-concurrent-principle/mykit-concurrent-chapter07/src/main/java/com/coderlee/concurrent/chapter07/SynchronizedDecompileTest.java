package com.coderlee.concurrent.chapter07;

import lombok.extern.slf4j.Slf4j;

/**
 * 类注释：
 * 该类用于演示 Java 中的同步机制，包含两种实现方式：同步方法和同步代码块。
 * 通过反编译字节码的方式可以观察到 `synchronized` 关键字在底层的具体实现细节。
 * cd target/classes/com/coderlee/concurrent/chapter07/
 * javap -c SynchronizedDecompileTest
 * output:
 * <p>
 * public synchronized void syncMethod();
    descriptor: ()V
    flags: (0x0021) ACC_PUBLIC, ACC_SYNCHRONIZED
    Code:
      stack=2, locals=1, args_size=1
         0: getstatic     #18                 // Field log:Lorg/slf4j/Logger;
         3: ldc           #28                 // String This is a synchronized method.
         5: invokeinterface #30,  2           // InterfaceMethod org/slf4j/Logger.info:(Ljava/lang/String;)V
        10: return
      LineNumberTable:
        line 12: 0
        line 13: 10
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0      11     0  this   Lcom/coderlee/concurrent/chapter07/SynchronizedDecompileTest;

    public void syncBlock();
    Code:
       0: aload_0
       1: dup
       2: astore_1
       3: monitorenter
       4: getstatic     #18                 // Field log:Lorg/slf4j/Logger;
       7: ldc           #37                 // String This is a synchronized block.
       9: invokeinterface #30,  2           // InterfaceMethod org/slf4j/Logger.info:(Ljava/lang/String;)V
      14: aload_1
      15: monitorexit
      16: goto          22
      19: aload_1
      20: monitorexit
      21: athrow
      22: return
    Exception table:
       from    to  target type
           4    16    19   any
          19    21    19   any
 * </p>
 */
@Slf4j
public class SynchronizedDecompileTest {

    /**
     * 方法注释：
     * 使用 `synchronized` 修饰的方法，线程在进入该方法时会自动获取对象锁，
     * 执行完毕后释放锁。适合需要对整个方法进行同步保护的场景。
     * 
     * 行注释：
     * 记录日志以表明这是一个同步方法。
     */
    public synchronized void syncMethod() {
        log.info("This is a synchronized method."); // 日志输出，标记为同步方法
    }

    /**
     * 方法注释：
     * 使用 `synchronized` 代码块的方式进行同步，锁的对象是当前实例 (`this`)。
     * 相较于同步方法，同步代码块可以更精确地控制锁的作用范围，减少锁的竞争。
     * 
     * 行注释：
     * 使用 `monitorenter` 和 `monitorexit` 实现同步块的加锁与解锁操作。
     */
    public void syncBlock() {
        synchronized (this) { // 对当前实例加锁
            log.info("This is a synchronized block."); // 日志输出，标记为同步代码块
        }
    }
}