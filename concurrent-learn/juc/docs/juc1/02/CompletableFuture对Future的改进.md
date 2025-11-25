# `CompletableFuture` 对 `Future` 的改进

## 1. `CompletableFuture` 为什么会出现

- 传统 `Future` 的局限性
    - 在 `Java 5` 中引入了 `Future` 接口，用于表示异步计算的结果。
    - 虽然 `Future` 可以获取异步任务的结果，但它存在一些明细的限制:
      - 不支持回调机制：无法在任务完成后自动触发其他操作。
      - 阻塞调用：使用 `get()` 方法会阻塞当前线程直到结果返回。
      - 缺乏组合能力：难以将多个异步任务组合起来形成复杂流程。

- 为了解决这些问题， `Java 8` 引入了 `CompletableFuture` ，它实现了 `Future` 和 `CompletionStage` 接口，提供了更强大的功能：
  - 支持非阻塞式的异步编程模型。
  - 提供丰富的链式调用方法，如 `thenApply()`、`thenCompose()`、`thenCombine()` 等。
  - 允许注册回调函数，在任务完成时自动执行。
  - 更好地支持并行与串行的任务编排。

## 2. `CompletableFuture` 和 `CompletionStage` 介绍

- `CompletableFuture` 是 `Future` 接口的一个实现类，并且还实现了 `CompletionStage` 接口。
  - 提供了非常强大的 `Future` 的扩展功能，可以帮助我们简化异步编程的复杂性，并且提供了函数式编程的能力，可以通过回调的方式处理计算结果，也提供了转换和组合 `CompletableFuture` 的方法。
  - 它可能代表一个明确完成的 `Future`，也可能代表一个完成阶段 `CompletionStage`，它支持在计算完成以后触发一些函数或执行某些动作。

- `CompletionStage` 
  - 代表异步计算过程中的某一个阶段，一个阶段完成以后可能会触发另外一个阶段。
  - 一个阶段的执行可能是被单个阶段的完成触发，也可能是由多个阶段一起触发。

**架构关系大致如下：**
```
        +-------------------+           +-------------------+
        |   Future<T>       |           | CompletionStage<T>|
        +-------------------+           +-------------------+
                ^                                 ^
                |                                 |
                -----------------------------------
                                 ^
                                 |
                      +---------------------+
                      | CompletableFuture<T>|
                      +---------------------+
```

## 3. 核心的四个静态方法

`CompletableFuture` 提供了几个非常重要的静态方法用于创建 `CompletableFuture` 实例，这些是构建异步任务的基础：
### `runAsync(Runnable runnable)`
    - 执行不带返回值的异步任务。
    - 使用默认的 ForkJoinPool 线程池执行给定的 Runnable 任务。
    - 返回 `CompletableFuture<Void>` 类型的对象。

### `runAsync(Runnable runnable, Executor executor)`
    - 执行不带返回值的异步任务，并指定自定义的线程池。
    - 允许你传入一个 `Executor` 来控制任务执行的线程环境。
    - 返回 `CompletableFuture<Void>` 类型的对象。

### `supplyAsync(Supplier<U> supplier)`
    - 执行带有返回值的异步任务。
    - 默认使用 ForkJoinPool 线程池来执行 Supplier 并获取其结果。
    - 返回 `CompletableFuture<U>` 类型的对象，其中 U 是 Supplier 提供的结果类型。

### `supplyAsync(Supplier<U> supplier, Executor executor)`
    - 执行带有返回值的异步任务，并允许指定使用的线程池。
    - 可以通过传递 `Executor` 参数来自定义执行任务的线程池。
    - 返回 `CompletableFuture<U>` 类型的对象。

以上四个方法是启动异步计算的关键入口点。前两个适用于不需要返回结果的任务（例如日志记录、通知等），后两个则适合那些需要产生某种结果的操作（如网络请求、数据库查询等）。通过合理选择是否提供自定义 `Executor`，可以灵活地管理资源和性能。

> 对于上述Executor参数说明：若没有指定，则使用默认的 `ForkJoinPool.commonPool()` 作为它的线程池执行异步代码，如果指定线程池，则使用我们自定义的或者特别指定的线程池执行异步代码


