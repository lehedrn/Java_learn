# `synchronized` 锁

## `synchronized` 的三种应用方式

- 作用与实例方法，当前实例加锁，进入同步代码块前要获取当前实例的锁
- 作用与代码块，对括号里配置的对象加锁
- 作用于静态方法，当前类加锁，进去同步代码前要获取当前类对象的锁

## 从字节码角度分析 `synchronized` 实现

1. 查看 `.class` 的字节码
```
cd concurrent-learn/juc/target/classes/com/coderlee/juc1/locks 
javap -v LockSyncDemo.class
```

2. `synchronized` 同步代码块

实现使用的是 `monitorenter` 和 `monitorexit` 指令

```
  public void m1();
    Code:
       0: aload_0
       1: getfield      #7                  // Field obj:Ljava/lang/Object;
       4: dup
       5: astore_1
       6: monitorenter
       7: getstatic     #13                 // Field log:Lorg/slf4j/Logger;
      10: ldc           #17                 // String ----hello synchronized code block
      12: invokeinterface #19,  2           // InterfaceMethod org/slf4j/Logger.info:(Ljava/lang/String;)V
      17: aload_1
      18: monitorexit
      19: goto          27
      22: astore_2
      23: aload_1
      24: monitorexit
      25: aload_2
      26: athrow
      27: return
    Exception table:
       from    to  target type
           7    19    22   any
          22    25    22   any
```
通常情况下是一个`monitorenter` 配合两个 `monitorexit` 指令，特殊情况下是一个 `monitorenter` 配合一个 `monitorexit`，如代码示例中的 `m1_1()` 方法
```
  public void m1_1();
    Code:
       0: aload_0
       1: getfield      #7                  // Field obj:Ljava/lang/Object;
       4: dup
       5: astore_1
       6: monitorenter
       7: getstatic     #13                 // Field log:Lorg/slf4j/Logger;
      10: ldc           #17                 // String ----hello synchronized code block
      12: invokeinterface #19,  2           // InterfaceMethod org/slf4j/Logger.info:(Ljava/lang/String;)V
      17: new           #25                 // class java/lang/RuntimeException
      20: dup
      21: ldc           #27                 // String ---exp
      23: invokespecial #29                 // Method java/lang/RuntimeException."<init>":(Ljava/lang/String;)V
      26: athrow
      27: astore_2
      28: aload_1
      29: monitorexit
      30: aload_2
      31: athrow
    Exception table:
       from    to  target type
           7    30    27   any
```

3. `synchronized` 普通同步方法
   调用指令将会检查方法的 `ACC_SYNCHRONIZED` 访问标志是否被设置，如果设置了，执行线程会将现持有 `monitor` 锁，然后再执行该方法，最后在方法完成（无论是否正常结束）时释放 `monitor`
```
  public synchronized void m2();
    Code:
       0: getstatic     #13                 // Field log:Lorg/slf4j/Logger;
       3: ldc           #31                 // String ----hello synchronized method 2
       5: invokeinterface #19,  2           // InterfaceMethod org/slf4j/Logger.info:(Ljava/lang/String;)V
      10: return
```

4. `synchronized` 静态同步方法
   `ACC_STATIC`、`ACC_SYNCHRONIZED` 访问标志区分该方法是否是静态同步方法
```
  public static synchronized void m3();
    Code:
       0: getstatic     #13                 // Field log:Lorg/slf4j/Logger;
       3: ldc           #33                 // String ----hello synchronized method 3
       5: invokeinterface #19,  2           // InterfaceMethod org/slf4j/Logger.info:(Ljava/lang/String;)V
      10: return
```

## 反编译 `synchronized` 锁的是什么

1. 为什么任何一个对象都可以成为一个锁
- 在 `Java` 中，每个对象都关联有一个 `monitor`（监视器锁或内置锁）
- 当使用 `synchronized` 关键字时，`JVM` 会自动为对象分配一个 `monitor`
- 对象头中包含 `Mark Word`，其中存储了锁的相关信息，包括锁状态、指向 `monitor` 对象的指针等
- 这使得任何 `Java` 对象都可以作为锁对象使用，无论是实例对象、类对象还是普通对象

从源码上说，ObjectMonitor.java--->ObjectMonitor.cpp--->ObjectMonitor.hpp

2. 什么是管程 `monitor`

`monitor` 是一种同步原语，具有以下特点：
- 互斥性：同一时刻只有一个线程能够持有 `monitor`
- 可重入性：同一个线程可以多次获得同一个 `monitor`，`JVM` 会记录重入次数
- 实现机制：
  - 对于同步代码块：通过 `monitorenter` 和 `monitorexit` 字节码指令实现
  - 对于同步方法：通过检查方法的 `ACC_SYNCHRONIZED` 访问标志实现
- 等待队列：当线程无法获取 `monitor` 时，会被阻塞并放入等待队列中
- 释放机制：当持有 `monitor` 的线程退出同步区域时，会自动释放 `monitor`，唤醒等待队列中的线程
这种设计使得 `synchronized` 成为了 `Java` 中最基本的同步机制之一。