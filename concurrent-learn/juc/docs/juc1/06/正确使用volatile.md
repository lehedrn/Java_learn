# 正确使用volatile

## 单一赋值可以，但是含复合运算赋值不可以（i++之类的）

```
volatile int a = 10;
volatile boolean flag = true;
```

## 状态标志，判断业务是否结束

作为一个布尔状态标志，用于指示发生了一个重要的一次性事件，例如完成初始化或任务结束

参见 [VolatileSeeDemo.java](../../../src/main/java/com/coderlee/juc1/volatiles/VolatileSeeDemo.java)

## 开销较低的读，写锁策略

当读远多于写，结合使用内部锁和volatile变量来减少同步的开销

原理是：利用 `volatile` 保证读操作的可见性，利用 `synchronized` 保证符合操作的原子性

参见: [VolatileCounter.java](../../../src/main/java/com/coderlee/juc1/volatiles/VolatileCounter.java)

## DCL双端锁的发布

**问题描述**： 一个加锁的单例模式场景
参见 [UnSafeDoubleCheckSingleton.java](../../../src/main/java/com/coderlee/juc1/volatiles/UnSafeDoubleCheckSingleton.java)

在单线程环境下（或者说正常情况下），在“问题代码处”，会执行以下操作，保证能获取到已完成初始化的实例：
```
// 1. 分配对象的内存空间
memory = allocate();
// 2. 初始化对象
ctorInstance(memory);
// 3. 设置 instance 指向刚分配的内存地址
instance = memory;
```

**隐患**:
在多线程环境下，在“问题代码处”，会执行以下操作，由于重排序导致2，3乱序，后果就是其他线程得到的是 `null` 而不是完成初始化的对象，其中第3步中实例化分多步执行（分配内存空间、初始化对象、将对象指向分配的内存空间），某些编译器为了性能原因，会将第二步和第三步重排序，这样某个线程肯能会获得一个未完全初始化的实例：

```
// 1. 分配对象的内存空间
memory = allocate();
// 3. 设置 instance 指向刚分配的内存地址
// 注意这个时候，对象还没有被初始化
instance = memory;
// 2. 初始化对象
ctorInstance(memory);
```

**解决方案**:
多线程下的解决方案：加 `volatile` 修饰
参见 [SafeDoubleCheckSingleton.java](../../../src/main/java/com/coderlee/juc1/volatiles/SafeDoubleCheckSingleton.java)