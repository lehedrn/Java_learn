# JUC 学习项目

## 项目简介
本项目旨在学习和实践 Java 并发编程（JUC，`java.util.concurrent` 包及其子包）的相关知识。通过实现常见的并发工具类、线程池、锁机制等功能，深入理解多线程编程的核心概念和最佳实践。

---

## 目录结构

```
  ├── src 
  |   |── main 
  |   |   |── java 
  |   |   |   |── com 
  |   |   |   |   |── coderlee 
  |   |   |   |   |   |── juc 
  |   |   |   |   |   |    └── VolatileDemo.java # 演示 volatile 关键字及线程可见性问题的代码 
  |   |   |   |   |   |    └── AtomicDemo.java # 演示 原子性操作与非原子性操作 
  |   |   |   |   |   |    └── CASDemo.java # 演示 基于 Compare-And-Swap (CAS) 算法的简单实现
  |   |   |   |   |   |    └── CopyOnWriteArrayListDemo.java # 演示 Collections.synchronizedList 和 CopyOnWriteArrayList 的行为差异
  |   |   |   |   |   |    └── CountDownLanchDemo.java # 演示 CountDownLatch 的使用
  |   |   |   |   |   |    └── CallableDemo.java # 演示 CountDownLatch 的使用
  |   |   |   |   |   |    └── LockDemo.java # 演示 三种不同的售票实现方式（无锁、显式锁 ReentrantLock、volatile 关键字），展示线程安全问题及不同同步机制的效果
  |   |   |   |   |   |    |── productorconsumer 生产者消费者案例
  |   |   |   |   |   |    |    └── ProductorConsumerDemoV1.java
  |   |   |   |   |   |    |    └── ProductorConsumerDemoV2.java
  |   |   |   |   |   |    |    └── ProductorConsumerDemoV3.java
  |   |   |   |   |   |    |    └── ProductorConsumerDemoV4.java
  |   |   |   |   |   |    |    └── ProductorConsumerDemoV5.java
  |   |   |   |   |   |    |    └── ProductorConsumerLockDemo.java
  |   |   |   |   |   |    └── ABCAlternateDemo.java # 演示 三个线程（A、B、C）按照顺序交替打印各自的名称。
  |   |   |   |   |   |    └── ReadWriteDemo.java # 演示 使用 `ReadWriteLock` 实现线程安全的读写操作。
  |   |   |   |   |   |    └── Thread8MonitorDemo.java # 演示 线程八锁的场景。
  |   |   |   |   |   |    └── ThreadPoolDemo.java # 演示 线程池的使用方式。
  |   |   |   |   |   |    └── ScheduledThreadPoolDemo.java # 演示 调度线程池 ScheduledThreadPool 的使用方式。
  |   |   |   |   |   |    └── ForkJoinPoolDemo.java # 演示 ForkJoinPool 的使用方式。
  |   |   |   |   |   └── Main.java # 项目入口类 
  ├── README.md # 项目说明文档 
  └── pom.xml # Maven 配置文件，定义项目依赖和构建信息
```

## 功能模块

### 1. juc
包含尚硅谷JUC课程的示例代码。
- **`VolatileDemo.java`**: 
  - 演示了 `volatile` 关键字的作用及其与 `synchronized` 的对比。
  - 包含普通变量、`synchronized` 和 `volatile` 在多线程环境下的可见性实验。

- **`AtomicDemo.java`**: 
  - 演示了原子性操作和非原子性操作之间的区别。

- **`CASDemo.java`**: 
  - 演示了基于 `Compare-And-Swap` 算法的简单实现。

- **`CopyOnWriteArrayListDemo.java`**: 
  - 演示了 `Collections.synchronizedList` 和 `CopyOnWriteArrayList` 的行为差异。
  - 测试了两种集合在并发迭代和修改操作中的表现：
    - `Collections.synchronizedList` 可能会抛出 `ConcurrentModificationException`。
    - `CopyOnWriteArrayList` 在写操作时复制底层数组，避免了迭代异常。

- **`CountDownLanchDemo.java`**: 
  - 演示了 `CountDownLatch` （闭锁） 的使用。

