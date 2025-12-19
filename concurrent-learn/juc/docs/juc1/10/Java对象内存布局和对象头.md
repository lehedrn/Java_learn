# `Java` 对象内存布局和对象头

## 1. 对象在堆内存中的布局

在 `HotSpot` 虚拟机中，一个 `Java` 对象在堆内存中的存储结构主要由以下三部分组成：

1. **对象头（Object Header）**
    - 包含两部分信息：
        - **`Mark Word`**：用于存储对象自身的运行时数据，如哈希码（`HashCode`）、GC 分代年龄、锁状态标志、线程持有的锁、偏向线程 ID、偏向时间戳等。这部分数据的长度在 32 位和 64 位虚拟机（未开启压缩指针）中分别为 32bit 和 64bit。
        - **`Class Pointer`（类型指针）**：即对象指向它的类元数据的指针，虚拟机通过这个指针来确定这个对象是哪个类的实例。如果使用了 `-XX:+UseCompressedOops` 参数（默认开启），这部分的长度会从 64bit 压缩为 32bit。
    - 如果对象是一个数组，那么对象头还需要有额外的一部分用来记录数组的长度。

2. **实例数据（Instance Data）**
    - 这部分是对象真正存储的有效信息，也是在程序代码中所定义的各种类型的字段内容。无论是从父类继承下来的，还是在子类中定义的字段，都需要记录起来。这部分的存储顺序会受到虚拟机分配策略参数（FieldsAllocationStyle）和字段在 Java 源码中定义顺序的影响。

3. **对齐填充（Padding）**
    - 这并不是必然存在的部分，它仅仅起着占位符的作用。由于 HotSpot 虚拟机的自动内存管理系统要求对象起始地址必须是 8 字节的整数倍，换句话说，就是对象的大小必须是 8 字节的整数倍。而对象头部分正好是 8 字节的整数倍（16 字节或 12 字节 + 4 字节 padding），因此，当对象实例数据部分没有对齐时，就需要通过对齐填充来补全。

## 2. 对象头详解

### 2.1 Mark Word 结构详解

`Mark Word` 是对象头中最核心的部分，用于存储对象的运行时元数据。其内容会根据对象的状态动态变化，以实现高效的空间利用。

#### Mark Word 在不同锁状态下的结构

以下是 32 位 JVM 和 64 位 JVM（开启指针压缩）下 `Mark Word` 在不同状态下的详细结构：

| 锁状态 | 32位JVM结构 | 64位JVM结构 |
|--------|-------------|-------------|
| **无锁** | `hash`:25 \| `age`:4 \| `biased_lock`:1 \| `lock`:2 | `unused`:25 \| `hash`:31 \| `unused`:1 \| `age`:4 \| `biased_lock`:1 \| `lock`:2 |
| **偏向锁** | `thread`:23 \| `epoch`:2 \| `age`:4 \| `biased_lock`:1 \| `lock`:2 | `thread`:54 \| `epoch`:2 \| `unused`:1 \| `age`:4 \| `biased_lock`:1 \| `lock`:2 |
| **轻量级锁** | `ptr_to_lock_record`:30 \| `lock`:2 | `ptr_to_lock_record`:62 \| `lock`:2 |
| **重量级锁** | `ptr_to_monitor`:30 \| `lock`:2 | `ptr_to_monitor`:62 \| `lock`:2 |
| **GC标记** | `flag`:30 \| `lock`:2 | `flag`:62 \| `lock`:2 |

#### 字段说明

- `hash`: 25/31 位的对象哈希码
- `age`: 4 位的 GC 分代年龄
- `biased_lock`: 1 位的偏向锁标识（1 表示可偏向）
- `lock`: 2 位的锁状态标志位
    - 00: 无锁
    - 01: 偏向锁
    - 10: 轻量级锁
    - 11: 重量级锁
