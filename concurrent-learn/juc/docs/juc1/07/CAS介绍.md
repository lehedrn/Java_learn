# `CAS`介绍

## `CAS`是什么？

`CAS` (Compare-And-Swap) 是一种无锁的并发控制机制，它是一种硬件级别的原子操作。与传统的基于锁的同步机制不同，`CAS` 允许在不阻塞线程的情况下实现多线程间的同步。

具体来说，`CAS` 操作包含三个操作数：

1. **内存位置（V）**：需要被更新的变量在内存中的地址。
2. **预期值（A）**：进行比较的旧值。
3. **新值（B）**：准备更新的新值。

当执行 `CAS` 操作时，只有当内存位置 `V` 中的实际值等于预期值 `A` 时，才会将内存位置 `V` 的值更新为新值 `B`；否则不会执行任何操作，并返回当前实际值，或者重试（当它重来重试的这种行为称为---自旋）。这个过程是原子性的，即整个比较并交换的操作是不可分割的。

在 Java 中，`java.util.concurrent` 包下的许多类都使用了 `CAS` 来实现高效的并发控制。这些类通过调用底层的 `native` 方法来实现 `CAS` 操作，从而避免了传统 `synchronized` 锁带来的性能开销。

`Java` 中的原子类:
```
java.util.concurrent.atomic

- AtomicBoolean
- AtomicInteger
- AtomicIntegerArray
- AtomicIntegerFieldUpdater
- AtomicLong
- AtomicLongArray
- AtomicLongFieldUpdater
- AtomicMarkableReference
- AtomicReference
- AtomicReferenceArray
- AtomicReferenceFieldUpdater
- AtomicStampedReference
- DoubleAccumulator
- DoubleAdder
- LongAccumulator
- LongAdder
- Striped64
```

**代码示例**
[CASDemo.java](../../../src/main/java/com/coderlee/juc1/cas/CASDemo.java)

## 计数器的实现

### 没有 `CAS` 之前

在没有使用 `CAS` 之前，实现线程安全的计数器开销最小的方式应该是使用 `volatile` + `synchronized`

代码实现参见 [Counter.java](../../../src/main/java/com/coderlee/juc1/cas/Counter.java)

### 使用 `CAS` 之后

代码实现参见 [Counter.java](../../../src/main/java/com/coderlee/juc1/cas/Counter.java)

使用 `CAS` 实现计数器的优势——无锁化设计
- 避免了线程阻塞和上下文切换
- 在低到中等竞争情况下性能显著提升

## `CAS` 的底层原理

### 1. `UnSafe` 类

`Unsafe` 类是 `CAS` 的核心类，由于 `Java` 方法无法直接访问底层系统，需要通过本地（native）方法来访问，`Unsafe` 相当于一个后门，基于该类可以直接操作特定内存的数据。`Unsafe` 类存在于 `sun.misc` 包（新版本在 `jdk.internal.misc` ）中，其内部方法操作可以像 `C` 的指针一样直接操作内存，因此 Java 中 `CAS` 操作的执行依赖于 `Unsafe` 类的方法。一下是关于 `Unsafe` 类中关于 `CAS` 的关键接口：

```java
// Unsafe 中的关键 CAS 方法（native 实现）
public final native boolean compareAndSwapObject(Object o, long offset, Object expected, Object x);
public final native boolean compareAndSwapInt(Object o, long offset, int expected, int x);
public final native boolean compareAndSwapLong(Object o, long offset, long expected, long x);
```

- **内存偏移量(offset)**：通过 `objectFieldOffset` 获取字段在内存中的精确位置
- **原子性保证**：这些操作由 JVM 保证原子性，底层调用 CPU 特定的原子指令（如 x86 的 `cmpxchg` 指令）

> 注意：`Unsafe` 类中的所有方法都是 `native` 修饰的，也就是说 `Unsafe` 类中的所有方法都直接调用操作系统底层资源执行相应任务。正因为其功能强大且绕过了 `Java` 的类型安全检查和访问控制，所以被称为 "Unsafe"。不当使用可能导致程序崩溃或数据损坏。

**问题**：我们知道 `i++` 是线程不安全的，那 `AtomicInteger.getAndIncrement()` 如何保证原子性？

