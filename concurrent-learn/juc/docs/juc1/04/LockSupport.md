# `LockSupport` 

`LockSupport` 是 Java 并发包 (`java.util.concurrent.locks`) 中的一个工具类，它提供了基本的线程阻塞和唤醒功能，是构建其他同步组件的基础工具。

## 主要功能

- **基础阻塞操作**：提供 `park()` 方法使线程进入阻塞状态
- **基础唤醒操作**：提供 `unpark(Thread)` 方法唤醒指定线程
- **许可证机制**：基于许可机制实现线程控制，每个线程拥有一个许可
- **底层支持**：是 `AbstractQueuedSynchronizer` (AQS) 等高级同步组件的基础

## 核心方法

### `void park()`
- 阻塞当前线程，直到发生以下情况之一：
    - 其他线程调用了 `unpark(Thread)` 方法作用于当前线程
    - 其他线程中断了当前线程
    - 调用 `spuriously` (虚假唤醒)

### `void park(Object blocker)`
- 与 `park()` 功能相同，额外参数用于记录导致线程阻塞的对象
- 有助于调试和问题诊断，可以通过 `getBlocker()` 获取阻塞对象

### `void unpark(Thread thread)`
- 唤醒指定线程
- 如果指定线程未被阻塞，则使其下次调用 `park()` 不会阻塞
- 每个线程拥有一个许可，`unpark()` 会使许可变为可用状态

### `void parkNanos(long nanos)`
- 阻塞当前线程指定纳秒数，或直到被唤醒

### `void parkUntil(long deadline)`
- 阻塞当前线程直到指定的绝对时间

## 工作原理

- 基于**许可机制**(permit)实现线程控制
- 每个线程默认没有许可(permit=false)
- 调用 `unpark(thread)` 给线程发放许可
- 调用 `park()` 时：
    - 如果有许可，则消费许可继续执行
    - 如果没有许可，则阻塞等待

### 与 `Object.wait()`/`notify()` 的区别

| 特性 | `LockSupport.park()` | `Object.wait()`      |
|------|----------------------|----------------------|
| 所属类 | `LockSupport` 工具类 | `Object` 基类          |
| 使用限制 | 无特定要求 | 必须在同步块内调用            |
| 许可机制 | 每个线程一个许可，可预先发放 | 依赖对象监视器              |
| 唤醒方式 | `unpark()` 可提前调用 | `notify()`必须在线程等待后调用 |

## 应用场景

- **构建同步器**：作为 `ReentrantLock`、`CountDownLatch` 等同步组件的基础
- **自定义锁**：实现自定义的同步控制逻辑
- **线程协调**：精确控制线程的阻塞和唤醒时机

### 注意事项

- `park()` 和 `unpark()` 操作不会抛出 `InterruptedException`
- 调用 `park()` 后需要检查中断状态
- `unpark()` 操作具有"先发制人"特性，可以提前调用使后续 `park()` 不阻塞