- `thread`: 持有偏向锁的线程 ID
- `epoch`: 偏向锁时间戳
- `ptr_to_lock_record`: 指向栈中 `Lock Record` 的指针
- `ptr_to_monitor`: 指向重量级锁 `Monitor` 的指针
- `flag`: GC 标记位
- `unused`: 未使用的填充位

### 2.2 Class Pointer（类型指针）

`Class Pointer`（也称为 `Klass Pointer`）指向方法区中的 `InstanceKlass` 对象，包含以下重要信息：
- 对象的方法表（`vtable`）
- 类的字段布局信息
- 类的常量池指针
- 访问标志等元数据

在 64 位 JVM 中，默认开启 `-XX:+UseCompressedOops` 时采用指针压缩技术，将 8 字节指针压缩为 4 字节存储。

### 2.3 数组长度（仅数组对象）

对于数组对象，对象头还会额外包含 4 字节的数组长度信息，这使得 JVM 能够快速获取数组大小而无需查询元数据。

### 2.4 内存对齐考虑

整个对象头大小需满足 JVM 的内存对齐要求，在大多数 64 位系统中要求按照 8 字节边界对齐，不足部分通过 padding 填充。

## 3. 从 `Object obj = new Object()` 来谈对象在内存的布局和对象头 

当我们执行 `Object obj = new Object()` 这行代码时，JVM会在堆内存中创建一个 `Object` 实例，并按照特定的内存布局进行存储。

### 3.1 对象创建过程中的内存分配

1. **内存分配**：JVM首先在堆内存中为新创建的 `Object` 实例分配内存空间
2. **对象头初始化**：
    - `Mark Word` 被初始化为无锁状态
    - `Class Pointer` 指向 `Object` 类的 `InstanceKlass`
    - 由于 `Object` 不是数组，不需要数组长度字段

3. **实例数据初始化**：`Object` 类本身没有实例字段，所以实例数据部分为空
4. **对齐填充**：根据需要添加 padding 字节确保对象大小为 8 字节的整数倍

### 3.2 新建 Object 对象的对象头状态

新创建的 `Object` 实例具有以下特征：

- **初始锁状态**：`Mark Word` 处于无锁状态，`lock` 标志位为 `00`
- **偏向锁设置**：`biased_lock` 位为 1，表示该对象支持偏向锁
- **GC分代年龄**：`age` 位为 0，表示刚创建的对象
- **哈希码**：此时还未计算哈希码，相关位为空

### 3.3 对象引用与对象头的关系

变量 `obj` 实际上存储的是指向堆内存中该 `Object` 实例的引用。通过这个引用，JVM可以：
- 定位到对象的起始地址
- 解析对象头中的 `Class Pointer` 来确定对象的具体类型
- 访问 `Mark Word` 中的锁状态和其他元数据信息

### 3.4 利用 `JOL` 来打印对象内存布局

```
@Slf4j
public class JOLDemo {
    public static void main(String[] args) {

//        log.info("vm deatils: {}", VM.current().details());

        Object obj = new Object();
        log.info("obj: {}", ClassLayout.parseInstance(obj).toPrintable());
        Customer1 c1 = new Customer1();
        log.info("c1: {}", ClassLayout.parseInstance(c1).toPrintable());
        Customer c = new Customer();
        log.info("c: {}", ClassLayout.parseInstance(c).toPrintable());
    }
}

class Customer1 {}

class Customer {
    boolean flag = false;
    long id;
    int age;
    String name;
}
```

运行结果如下：