`AtomicInteger` 类主要利用 `CAS` + `volatile` 和 `native` 方法来保证原子操作，从而避免 `synchronized` 的高开销，执行效率大为提升。

`CAS` 并发原语体现在 `Java` 语言中就是 `Unsafe` 类中的各个方法。调用 `Unsafe` 类中的 `CAS` 方法，`JVM` 会帮我们实现出 `CAS` 汇编指令。这是一种完全依赖于硬件的功能，通过它实现了原子操作。再次强调，由于 `CAS` 是一种系统原语，原语属于操作系统用语范畴，是由若干条指令组成的，用于完成某个功能的一个过程，并且原语的执行必须是连续的，在执行过程中不允许被中断，也就是说 `CAS` 是一条 `CPU` 的原子指令，不会造成所谓的数据不一致问题。


### 2. 结合 `AtomicInteger` 的源码实现分析

以 `AtomicInteger` 为例，看高层 API 如何封装 CAS：

```java
public class AtomicInteger extends Number implements java.io.Serializable {
    private static final long valueOffset;
    private volatile int value;

    static {
        try {
            // 获取value字段的内存偏移量，这是CAS操作的关键
            valueOffset = unsafe.objectFieldOffset
                (AtomicInteger.class.getDeclaredField("value"));
        } catch (Exception ex) { throw new Error(ex); }
    }

    // 核心CAS方法
    public final boolean compareAndSet(int expect, int update) {
        return unsafe.compareAndSwapInt(this, valueOffset, expect, update);
    }

    // 基于CAS的自增操作
    public final int getAndIncrement() {
        return unsafe.getAndAddInt(this, valueOffset, 1);
    }
}
```

- **volatile 修饰 value**：保证变量的可见性，但不保证原子性
- **valueOffset**：精确计算字段在对象内存布局中的偏移位置
- **getAndAddInt 实现**：
  ```java
  // Unsafe.java 中的实现逻辑
  public final int getAndAddInt(Object o, long offset, int delta) {
      int v;
      do {
          v = getIntVolatile(o, offset); // 获取当前值
      } while (!compareAndSwapInt(o, offset, v, v + delta)); // CAS尝试
      return v;
  }
  ```

### 3. `CAS` 的底层硬件实现

CAS 操作最终依赖 CPU 的原子指令：
- **x86 架构**：`cmpxchg` 指令（Compare and Exchange）
- **实现原理**：
  1. 将寄存器值与内存值比较
  2. 如果相等，将新值写入内存
  3. 整个过程通过硬件锁总线或缓存一致性协议（MESI）保证原子性
- **内存屏障**：CAS 操作隐含了内存屏障，确保指令重排序不会影响操作的原子性


## `CAS` 的优缺点深度分析

### 优点
- **无锁化**：避免线程阻塞和上下文切换开销
- **细粒度控制**：可针对单个变量进行原子操作
- **非阻塞算法基础**：支持实现高效的无锁数据结构

### 缺点
- **ABA 问题**：需额外机制解决
- **循环开销**：高竞争下可能多次重试
- **只能保证单变量原子性**：多变量操作需额外同步机制
- **CPU 消耗**：高并发下可能造成 CPU 资源浪费

## `JUC` 中 `CAS` 的典型应用

### AQS (AbstractQueuedSynchronizer)
- `state` 变量的修改通过 CAS 实现
- 独占模式：`compareAndSetState(0, 1)`
- 共享模式：状态值的原子增减

### ConcurrentHashMap
- `JDK 8` 中的 `CounterCell` 采用 CAS 实现并发计数
- 链表转红黑树过程中的节点操作

### ConcurrentLinkedQueue
- 非阻塞队列实现，完全基于 CAS 操作
- `tail` 和 `head` 指针的更新

## `CAS` 的性能考量

- **低竞争场景**：性能远优于锁机制
- **高竞争场景**：可能因多次重试导致性能下降
- **JDK 优化**：
  - JDK 8 引入 `LongAdder` 替代 `AtomicLong` 高并发计数
  - 分段累加策略，减少单点竞争

## 现代 `JVM` 对 `CAS` 的优化

- **自旋优化**：`JVM` 会根据系统负载动态调整自旋次数
- **指令重排控制**：`CAS` 操作隐含的内存屏障确保正确性
- **伪共享问题处理**：通过 `@Contended` 注解避免缓存行伪共享


