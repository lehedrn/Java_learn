# ThreadLocal

## 1. `ThreadLocal` 简介

### 1.1 `ThreadLocal` 是什么

`ThreadLocal` 是 `Java` 中提供的一种线程局部变量机制。它为每个使用该变量的线程都提供了变量的一个独立副本，使得每个线程都可以独立地改变自己的副本，而不会影响其他线程所对应的副本。`ThreadLocal` 实例通常是类中的私有静态字段，使用它的目的是希望将状态（例如，用户ID或事物ID）与线程关联起来。

### 1.2 `ThreadLocal` 能干什么

- **线程隔离**: 提供线程级别的变量隔离，避免多线程环境下的数据竞争问题
- **上下文传递**: 在同一线程的不同方法调用间传递上下文信息，如用户身份、事务ID等
- **避免参数传递**: 减少方法间不必要的参数传递
- **数据库连接管理**: 为每个线程维护独立的数据库连接
- **Session管理**: 在Web应用中为每个请求线程维护独立的用户会话

### 1.3 API介绍

```
T get() 返回当前线程的此线程局部变量的副本中的值

protected T initialValue() 创建此线程局部变量的初始值

void remove() 删除当前线程的此线程局部变量的副本

void set(T value) 改变当前线程的此线程局部变量的副本中的值

static <T> ThreadLocal<T> withInitial(Supplier<? extends T> supplier) 创建一个 ThreadLocal 实例，并指定初始值
```


### 1.4 示例

[ThreadLocalDemo.java](../../../src/main/java/com/coderlee/juc1/threadlocal/ThreadLocalDemo.java)

### 1.5 小结

- 因为每个 `Thread` 内有自己的实例副本且该副本只有当前线程自己使用
- 既然其他 `ThreadLocal` 不可访问，那就不存在多线程间共享问题
- 统一设置初始值，但是每个线程对这个值得修改都是各自线程互相独立得
- 如何才能不争抢
    - 加入 `synchronized` 或者 `Lock` 控制资源的访问顺序
    - 人手一份，大家各自安好，没有必要争抢

## 2. `ThreadLocal` 源码分析

### 2.1 `ThreadLocal` 源码解读

`ThreadLocal` 的核心实现依赖于 `ThreadLocalMap`，这是一个定制化的哈希表，用于存储线程本地变量。

主要源码结构：
```java
public class ThreadLocal<T> {
    // ThreadLocal实例的hashcode
    private final int threadLocalHashCode = nextHashCode();
    
    // 获取当前线程的变量值
    public T get() {
        Thread t = Thread.currentThread();
        ThreadLocalMap map = getMap(t);
        if (map != null) {
            ThreadLocalMap.Entry e = map.getEntry(this);
            if (e != null) {
                T result = (T)e.value;
                return result;
            }
        }
        return setInitialValue();
    }
    
    // 设置当前线程的变量值
    public void set(T value) {
        Thread t = Thread.currentThread();
        ThreadLocalMap map = getMap(t);
        if (map != null) {
            map.set(this, value);
        } else {
            createMap(t, value);
        }
    }
    
    // 移除当前线程的变量值
    public void remove() {
        ThreadLocalMap m = getMap(Thread.currentThread());
        if (m != null) {
            m.remove(this);
        }
    }
}
```

### 2.2 `Thread`、`ThreadLocal`、`ThreadLocalMap` 的关系

三者之间的关系可以用以下方式描述：

- **`Thread`**：每个线程对象内部持有一个 `ThreadLocalMap` 实例
- **`ThreadLocal`**：作为 `key` 存储在 `ThreadLocalMap` 中，用于标识不同的线程本地变量
- **`ThreadLocalMap`**：线程私有的哈希表，存储该线程所有的 `ThreadLocal` 变量及其对应的值

关系图示：
```
Thread线程对象
├── threadLocals (ThreadLocalMap)
│   ├── Entry[0] -> {key: ThreadLocal实例1, value: 值1}
│   ├── Entry[1] -> {key: ThreadLocal实例2, value: 值2}
│   └── ...
└── 其他线程属性...

ThreadLocal实例1  ThreadLocal实例2
       │                 │
       └─── 都作为key ────┘
```


