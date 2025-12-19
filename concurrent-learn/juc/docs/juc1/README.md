# juc 并发编程

1. 线程基础知识

- [`Thread.start()` 源码分析](01/Thread.start()源码分析.md)
- [Java多线程相关概念](01/Java多线程相关概念.md)
- [用户线程和守护线程](01/用户线程和守护线程.md)

2. `CompletableFuture`

- [`Future` 接口理论知识](02/Future接口理论知识.md)
- [`Future` 接口常用实现类 `FutureTask` 异步任务](02/Future接口常用实现类FutureTask异步任务.md)
- [`CompletableFuture` 对 `Future` 的改进](02/CompletableFuture对Future的改进.md)
- [案例精讲-电商网站的比价](02/案例精讲-电商网站的比价.md)
- [`CompletableFuture` 常用方法](02/CompletableFuture常用方法.md)

3. `Java` 锁事

- [乐观锁和悲观锁](03/乐观锁和悲观锁.md)
- [锁相关的8种案例](03/锁相关的8种案例.md)
- [ `synchronized` 锁](03/synchronized锁.md)
- [公平锁和非公平锁](03/公平锁和非公平锁.md)
- [可重入锁(又名递归锁)](03/可重入锁.md)
- [死锁及排查](03/死锁及排查.md)
- [ `objectMonitor` 在 `synchronized` 锁中的作用](03/objectMonitor在Synchronized锁中的作用.md)
- [写锁(独占锁)/读锁(共享锁)]
- [自旋锁 `SpinLock`]
- [无锁、独占锁、读写锁、邮戳锁的演变]
- [无锁、偏向锁、轻量锁、重量锁的演变]

4. `LockSupport` 与线程中断

- [线程中断机制](04/线程中断机制.md)
- [`LockSupport` 是什么](04/LockSupport.md)
- [线程等待唤醒机制](04/线程等待唤醒机制.md)

5. `Java` 内存模型 `JMM`

- [计算机硬件存储体系](05/计算机硬件存储体系.md)
- [`Java` 内存模型 `Java Memory Model`](05/Java内存模型JMM.md)
- [`JMM` 规范下的三大特性](05/JMM规范下的三大特性.md)
- [`JMM` 规范下多线程对变量的读写过程](05/JMM规范下多线程对变量的读写过程.md)
- [`JMM` 规范下多线程先行发生原则之 `happens-before`](05/JMM规范下多线程先行发生原则之happens-before.md)

6. `volatile` 与 `JMM`

- [`volatile` 修饰的变量的两大特点](06/volatitle修饰的变量的两大特点.md)
- [内存屏障](06/内存屏障.md)
- [`volatile` 特性](06/volatile特性.md)
- [正确使用 `volatile`](06/正确使用volatile.md)

7. `CAS`

- [`CAS` 介绍](07/CAS介绍.md)
- [原子引用](07/原子引用.md)
- [自旋锁](07/自旋锁.md)
- [`ABA` 问题](07/ABA问题.md)

8. 原子操作类

- [原子操作类](08/原子操作类.md)

9. `ThreadLocal`

- [`ThreadLocal` 介绍](09/ThreadLocal.md)

10. `Java` 对象内存布局和对象头

- [`Java` 对象内存布局和对象头](10/Java对象内存布局和对象头.md)