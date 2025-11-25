# `Future` 接口常用实现类 `FutureTask` 异步任务

## 目录

1. [什么是 `FutureTask`](#1-什么是-futuretask)
2. [`FutureTask` 的类关系图](#2-futuretask-的类关系图)
3. [`FutureTask` 的特点](#3-futuretask-的特点)
4. [`FutureTask` 的核心方法](#4-futuretask-的核心方法)
    - [构造方法](#构造方法)
    - [主要方法](#主要方法)
5. [`FutureTask` 的状态转换](#5-futuretask-的状态转换)
6. [`FutureTask` 的实现原理](#6-futuretask-的实现原理)
    - [核心设计](#核心设计)
    - [状态流转](#状态流转)
    - [关键机制](#关键机制)
7. [`FutureTask` 的应用场景](#7-futuretask-的应用场景)
    - [线程池中的应用](#线程池中的应用)
    - [缓存场景](#缓存场景)
8. [`Future` 优缺点分析](#8-future-优缺点分析)
    - [优点](#优点)
    - [缺点](#缺点)

## 1. 什么是 `FutureTask`

`FutureTask` 是 Java 并发包 (`java.util.concurrent)` 中的一个重要类，它是 `Future` 接口的一个实现类。`FutureTask` 可以包装 `Callable` 或 `Runnable` 对象，使其具备异步执行的能力，并且可以通过 `Future` 接口的方法获取异步计算的结果。

## 2. `FutureTask` 的类关系图

```
          @FunctionalInterface
                     ↓ (注解)
               Future<V>
                  ↑
                  ↑
      RunnableFuture<V>    Runnable
                  ↑          ↑
                  └──────┬──┘
                         ↓
                  FutureTask<V>  
```

## 3. `FutureTask` 的特点

- 可取消: 可以通过 `cancel()` 方法取消正在执行的任务
- 可获取结果: 可以通过 `get()` 方法阻塞等待并获取异步计算结果
- 状态检查: 可以通过 `isDone()` 和 `isCancelled()` 检查任务状态
- 复用性: 同一个 `FutureTask` 实例只能执行一次，多次调用会返回相同结果

## 4. `FutureTask` 的核心方法

### 构造方法

```java
// 包装 Callable 对象
FutureTask(Callable<V> callable);

// 包装 Runnable 对象，result 为完成后的返回值
FutureTask(Runnable runnable, V result);
```

### 构造方法使用场景

#### `FutureTask(Callable<V> callable)`

- **适用场景**：需要获取异步任务执行结果
- **特点**：
    - `Callable` 接口的 `call()` 方法可以返回一个值
    - 通过 `get()` 方法可以获取计算结果
    - 适用于有返回值的业务逻辑，如数据库查询、文件读取等

#### `FutureTask(Runnable runnable, V result)`

- **适用场景**：任务本身不返回结果，但需要在完成时提供一个默认返回值
- **特点**：
    - `Runnable` 接口的 `run()` 方法无返回值
    - `result` 参数指定任务完成后的返回值
    - 适用于只需要执行某些操作但不需要返回具体计算结果的场景

**选择建议**：
- 如果需要获取异步计算结果，使用 `FutureTask(Callable<V> callable)`
- 如果只是需要执行某个操作且需要一个固定的返回值，使用 `FutureTask(Runnable runnable, V result)`


### 主要方法
- `get()`: 阻塞等待任务完成并返回结果
- `get(long timeout, TimeUnit unit)`: 带超时时间的获取结果方法
- `cancel(boolean mayInterruptIfRunning)`: 取消任务执行
- `isCancelled()`: 判断任务是否被取消
- `isDone()`: 判断任务是否已完成
- `run()`: 执行任务（通常由线程池调用）

## 5. `FutureTask` 的状态转换
`FutureTask` 内部维护了一个状态机，具有以下几种状态：

- `NEW`: 新创建状态
- `COMPLETING`: 正在完成中
- `NORMAL`: 正常完成
- `EXCEPTIONAL`: 异常完成
- `CANCELLED`: 已取消
- `INTERRUPTING`: 正在中断
- `INTERRUPTED`: 已中断

状态转换是单向的，一旦完成就不能回到之前的状态。

## 6. `FutureTask` 的实现原理

### 核心设计
- 状态机模式：通过 `int state` 字段管理任务生命周期
- 无锁设计：使用 `CAS` 操作保证线程安全
- 双重接口：实现 `RunnableFuture<V>` 接口，兼具可执行性和结果获取能力

### 状态流转
```
NEW → COMPLETING → NORMAL/EXCEPTIONAL/CANCELLED
```

### 关键机制
`run()`：执行 `Callable` 任务，通过 `CAS` 控制执行权限
`get()`：阻塞等待任务完成，返回结果或抛出异常
`cancel()`：支持中断和取消操作

## 7. `FutureTask` 的应用场景

### 线程池中的应用
详见 [FutureThreadPoolDemo.java](../../../src/main/java/com/coderlee/juc1/cf/FutureThreadPoolDemo.java)

### 缓存场景

由于 `FutureTask` 的特性，它可以用来实现高效的缓存机制：

```java
private final Map<String, FutureTask<String>> cache = new ConcurrentHashMap<>();

public String getData(String key) throws Exception {
    FutureTask<String> future = cache.get(key);
    if (future == null) {
        future = new FutureTask<>(() -> fetchDataFromDB(key));
        cache.put(key, future);
        new Thread(future).start();
    }
    return future.get();
}
```

## 8. `Future` 优缺点分析
基于 `FutureTask` 和 `Future` 接口的特性与使用方式，我们可以总结出其优点和缺点如下：

### 优点

1. **异步执行能力**
    - 支持将耗时任务提交后立即返回，不阻塞主线程。
    - 允许程序继续执行其他任务，提高并发性能。

2. **结果获取灵活**
    - 提供 `get()` 方法可以等待并获取异步计算结果。
    - 支持带超时时间的结果获取（`get(long timeout, TimeUnit unit)`），避免无限期等待。

3. **任务控制完善**
    - 可以通过 `cancel()` 方法取消尚未完成的任务。
    - 提供了 `isDone()` 和 `isCancelled()` 等方法来检查任务状态。

4. **线程安全设计**
    - 使用 `CAS` 操作保证多线程环境下的安全性。
    - 内部状态机的设计使得任务状态转换清晰且不可逆。

5. **良好的复用性**
    - 同一个 `FutureTask` 实例可以在多个地方调用 `get()` 获取相同结果。
    - 结果只会被计算一次，适合用于缓存场景。

### 缺点

1. **阻塞式 API**
    - 调用 `get()` 方法会阻塞当前线程直到任务完成，降低了响应性。
    - 不支持回调通知机制，无法在任务完成后自动触发后续操作。

2. **缺乏链式调用支持**
    - 不像现代异步编程框架那样支持链式操作（如 `CompletableFuture` 的 `thenApply`、`thenCompose` 等）。
    - 多个依赖任务之间难以优雅地组合处理。

3. **批量操作缺失**
    - 缺乏对多个任务统一管理的能力，例如不能方便地等待一组任务全部完成。
    - 需要手动编写额外代码才能实现类似功能。

4. **异常处理不够直观**
    - 异常信息只有在调用 `get()` 方法时才会抛出，增加了调试难度。
    - 对于复杂的异步流程，错误传播路径不易追踪。

5. **资源释放问题**
    - 如果没有正确调用 `get()` 或 `cancel()` 方法，可能导致资源泄漏。
    - 在高并发环境下需要注意合理管理 FutureTask 生命周期。

6. **轮询模式**
    - `isDone()` 轮询会耗费CPU资源，而且不见得可以及时获取到计算结果.

综上所述，虽然 `Future` 和 `FutureTask` 提供了一种基本的异步编程手段，首先，`Future` 对于结果的获取不是很友好，只能通过堵塞或者轮询的方式得到任务的结果。其次，在复杂场景下存在一定的局限性。对于更高级别的异步需求，推荐考虑使用 `CompletableFuture` 或反应式编程模型。