这种设计保证了：
- 每个 `Thread` 线程拥有自己独立的变量副本
- 不同的 `ThreadLocal` 实例可以在同一个线程中存储不同的值
- 线程之间完全隔离，不存在数据共享和竞争问题

## 3. `ThreadLocal` 内存泄漏问题

### 3.1 什么是内存泄漏

内存泄漏是指程序中已动态分配的堆内存由于某种原因程序未释放或无法释放，造成系统内存的浪费，导致程序运行速度减慢甚至系统崩溃等严重后果。

在 `ThreadLocal` 的场景中，内存泄漏指的是 `ThreadLocalMap` 中的 `Entry` 对象无法被垃圾回收，即使 `ThreadLocal` 实例已经不再被使用，但仍占用着内存资源。

### 3.2 为什么会导致 `ThreadLocal` 内存泄漏，从底层分析

`ThreadLocal` 内存泄漏的根本原因在于 `ThreadLocalMap` 的 `Entry` 结构设计：

```
static class Entry extends WeakReference<ThreadLocal<?>> {
    Object value;
    Entry(ThreadLocal<?> k, Object v) {
        super(k);
        value = v;
    }
}
```

内存泄漏的原因分析：

1. **Entry的键是弱引用，值是强引用**
    - `Entry` 继承自 `WeakReference<ThreadLocal<?>>`，所以 `key`（即 `ThreadLocal` 实例）是弱引用
    - 但 `value` 是强引用，指向实际存储的对象

2. **线程生命周期长于 `ThreadLocal` 实例**
    - 当线程池中的线程长期存活，而 `ThreadLocal` 实例作用域较短时
    - 即使 `ThreadLocal` 实例没有强引用指向，被GC回收后，`Entry` 的 `key` 变为 `null`
    - 但由于 `value` 仍然是强引用，这些 `Entry` 无法被清理，造成内存泄漏

3. **`ThreadLocalMap` 的清理机制不完善**
    - 只有在执行 `set()`、`get()`、`remove()` 操作时才会触发对 `key` 为 `null` 的 `Entry` 的清理
    - 如果线程长时间不执行这些操作，泄漏的 `Entry` 会持续占用内存

### 3.3 为什么要用弱引用，不使用可以么？

使用弱引用的原因和必要性：

#### 3.3.1 为什么使用弱引用：

1. **避免 `ThreadLocal` 实例无法回收**
   ```java
   // 如果使用强引用
   static class Entry extends Reference<ThreadLocal<?>> {
       ThreadLocal<?> key;  // 强引用
       Object value;
   }
   ```

    - 若 `Entry` 对 `ThreadLocal` 使用强引用，则只要 `Thread` 存活，`ThreadLocal` 实例就永远不会被回收
    - 这样会造成更严重的内存泄漏

2. **让 `JVM` 能够回收无用的 `ThreadLocal` 实例**
    - 使用弱引用后，当外部没有强引用指向 `ThreadLocal` 实例时，它可以在下一次GC时被回收
    - 虽然 `Entry` 的 `value` 仍然存在泄漏风险，但这比完全无法回收要好

#### 3.3.2 不使用弱引用的后果：

- `ThreadLocal` 实例永远无法被回收，即使已经不再使用
- 在长时间运行的应用（特别是使用线程池的场景）中，内存会持续增长直至溢出
- 违背了 `ThreadLocal` 设计的初衷——提供线程私有的变量存储

### 3.3 最佳实践建议：

1. **主动调用remove()方法**
   ```
   try {
       threadLocal.set(value);
       // 使用threadLocal
   } finally {
       threadLocal.remove(); // 清理资源，防止内存泄漏
   }
   ```


2. **及时清理不用的 `ThreadLocal` 变量**
    - 在使用完 `ThreadLocal` 后，显式调用 `remove()` 方法
    - 特别是在使用线程池的场景中，这一点尤为重要

