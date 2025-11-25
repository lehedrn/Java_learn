# `Future` 接口理论知识

## 1. `Future` 接口

`Future` 接口是 Java 并发包 (`java.util.concurrent`) 中的一个重要接口，它代表一个异步计算的结果。

### 核心概念

- **异步计算**: `Future` 允许你提交一个任务并在稍后获取其结果，而无需阻塞主线程
- **结果占位符**: 它作为异步操作最终结果的占位符，在结果准备好之前可以进行其他操作
- **生命周期控制**: 提供对异步任务生命周期的控制机制

> 三个特点：多线程、有返回、异步任务

### 主要方法

- `get()`: 阻塞等待计算完成并返回结果
- `get(long timeout, TimeUnit unit)`: 在指定时间内等待计算完成并返回结果
- `cancel(boolean mayInterruptIfRunning)`: 尝试取消任务执行
- `isCancelled()`: 检查任务是否在完成前被取消
- `isDone()`: 判断计算是否已完成

## 2. `Future` 源码

`Future` 接口定义了标准的异步计算契约：

```java
public interface Future<V> {
    boolean cancel(boolean mayInterruptIfRunning);
    boolean isCancelled();
    boolean isDone();
    V get() throws InterruptedException, ExecutionException;
    V get(long timeout, TimeUnit unit) 
        throws InterruptedException, ExecutionException, TimeoutException;
}
```


### 方法详解

- **`cancel(boolean mayInterruptIfRunning)`**:
    - 返回 `true` 如果任务成功被取消
    - `mayInterruptIfRunning` 参数决定是否中断正在执行任务的线程

- **`isCancelled()`**:
    - 只有当任务在正常完成前被取消才返回 `true`

- **`isDone()`**:
    - 当任务以任何形式完成时返回 `true`（包括成功、失败或取消）

- **`get()`**:
    - 无限期阻塞直到计算完成
    - 如果计算过程中抛出异常，则抛出 `ExecutionException`

- **`get(long timeout, TimeUnit unit)`**:
    - 如果在超时时间内未完成计算，则抛出 `TimeoutException`

## 3. `Future` 应用场景

### 常见使用模式

- **并行任务执行**: 同时执行多个独立任务并收集结果
- **长时间运行操作**: 处理耗时操作而不会阻塞调用线程
- **服务集成**: 与外部服务或 API 进行非阻塞集成
- **批处理**: 将大数据集分解为较小的异步块进行处理
- **资源密集型计算**: 将 CPU 密集型工作从主线程卸载

### 实现示例

在 Java 中，`Future` 通常与以下组件一起使用：
- `ExecutorService`: 提交任务进行异步执行
- `Callable`: 作为返回结果的任务表示
- `CompletableFuture`: 更高级的异步编程模式

### 优势

- **提升响应性**: 在长操作期间应用程序保持响应
- **更好的资源利用**: 高效使用线程和系统资源
- **增强用户体验**: 用户在后台操作期间不会遇到阻塞
- **可扩展性**: 能够高效处理多个并发操作