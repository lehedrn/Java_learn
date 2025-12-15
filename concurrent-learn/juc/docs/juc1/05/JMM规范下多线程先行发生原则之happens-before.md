# `JMM` 规范下多线程先行发生原则之 `happens-before`

在 `JVM` 中，如果一个操作执行的结果需要对另一个操作可见或者代码重排序，那么这两个操作之间必须存在 `happens-before`（先行发生）原则，逻辑上的先后关系。

## 案例分析

| 场景描述 | 代码示例 | 线程分配 | 操作类型 |
|---------|---------|---------|---------|
| 线程A执行赋值操作 | `x = 5` | 线程A | 写操作 |
| 线程B执行读取操作 | `y = x` | 线程B | 读操作 |

### happens-before关系分析

| 条件 | 结果 | 说明 |
|-----|-----|-----|
| 存在 `happens-before` 关系 | `y = 5` 必然成立 | 线程A的 `x = 5` 操作对线程B可见 |
| 不存在 `happens-before` 关系 | `y = 5` 不一定成立 | 可能出现可见性问题，线程B可能读到旧值 |

这就是happens-before原则的为例----------->包含可见性和有序性的约束

## `happens-before` 原则说明

如果 `Java` 内存模型中所有的有序性都仅靠 `volatile` 和 `synchronized` 来完成，那么有很多操作都将变得非常罗嗦，但是我们在编写 `Java` 并发代码的时候并没有察觉到这一点。
我们没有时时、处处、次次，添加 `volatile` 和 `synchronized` 来完成程序，这是因为 `Java` 语言中 `JMM` 原则下，有一个“先行发生”（`happens-before`）的原则限制和规矩，给你理好了规矩！
这个原则非常重要：它是判断数据是否存在竞争，线程是否安全的非常有用的手段。依赖这个原则，我们可以通过几条简单规则一揽子解决并发环境下两个操作之间是否可能存在冲突的所有问题，而不需要陷入 `Java` 内存模型晦涩难懂的底层编译原理之中。

## `happens-before` 总原则

- 如果一个操作 `happens-before` 另一个操作，那么第一个操作的执行结果将对第二个操作可见，而且第一个操作的执行顺序排在第二个操作之前
- 如果两个操作之间存在 `happens-before` 关系，并不意味着一定要按照 `happens-before` 原则制定的顺序来执行。如果重排之后的执行结果与按照 `happens-before` 关系来执行的结果一致，那么这种重排序并不非法。

## `happens-before` 八条原则

从 `JDK 5` 开始，`Java` 使用新的 `JSR-133` 内存模型，提供了 `happens-before` 原则来辅助保证程序执行的原子性、可见性以及有序性的问题，它是判断数据是否存在竞争、线程是否安全的依据，`happens-before` 原则内容如下：
1. 次序规则：一个线程内，按照代码的顺序，写在前面的操作先行发生于写在后面的操作，也就是说前一个操作的结果可以被后续的操作获取（保证语义串行性，按照代码顺序执行）。比如前一个操作把变量x赋值为1，那后面一个操作肯定能知道x已经变成了1
2. 锁定规则：一个 `unLock` 操作先行发生于后面对同一个锁的 `lock` 操作（后面指时间上的先后）。
3. `volatile` 变量规则：对一个 `volatile` 变量的写操作先行发生于后面对这个变量的读操作，前面的写对后面的读是可见的，这里的后面同样指时间上的先后。
4. 传递规则：如果操作A先行发生于操作B，而操作B又先行发生于操作C，则可以得出操作A先行发生于操作C。
5. 线程启动规则（Thread start Rule）：`Thread` 对象的 `start()` 方法先行发生于此线程的每一个动作。
6. 线程中断规则（Thread Interruption Rule）：
   a. 对线程 `interrupt()` 方法的调用先行发生于被中断线程的代码检测到中断事件的发生
   b. 可以通过 `Thread.interrupted()` 检测到是否发生中断
   c. 也就是说你要先调用 `interrupt()` 方法设置过中断标志位，我才能检测到中断发生
7. 线程终止规则（Thread Termination Rule）：线程中的所有操作都优先发生于对此线程的终止检测，我们可以通过 `isAlive()` 等手段检测线程是否已经终止执行。
8. 对象终结规则（Finalizer Rule）：一个对象的初始化完成（构造函数执行结束）先行发生于它的 `finalize()` 方法的开始------->对象没有完成初始化之前，是不能调用 `finalized()` 方法的

## `happens-before` 小总结

- 在 `Java` 语言里面，`Happens-before` 的语义本质上是一种可见性
- `A happens-before B` ,意味着A发生过的事情对B而言是可见的，无论A事件和B事件是否发生在同一线程里
- `JVM` 的设计分为两部分：
  - 一部分是面向我们程序员提供的，也就是 `happens-before` 规则，它通俗易懂的向我们程序员阐述了一个强内存模型，我们只要理解 `happens-before` 规则，就可以编写并发安全的程序了
  - 另一部分是针对 `JVM` 实现的，为了尽可能少的对编译器和处理器做约束从而提升性能，`JMM` 在不影响程序执行结果的前提下对其不做要求，即允许优化重排序，我们只要关注前者就好了，也就是理解`happens-before` 规则即可，其他繁杂的内容由 `JMM` 规范结合操作系统给我们搞定，我们只写好代码即可。

## 案例说明

```
private int value =0;
public int getValue(){
    return value;
}
public int setValue(){
    return ++value;
}
```

**问题描述**：假设存在线程A和B，线程A先（时间上的先后）调用了 `setValue()` 方法，然后线程B调用了同一个对象的 `getValue()` 方法，那么线程B收到的返回值是什么？
**答案**: 不一定
**原因**:
分析`happens-before`规则（规则5，6，7，8可以忽略，和代码无关）
- 由于两个方法由不同线程调用，不满足一个线程的条件，不满足程序次序规则
- 两个方法都没有用锁，不满足锁定规则
- 变量没有使用 `volatile` 修饰，所以不满足 `volatile` 变量规则
- 传递规则肯定不满足
综上：无法通过 `happens-before` 原则推导出线程 `A happens-before B`，虽然可以确定时间上线程A优于线程B，但就是无法确定线程B获得的结果是什么，所以这段代码不是线程安全的。

> 注意：如果两个操作的执行次序无法从 `happens-before` 原则推导出来，那么就不能保证他们的有序性，虚拟机可以随意对他们进行重排序

**问题修复**

- 把 `getter/setter` 方法都定义为 `synchronized` 方法 -------> 不好，重量锁，并发性下降

```
private int value =0;
public synchronized int getValue(){
    return value;
}
public synchronized int setValue(){
    return ++value;
}
```

- 把 `value` 定义为 `volatile` 变量，由于 `setter` 方法对 `value` 的修改不依赖 `value` 的原值，满足 `volatile` 关键字使用场景

```
/**
* 利用volatile保证读取操作的可见性，
* 利用synchronized保证符合操作的原子性结合使用锁和volatile变量来减少同步的开销
*/
private volatile int value =0;
public int getValue(){
    return value;
}
public synchronized int setValue(){
    return ++value;
}
```