3. **`ThreadLocal` 一定要初始化，避免空指针异常**

### 3.4 示例
[ThreadLocalDemo2.java](../../../src/main/java/com/coderlee/juc1/threadlocal/ThreadLocalDemo2.java)


## 4. 强引用、软引用、弱引用、虚引用

`Java` 中的四种引用类型提供了不同级别的可达性和垃圾回收策略，它们在 `ThreadLocal` 的内存管理中起着关键作用。

### 4.1 强引用 (Strong Reference)

```java
Object obj = new Object(); // 强引用
```


- **特点**: 最常见的引用类型，只要强引用存在，对象就不会被垃圾回收
- **GC行为**: 即使内存不足，也不会回收强引用指向的对象，宁愿抛出 `OutOfMemoryError`
- **应用场景**: 正常的对象创建和使用

### 4.2 软引用 (Soft Reference)

```java
SoftReference<Object> softRef = new SoftReference<>(new Object());
```


- **特点**: 内存充足时不回收，内存不足时会被回收
- **GC行为**: 在发生 `OutOfMemoryError` 之前，会回收软引用指向的对象
- **应用场景**: 实现内存敏感的缓存，如图片缓存、网页缓存等

### 4.3 弱引用 (Weak Reference)

```java
WeakReference<Object> weakRef = new WeakReference<>(new Object());
```


- **特点**: 只要发生垃圾回收，无论内存是否充足都会被回收
- **GC行为**: 下一次 GC 时就会被回收（前提是只存在弱引用）
- **应用场景**:
    - `ThreadLocalMap` 中的 `Entry` 键引用
    - `WeakHashMap` 的实现
    - 监听器和回调函数的注册

### 4.4 虚引用 (Phantom Reference)

```java
PhantomReference<Object> phantomRef = new PhantomReference<>(obj, queue);
```


- **特点**: 不能通过虚引用获取对象实例，主要用于跟踪对象被垃圾回收的活动
- **GC行为**: 对象被回收时会收到系统通知
- **应用场景**:
    - 对象被回收时的清理工作
    - 精确控制对象生命周期的场景

### 4.5 引用类型对比表

| 引用类型 | 回收时机 | 生存时间 | 应用场景 |
|---------|---------|---------|---------|
| 强引用 | 从不回收 | JVM进程结束 | 正常对象使用 |
| 软引用 | 内存不足时 | 内存充足时一直存在 | 缓存实现 |
| 弱引用 | 下次GC时 | 很短，取决于GC频率 | ThreadLocal、WeakHashMap |
| 虚引用 | 对象回收时 | 仅用于跟踪回收过程 | 对象清理监控 |

### 4.6 ThreadLocal中引用类型的运用

在 `ThreadLocalMap` 的设计中：

```java
static class Entry extends WeakReference<ThreadLocal<?>> {
    Object value;
    Entry(ThreadLocal<?> k, Object v) {
        super(k); // ThreadLocal作为弱引用的key
        value = v; // value是强引用
    }
}
```


这种设计的深意：
1. **Key使用弱引用**: 允许 `ThreadLocal` 实例在无外部强引用时被回收
2. **Value使用强引用**: 确保在 `ThreadLocal` 被使用期间，存储的值不会被意外回收
3. **平衡考虑**: 在内存管理和功能需求之间找到平衡点

正是因为这种混合引用的设计，才产生了 `ThreadLocal` 的内存泄漏问题，需要开发者手动调用 `remove()` 方法来清理资源。

### 4.7 示例
[ReferenceDemo.java](../../../src/main/java/com/coderlee/juc1/threadlocal/ReferenceDemo.java)


## 5. 关于 `ThreadLocal` 的一些面试题

- `ThreadLocal`中 `ThreadLocalMap` 的数据结构和关系

- `ThreadLocal` 的 `key` 是弱引用，这是为什么？

- `ThreadLocal` 内存泄漏问题

- `ThreadLocal` 中最后为什么要加 `remove` 方法