```
17:10:29.513 [main] INFO com.coderlee.juc1.objecthead.JOLDemo - obj: java.lang.Object object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
      0     4        (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
      4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4        (object header)                           68 0d 00 00 (01101000 00001101 00000000 00000000) (3432)
     12     4        (loss due to the next object alignment)
Instance size: 16 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total

17:10:29.516 [main] INFO com.coderlee.juc1.objecthead.JOLDemo - c1: com.coderlee.juc1.objecthead.Customer1 object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
      0     4        (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
      4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4        (object header)                           c0 7a 0c 01 (11000000 01111010 00001100 00000001) (17595072)
     12     4        (loss due to the next object alignment)
Instance size: 16 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total

17:10:29.518 [main] INFO com.coderlee.juc1.objecthead.JOLDemo - c: com.coderlee.juc1.objecthead.Customer object internals:
 OFFSET  SIZE               TYPE DESCRIPTION                               VALUE
      0     4                    (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
      4     4                    (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4                    (object header)                           c8 7c 0c 01 (11001000 01111100 00001100 00000001) (17595592)
     12     4                int Customer.age                              0
     16     8               long Customer.id                               0
     24     1            boolean Customer.flag                             false
     25     3                    (alignment/padding gap)                  
     28     4   java.lang.String Customer.name                             null
Instance size: 32 bytes
Space losses: 3 bytes internal + 0 bytes external = 3 bytes total
```

**运行结果分析**

通过 `JOL` (Java Object Layout) 工具打印出的对象内存布局，我们可以清晰地观察到不同对象在内存中的实际存储情况：

1. `Object` 对象 (`obj`) 内存分析

```
java.lang.Object object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
      0     4        (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
      4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4        (object header)                           68 0d 00 00 (01101000 00001101 00000000 00000000) (3432)
     12     4        (loss due to the next object alignment)
Instance size: 16 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
```

- **对象头占用 12 字节**：
    - 前 8 字节 `(object header)` 对应 `Mark Word`
    - 后 4 字节对应 `Class Pointer`（开启了指针压缩）
- **总大小 16 字节**：符合 8 字节对齐要求
- **Mark Word 值分析**：
    - 第一个字节 `01` 表示无锁状态（最低两位为 01，但由于偏向锁启用，实际是偏向锁状态）
    - 其他字节为 0，表示没有线程持有偏向锁

2. `Customer1` 对象 (`c1`) 内存分析

```
com.coderlee.juc1.objecthead.Customer1 object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
      0     4        (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
      4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4        (object header)                           c0 7a 0c 01 (11000000 01111010 00001100 00000001) (17595072)
     12     4        (loss due to the next object alignment)
Instance size: 16 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
```


- 与 `Object` 对象相同，都是 16 字节大小
- `Customer1` 类没有实例字段，因此只有对象头和对齐填充
- `Class Pointer` 值不同，指向 `Customer1` 的 `InstanceKlass`

3. `Customer` 对象 (`c`) 内存分析

```
com.coderlee.juc1.objecthead.Customer object internals:
 OFFSET  SIZE               TYPE DESCRIPTION                               VALUE
      0     4                    (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
      4     4                    (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4                    (object header)                           c8 7c 0c 01 (11001000 01111100 00001100 00000001) (17595592)
     12     4                int Customer.age                              0
     16     8               long Customer.id                               0
     24     1            boolean Customer.flag                             false
     25     3                    (alignment/padding gap)                  
     28     4   java.lang.String Customer.name                             null
Instance size: 32 bytes
Space losses: 3 bytes internal + 0 bytes external = 3 bytes total
```


- **实例数据部分**：
    - `int age`: 占用 4 字节，偏移量 12
    - `long id`: 占用 8 字节，偏移量 16（需要 8 字节对齐）
    - `boolean flag`: 占用 1 字节，偏移量 24
    - `String name`: 占用 4 字节（引用类型），偏移量 28
- **对齐填充**：在 `flag` 字段后有 3 字节的填充，确保对象总大小为 8 字节的倍数
- **总大小 32 字节**：对象头(12) + 实例数据(17) + 填充(3) = 32 字节

通过这三个示例可以看出，JVM严格按照对象内存布局规范来组织对象的内存结构，并通过合理的字段重排序和对齐填充来优化内存使用效率。