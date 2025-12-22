# `ReentrantReadWriteLock`、`StampedLock`

## 1. `ReentrantReadWriteLock`

### 1.1 `ReentrantReadWriteLock` 是什么
`ReentrantReadWriteLock` 是 Java 中提供的一种可重入的读写锁实现。它允许多个线程同时进行读操作，但在写操作期间会独占锁，防止其他线程的读或写操作。这个类实现了 `ReadWriteLock` 接口，并提供了类似于 `ReentrantLock` 的语义，支持公平性和非公平性策略。

### 1.2 `ReentrantReadWriteLock` 的特点

- **分离读写锁**：读操作使用共享锁，写操作使用排他锁。
- **可重入性**：同一线程可以多次获取读锁或写锁。
- **锁降级**：允许持有写锁的线程在不释放写锁的情况下获取读锁。
- **公平性选择**：可以选择公平或非公平模式，默认为非公平模式。

### 1.3 `ReentrantReadWriteLock` 的原理

`ReentrantReadWriteLock` 内部维护了一个 `Sync` 对象，该对象继承自 `AbstractQueuedSynchronizer (AQS)`。其核心思想是通过 AQS 的状态值来表示锁的状态：

- 高16位表示读锁的计数。
- 低16位表示写锁的重入次数。

当没有线程持有写锁时，多个线程可以同时持有读锁；而一旦有线程持有写锁，则其他所有试图获取读锁或写锁的线程都会被阻塞。

### 1.4 `ReentrantReadWriteLock` 的应用场景
适用于读多写少的并发场景，例如缓存系统、配置管理等。在这种情况下，频繁的读取不会相互阻塞，提高了系统的并发性能。

### 1.5 示例
- [ReentrantReadWriteLockDemo.java](../../../src/main/java/com/coderlee/juc1/rwlock/ReentrantReadWriteLockDemo.java)
- [LockDownGradingDemo.java](../../../src/main/java/com/coderlee/juc1/rwlock/LockDownGradingDemo.java)

## 2. `StampedLock`

### 2.1 `StampedLock` 是什么

`StampedLock` 是 Java 8 引入的一种新的锁机制，它提供了比 `ReentrantReadWriteLock` 更高的性能和灵活性。与传统的读写锁不同，`StampedLock` 返回一个 `stamp`（戳记）用于控制锁的状态，支持三种锁模式：写锁、悲观读锁和乐观读锁。它不是 `ReentrantReadWriteLock` 的替代品，而是在特定场景下提供更好的性能选择。

### 2.2 `StampedLock` 是由饥饿问题引出的

`ReentrantReadWriteLock` 存在潜在的饥饿问题：当读线程非常多时，写线程可能长时间无法获取到锁，因为读锁是共享的，只要有读线程存在，写线程就需要等待。`StampedLock` 通过引入乐观读锁和允许写锁优先的机制，在一定程度上缓解了这种饥饿问题，提供了更灵活的锁控制策略。

> 如何解决锁饥饿问题：
> - 使用”公平“策略可以一定程度上缓解这个问题
> - 使用”公平“策略是以牺牲系统吞吐量为代价的
> - `StampedLock` 类的乐观读锁方式--->采取乐观获取锁，其他线程尝试获取写锁时不会被阻塞，在获取乐观读锁后，还需要对结果进行校验

### 2.3 `StampedLock` 的特点及优缺点

#### 特点：
- **三种锁模式**：支持写锁、悲观读锁和乐观读锁
- **无重入性**：不支持锁的重入，同一个线程多次获取锁会导致死锁风险
- **高性能**：在读多写少的场景下，乐观读锁能提供非常高的性能
- **非公平性**：默认是非公平锁，但可以通过参数控制

#### 优点：
- 比 `ReentrantReadWriteLock` 性能更高
- 支持乐观读锁，适合读操作远多于写操作的场景
- 提供了更细粒度的锁控制

#### 缺点：
- 不支持重入，使用不当容易导致死锁
- API 使用相对复杂，需要手动管理 `stamp`
- 不支持条件变量 (`Condition`)
- 使用 `StampedLock` 一定不要调用中断操作，即不要调用 `interrupt()` 方法

### 2.4 示例
[StampedLockDemo.java](../../../src/main/java/com/coderlee/juc1/rwlock/StampedLockDemo.java)