- **`CallableDemo.java`**: 
  - 演示了 `Callable` 和 `FutureTask` 的使用。
    - 定义了一个实现了 `Callable<Integer>` 接口的类 [MyCallable]，用于执行耗时的整数累加计算。
    - 使用 `FutureTask` 包装 [MyCallable] 实例，并通过 Thread 启动异步任务。
    - 调用 `FutureTask.get()` 方法阻塞主线程，直到任务完成并返回结果。
    - 处理可能发生的线程中断或任务执行异常。
  - 理解 `Callable` 的优势：支持返回值和更好的异常处理能力。
  - 掌握 `FutureTask` 的基本用法及其与 `Callable` 的配合方式。
  - 学习如何在多线程环境中获取异步任务的结果。

- **`LockDemo.java`**: 
  - 通过三种不同的售票实现方式（无锁、显式锁 ReentrantLock、volatile 关键字），展示了线程安全问题及不同同步机制的效果。

- **`productorconsumer`**:
  - 包含了生产者消费者案例，用于演示多线程之间的通信和数据同步。

- **`ABCAlternateDemo.java`**: 
  - 演示了 三个线程（A、B、C）按照顺序交替打印各自的名称。

- **`ReadWriteDemo.java`**:
  - 演示了读写锁的使用，用于解决多线程并发访问同一资源的问题。

- **`Thread8MonitorDemo.java**:
  - 演示了 Java 多线程中锁的行为，特别是 **非静态同步方法** 和 **静态同步方法** 在不同场景下的表现。
  - 包含 8 个不同的场景，通过逐步增加复杂性的方式，分析多线程环境下锁的竞争和执行顺序：
    - 场景 1：两个普通同步方法，共用一个对象实例，输出顺序为 `one two`。
    - 场景 2：新增 `Thread.sleep()` 给 [getOne()]，输出顺序仍为 `one two`。
    - 场景 3：新增普通方法 [getThree()]，不受锁限制，输出顺序为 `three one two`。
    - 场景 4：两个普通同步方法，使用两个对象实例，输出顺序为 `two one`。
    - 场景 5：[getOne()] 改为静态同步方法，输出顺序为 `two one`。
    - 场景 6：两个方法均为静态同步方法，输出顺序为 `one two`。
    - 场景 7：一个静态同步方法，一个非静态同步方法，使用两个对象实例，输出顺序为 `two one`。
    - 场景 8：两个静态同步方法，使用两个对象实例，输出顺序为 `one two`。
  - 核心知识点：
    - 非静态同步方法的锁默认为 `this`（当前对象实例）。
    - 静态同步方法的锁为类的 `Class` 对象。
    - 某一时刻内，只能有一个线程持有某个锁，无论涉及几个方法。
  
- **`ThreadPoolDemo.java`**:
  - 演示了 Java 多线程中线程池的使用，以及线程池的参数设置。

- **`ScheduledThreadPoolDemo.java`**:
  - 演示了 Java 多线程中调度线程池的使用，以及调度线程池的参数设置。

- **`ForkJoinPoolDemo.java`**:  
  - 演示了 `ForkJoinPool` 的使用方式及其在大规模计算中的性能优势。
  - 包含以下三种实现方式的对比：
    1. **普通 `for` 循环**：通过传统迭代方法逐个累加数字。
    2. **Java Stream API 并行流**：利用 `LongStream` 的并行流进行高效累加。
    3. **ForkJoinPool 分治算法**：通过自定义 `RecursiveTask` 实现分治逻辑，将任务拆分为更小的子任务并行处理。
  - 核心知识点：
    - `ForkJoinPool` 是 Java 提供的一种高效的并行计算框架，适合处理可分解的大规模任务。
    - `RecursiveTask` 是 Fork/Join 框架中的核心抽象类，用于定义可分解的任务。
    - `fork()` 方法用于异步执行子任务，`join()` 方法用于获取子任务的结果。
    - 分治算法的核心思想是将大任务拆分为小任务，直到任务规模足够小以直接计算。
  - 性能分析：
    - 对比三种实现方式的耗时，展示 `ForkJoinPool` 在处理大规模数据时的性能优势。
    - 强调合理设置任务拆分阈值（如代码中的 [THRESHOLD] 值）对性能的影响。


