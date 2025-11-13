# concurrent-design-patterns 

## 项目简介
《实战高并发设计模式>的配套源码，包含所有示例代码。


## 目录结构

## 目录结构
```
concurrent-design-patterns
    ├── concurrent-design-patterns-immutable                # 第1章 不可变模式
    ├── concurrent-design-patterns-guarded-suspension       # 第2章 保护性暂挂模式
    ├── concurrent-design-patterns-thread                   # 第3章 两阶段终止模式核心模块
    ├── concurrent-design-patterns-two-phase-termination    # 第3章 两阶段终止模式应用
    |—— concurrent-design-patterns-promise                  # 第4章 承诺模式
    |—— concurrent-design-patterns-producer-comsumer        # 第5章 生产者消费者模式
    |—— concurrent-design-patterns-active-object            # 第6章 主动对象模式
    |—— concurrent-design-patterns-thread-pool              # 第7章 线程池模式
    |—— concurrent-design-patterns-threadlocal              # 第8章 线程特有存储模式
    |—— concurrent-design-patterns-thread-close             # 第9章 串行线程封闭模式
    |—— concurrent-design-patterns-master-slave             # 第10章 主仆模式
    ├── README.md 
    └── pom.xml
```

## 功能模块

### 第一章 不可变模式

1. 线程安全问题

- 案例：访问计数场景，引出线程安全问题
- 错误示例:[WrongCounter.java](concurrent-design-patterns-immutable/src/main/java/com/coderlee/concurrent/design/wrong/WrongCounter.java) 展示了一个典型的非线程安全实现：
  - 使用一个简单的 int visiteCount 变量来记录访问次数
  - `accessVisit()` 方法直接对计数器进行递增操作：`visiteCount++`
  - 缺乏任何同步机制来保护共享变量
> 这种实现在单线程环境下可以正常工作，但在多线程环境下会出现严重问题。visiteCount++ 操作实际上包含三个步骤：读数、加1、写数。当多个线程同时执行这个操作时，可能会发生数据竞争，导致计数不准确。

- 在单体式的应用服务中，解决并发问题的方案，如下：
  - 有锁方案 [SynchronizedCounter.java](concurrent-design-patterns-immutable/src/main/java/com/coderlee/concurrent/design/right/SynchronizedCounter.java)]
    - synchronized
        - 自动管理锁的获取和释放
        - 语法相对简单
    - Lock
        - 提供更灵活的锁定操作
        - 需要在 finally 块中手动释放锁
  - 无锁方案
    - 局部变量, [LocalVariable.java](concurrent-design-patterns-immutable/src/main/java/com/coderlee/concurrent/design/right/LocalVariable.java) 虽然不是计数器的直接解决方案，但是展示了另一种重要的线程安全概念：
      - 局部变量存储在线程私有的栈中
      - 天然具有线程安全性
      - 说明了线程安全的一种重要思路：减少共享状态
    - CAS原子类, [AtomicIntegerTest.java](concurrent-design-patterns-immutable/src/main/java/com/coderlee/concurrent/design/right/AtomicIntegerTest.java) 和 [RightCounter.java](concurrent-design-patterns-immutable/src/main/java/com/coderlee/concurrent/design/right/RightCounter.java)]，这两个类展示了使用 `java.util.concurrent.atomic` 包中的原子类来解决线程安全问题:
      - 使用 `AtomicInteger` 替代普通的 `int` 类型
      - 利用 `incrementAndGet()` 方法实现原子性的递增操作
      - 无需显式加锁，性能较好
    - ThreadLocal
    - 不可变对象
2. 可变类的线程安全问题
- 案例：检票系统，通过模拟检票场景，需要支持多个人同时通过检票口，每个人携带身份证信息。系统需要维护用户检票信息，并支持并发访问。
- 错误示例 [wrong](concurrent-design-patterns-immutable/src/main/java/com/coderlee/concurrent/design/demo/wrong) : 
  - [User](concurrent-design-patterns-immutable/src/main/java/com/coderlee/concurrent/design/demo/wrong/User.java)
    - 字段 `name` 和 `idCard` 都是非 `final` 的可变字段
    - 提供了 `set(String name, Long idCard)` 方法允许修改对象状态
    - 这种可变性在并发环境下会引发数据竞争
  - [TicketCheck](concurrent-design-patterns-immutable/src/main/java/com/coderlee/concurrent/design/demo/wrong/TicketCheck.java)
    - 虽然使用了 `ConcurrentHashMap` 来保证容器级别的线程安全
    - 但在 `updateUser(String userKey, String userName, Long idCard)` 方法中存在线程安全隐患：
      - 从 `userMap` 中获取 `User` 对象
      - 直接调用 `user.set(userName, idCard)` 修改对象状态
      - 多个线程可能同时修改同一个 User 对象，导致数据不一致
- 不可变类解决线程安全问题示例 [right](concurrent-design-patterns-immutable/src/main/java/com/coderlee/concurrent/design/demo/right) :
  - [User.java](concurrent-design-patterns-immutable/src/main/java/com/coderlee/concurrent/design/demo/right/User.java) 不可变类的设计
    - 类声明为 `final`，防止被继承
    - 所有字段都使用 `final` 修饰，确保对象创建后状态不可变
    - 不提供任何修改状态的方法（无 setter）
    - 通过构造函数一次性完成对象初始化
  - [TicketCheck](concurrent-design-patterns-immutable/src/main/java/com/coderlee/concurrent/design/demo/right/TicketCheck.java) 类的安全设计：
    - 使用 `ConcurrentHashMap` 保证容器线程安全
    - `updateUser(String userKey, User user)` 方法直接替换整个 `User` 对象，而不是修改已有对象
    - `getUserMap()` 方法返回 `Collections.unmodifiableMap(userMap)`，防止外部代码修改内部映射表
- 核心思想:
  - **容器安全 ≠ 对象安全**：
    - 即使使用了线程安全的容器（如 `ConcurrentHashMap`），如果存储的对象本身是可变的，仍然可能出现线程安全问题
  - **不可变对象的优势**：
    - 一旦创建就不能被修改，天然具有线程安全性
    - 避免了复杂的同步机制
    - 简化了并发编程的复杂度
  - **防御性编程**：
    - 通过返回不可修改的视图（`Collections.unmodifiableMap`）来保护内部状态
    - 防止外部代码意外修改内部数据结构
3. 消息路由管理器示例——不可变对象模式的应用
- 案例：模拟一个消息推送系统的路由管理器，需要维护不同推送平台（如极光、信鸽、友盟）的路由信息，并支持在运行时动态更新这些路由配置。
- 错误示例 [wrong](concurrent-design-patterns-immutable/src/main/java/com/coderlee/concurrent/design/example/wrong) :
  - [MessageInfo](concurrent-design-patterns-immutable/src/main/java/com/coderlee/concurrent/design/example/wrong/MessageInfo.java) 类的问题：
    - 字段 `deviceCode` 和 `messageUrl` 是可变的，提供了修改方法
  - [MessageRouter](concurrent-design-patterns-immutable/src/main/java/com/coderlee/concurrent/design/example/wrong/MessageRouter.java) 类的问题：
    - 虽然声明了 `messageInfoMap` 为 `final`，但 `getMessageInfoMap()` 方法直接返回原始映射表
    - 在 `updateRoute(String routeKey, String deviceCode, String messageUrl)` 方法中：
      - 直接获取内部映射表的引用
      - 获取其中的 `MessageInfo` 对象
      - 调用 `messageInfo.setDeviceCode()` 和 `messageInfo.setMessageUrl()` 修改对象状态
      - 这种做法破坏了"不可变"的承诺，导致线程安全问题
- 正确实现[right](concurrent-design-patterns-immutable/src/main/java/com/coderlee/concurrent/design/example/right):
  - [MessageInfo](concurrent-design-patterns-immutable/src/main/java/com/coderlee/concurrent/design/example/right/MessageInfo.java)
    - 类声明为 `final`，防止被继承
    - 所有字段都使用 `final` 修饰，确保对象创建后状态不可变
    - 使用 `@Getter` 自动生成只读访问器，不提供任何修改状态的方法
    - 提供拷贝构造函数用于创建新的实例
  - [MessageRouter](concurrent-design-patterns-immutable/src/main/java/com/coderlee/concurrent/design/example/right/MessageRouter.java)
    - 使用 `Collections.unmodifiableMap(messageInfoMap)` 返回只读视图
    - 采用"替换实例"的方式更新缓存：
      - 不直接修改现有对象或映射表
      - 通过 `MessageRouter.setInstance(new MessageRouter())` 创建新实例替换旧实例
      - 这种方式避免了并发修改异常
- 核心思想:
  - 缓存更新的挑战：
    - 在并发环境下更新缓存数据是一个复杂问题
    - 直接修改缓存中的对象会导致数据不一致
  - 不可变对象 + 实例替换模式:
    - 通过创建新实例替换旧实例的方式实现缓存更新
    - 避免了复杂的同步机制和并发修改问题
    - 保证了缓存数据的一致性和线程安全性
  - 防御性设计：
    - 通过返回不可修改的视图保护内部状态
    - 确保外部代码无法意外修改缓存数据
4. JDK中的等效不可变类
- `java.util.concurrent.CopyOnWriteArrayList`
- 等效不可变
- 写时复制(`Copy-On-Write`)

### 第二章 保护性暂挂模式 (Guarded Suspension Pattern)
#### 1. 模式概述
保护性暂挂模式是一种并发设计模式，用于处理线程间协调问题。当某个线程需要执行某个操作但条件不满足时，该线程不会立即执行失败，而是被挂起等待直到条件满足后再继续执行。
#### 2. 核心应用场景
##### 2.1 报警系统示例
在 `alarm` 包中展示了典型的Guarded Suspension模式应用：
- [AlarmAgent.java](concurrent-design-patterns-guarded-suspension/src/main/java/com/coderlee/guarded/suspension/lock/alarm/agent/AlarmAgent.java) 报警代理类
- [Blocker.java](concurrent-design-patterns-guarded-suspension/src/main/java/com/coderlee/guarded/suspension/lock/alarm/blocker/Blocker.java) 阻塞器接口
- [JdkConditionBlocker.java](concurrent-design-patterns-guarded-suspension/src/main/java/com/coderlee/guarded/suspension/lock/alarm/blocker/JdkConditionBlocker.java) 基于JDK Condition的阻塞器实现
**核心思想**：
- 当报警代理未连接到服务器时，发送报警请求的线程会被挂起等待
- 一旦连接建立，所有等待的线程会被唤醒并继续执行
- 通过 [GuardedAction.java](concurrent-design-patterns-guarded-suspension/src/main/java/com/coderlee/guarded/suspension/lock/alarm/action/GuardedAction.java) 封装受保护的动作
- 使用 [Predicate.java](concurrent-design-patterns-guarded-suspension/src/main/java/com/coderlee/guarded/suspension/lock/alarm/predicate/Predicate.java) 定义保护条件
##### 2.2 缓冲区示例
在 `buffer` 包中展示了基于Lock和Condition的缓冲区实现：
- [RequestCacheBuffer.java](concurrent-design-patterns-guarded-suspension/src/main/java/com/coderlee/guarded/suspension/lock/buffer/RequestCacheBuffer.java) 请求缓存缓冲区
- [Request.java](concurrent-design-patterns-guarded-suspension/src/main/java/com/coderlee/guarded/suspension/lock/buffer/Request.java) 请求对象
**实现要点**：
- 使用 `ReentrantLock` 和 `Condition` 实现线程协调
- 当缓冲区为空时，消费者线程被挂起等待
- 当缓冲区满时，生产者线程被挂起等待
- 通过 `notEmpty` 和 `notFull` 两个条件变量协调生产者和消费者
#### 3. 基础实现示例

[demo](concurrent-design-patterns-guarded-suspension/src/main/java/com/coderlee/guarded/suspension/lock/demo) 包中提供了基础实现：
- [GuardedQueue.java](concurrent-design-patterns-guarded-suspension/src/main/java/com/coderlee/guarded/suspension/lock/demo/GuardedQueue.java) 基于 synchronized/wait/notify 的队列实现
- [GuardedQueueApp.java](concurrent-design-patterns-guarded-suspension/src/main/java/com/coderlee/guarded/suspension/lock/demo/GuardedQueueApp.java) 测试应用

#### 4. 模式优势与适用场景
- 优势
  - 避免忙等待，提高系统资源利用率
  - 简化线程间协调逻辑
  - 提供清晰的条件等待和通知机制
- 适用场景
  - 线程间需要协调执行顺序的场景
  - 条件不满足时需要等待而非立即失败的场景
  - 生产者-消费者模式的实现
  - 异步操作结果等待场景

#### 5. 与其他模式的关系

- 与生产者-消费者模式结合使用
- 是观察者模式在并发场景下的实现基础
- 与Future模式配合实现异步结果获取

### 第三章 两阶段终止模式 (Two-Phase Termination Pattern)
#### 1. 模式概述
两阶段终止模式是一种并发设计模式，用于优雅地终止线程。它将线程终止过程分为两个阶段：
1. **准备阶段**：发出终止请求，设置终止标志
2. **执行阶段**：线程检测到终止标志后，清理资源并安全退出
这种模式避免了强制终止线程可能导致的数据不一致问题，确保线程能够完成必要的清理工作。
#### 2. 核心组件分析
##### 2.1 基础框架 (concurrent-design-patterns-thread 模块)
基础框架提供了两阶段终止模式的核心实现：
- [TerminationToken](concurrent-design-patterns-thread/src/main/java/com/coderlee/concurrent/design/thread/TerminationToken.java): 终止令牌类，维护终止状态和任务计数
    - `toShutdown` 终止标志
    - `noExecuteTaskCount` 未执行任务计数器
    - `coordinatedThreads` 协调线程队列
- [Termination](concurrent-design-patterns-thread/src/main/java/com/coderlee/concurrent/design/thread/Termination.java): 终止接口，定义terminate方法
- [AbstractTerminationThread](concurrent-design-patterns-thread/src/main/java/com/coderlee/concurrent/design/thread/AbstractTerminationThread.java): 抽象终止线程类
    - 实现了标准的两阶段终止流程
    - 提供了 `doRun()`、`doTerminate()`、`doCleanup()`等模板方法供子类实现
##### 2.2 应用实现 (concurrent-design-patterns-two-phase-termination 模块)
基于基础框架，提供了实际的应用示例：
- [AlarmManager](concurrent-design-patterns-two-phase-termination/src/main/java/com/coderlee/concurrent/design/two/phase/alarm/right/AlarmManager.java): 告警管理器，单例模式
- [AlarmSendingThread](concurrent-design-patterns-two-phase-termination/src/main/java/com/coderlee/concurrent/design/two/phase/alarm/right/AlarmSendingThread.java): 告警发送线程，继承自[AbstractTerminationThread](concurrent-design-patterns-thread/src/main/java/com/coderlee/concurrent/design/thread/AbstractTerminationThread.java)
- [AlarmInfo](concurrent-design-patterns-two-phase-termination/src/main/java/com/coderlee/concurrent/design/two/phase/alarm/right/AlarmInfo.java): 告警信息实体类
- [AlarmType](concurrent-design-patterns-two-phase-termination/src/main/java/com/coderlee/concurrent/design/two/phase/alarm/right/AlarmType.java): 告警类型枚举

#### 3. 实现细节
##### 3.1 正确实现 (right包)
[AlarmSendingThread](concurrent-design-patterns-two-phase-termination/src/main/java/com/coderlee/concurrent/design/two/phase/alarm/right/AlarmSendingThread.java)继承自[AbstractTerminationThread](concurrent-design-patterns-thread/src/main/java/com/coderlee/concurrent/design/thread/AbstractTerminationThread.java)，实现了标准的两阶段终止：
1. **第一阶段**: 调用 `AlarmManager.shutdown()` 方法
   ```java
   public synchronized void shutdown() {
       if (shutdownRequested) {
           throw new IllegalStateException("已经调用了shutdown方法....");
       }
       alarmSendingThread.terminate(); // 设置终止标志
       shutdownRequested = true;
   }
   ```
2. **第二阶段**: 线程检测终止标志并安全退出
   ```java
   @Override
   protected void doRun() throws InterruptedException {
       // 检测终止标志
       if (terminationToken.isToShutdown() && terminationToken.noExecuteTaskCount.get() <= 0) {
           // 完成清理工作后退出
           break;
       }
       // 执行具体业务逻辑
   }
   ```
##### 3.2 错误实现对比 (wrong包)
错误实现展示了不恰当的线程终止方式：
- 直接使用`Thread.interrupt()`强制中断
- 线程不能优雅地完成正在进行的任务
- 缺乏统一的终止管理机制
#### 4. 核心优势
1. **优雅终止**: 线程有机会完成清理工作，避免数据损坏
2. **统一管理**: 通过[TerminationToken](concurrent-design-patterns-thread/src/main/java/com/coderlee/concurrent/design/thread/TerminationToken.java)统一管理多个协调线程
3. **任务完整性**: 确保已接收的任务得到处理
4. **扩展性强**: 基于抽象类的设计易于扩展

### 第四章 承诺模式 (Promise Pattern)

#### 1. 模式概述
承诺模式（Promise Pattern）是一种异步编程设计模式，用于处理异步操作及其结果。Promise代表了一个尚未完成但将来会完成的操作，它可以使得异步代码更加易读和易管理。

#### 2. 核心概念
- **Promise**: 代表一个异步操作的最终结果
- **Future**: 用于获取异步操作结果的占位符
- **异步执行**: 将耗时操作放在后台线程执行，不阻塞主线程

#### 3. 应用场景示例

##### 3.1 用户支付后奖励发放示例
模拟用户支付成功后需要发送积分和优惠券的场景：

- 错误实现 [PromiseWrongTest.java](concurrent-design-patterns-promise/src/main/java/com/coderlee/concurrent/design/promise/wrong/PromiseWrongTest.java)
    - 虽然创建了线程处理积分发送，但使用 `Thread.join()` 阻塞主线程
    - 导致优惠券发送必须等待积分发送完成后才能开始
    - 实际上是串行执行，没有发挥并发优势

- 正确实现 [PromiseRightTest.java](concurrent-design-patterns-promise/src/main/java/com/coderlee/concurrent/design/promise/right/PromiseRightTest.java)
    - 使用 [Promisor.java](concurrent-design-patterns-promise/src/main/java/com/coderlee/concurrent/design/promise/right/Promisor.java) 创建异步任务
    - 通过 `FutureTask` 实现异步执行
    - 积分发送和优惠券发送可以并行执行，显著提升效率

##### 3.2 文件同步示例
模拟文件同步系统中连接服务器和扫描本地文件的并发处理：

- [FileSyncerPromisor.java](concurrent-design-patterns-promise/src/main/java/com/coderlee/concurrent/design/promise/sync/FileSyncerPromisor.java)
    - 异步初始化 [FileSyncer](concurrent-design-patterns-promise/src/main/java/com/coderlee/concurrent/design/promise/sync/FileSyncer.java#L5-L30) 实例并建立服务器连接
    - 返回 `Future<FileSyncer>` 对象供后续使用
- [FileSyncerTask.java](concurrent-design-patterns-promise/src/main/java/com/coderlee/concurrent/design/promise/sync/FileSyncerTask.java)
    - 并发执行文件扫描和服务器连接建立
    - 提升整体处理效率

#### 4. 核心组件分析

- [Promisor.java](concurrent-design-patterns-promise/src/main/java/com/coderlee/concurrent/design/promise/right/Promisor.java)
    - 使用 `FutureTask` 包装异步任务
    - 在新线程中执行任务
    - 返回 `Future` 对象供调用方使用

- [FileSyncerPromisor.java](concurrent-design-patterns-promise/src/main/java/com/coderlee/concurrent/design/promise/sync/FileSyncerPromisor.java)
    - 单例模式实现
    - 异步初始化复杂资源
    - 使用专用线程池执行任务

#### 5. 模式优势与适用场景

- **优势**
    - 提高系统响应性，避免长时间阻塞
    - 支持并发执行多个独立任务
    - 简化异步编程模型
    - 提供统一的异步结果处理接口

- **适用场景**
    - 耗时的I/O操作（网络请求、文件读写等）
    - 需要并行处理多个独立任务的场景
    - 需要延迟初始化复杂资源的情况
    - 异步处理用户请求以提升用户体验

### 第5章 生产者消费者模式
生产者消费者模式是一种经典的并发设计模式，用于解决生产者和消费者之间的速度不匹配问题。该模式通过一个缓冲区（通常称为队列）来平衡生产者和消费者的处理速度，实现解耦和提高系统吞吐量。

#### 1. 模式概述

生产者消费者模式的核心思想是将生产数据和消费数据的过程分离，通过一个共享的缓冲区来进行数据交换。生产者负责生成数据并放入缓冲区，消费者则从缓冲区取出数据进行处理。

### 2. 错误实现示例 (wrong包)

在 `wrong` 包中展示了没有正确使用生产者消费者模式的实现：

- [PCWrongTest.java](concurrent-design-patterns-producer-comsumer/src/main/java/com/coderlee/concurrent/design/pc/wrong/PCWrongTest.java) 展示了串行执行的场景，各个服务(DBService、UploadService、IndexService)依次执行，没有并发处理。

- [PCWrongTest2.java](concurrent-design-patterns-producer-comsumer/src/main/java/com/coderlee/concurrent/design/pc/wrong/PCWrongTest2.java) 和 [PCWrongTest3.java](concurrent-design-patterns-producer-comsumer/src/main/java/com/coderlee/concurrent/design/pc/wrong/PCWrongTest3.java) 虽然尝试使用线程池来并行处理部分任务(IndexService)，但仍存在以下问题:
    - 没有真正的缓冲区来存储待处理的任务
    - 生产者和消费者之间仍然是紧耦合
    - 无法平滑处理生产者和消费者速度不匹配的问题

### 3. 正确实现示例 (right包)

在 `right` 包中展示了正确的生产者消费者模式实现：

#### 3.1 核心组件

- [Channel.java](concurrent-design-patterns-producer-comsumer/src/main/java/com/coderlee/concurrent/design/pc/right/Channel.java) 接口定义了通道的基本操作：`put()` 和 `take()`
- [BlockingQueueChannel.java](concurrent-design-patterns-producer-comsumer/src/main/java/com/coderlee/concurrent/design/pc/right/BlockingQueueChannel.java) 是 `Channel` 接口的具体实现，使用 `BlockingQueue` 作为底层存储
- [FileInfo.java](concurrent-design-patterns-producer-comsumer/src/main/java/com/coderlee/concurrent/design/pc/right/FileInfo.java) 是传输的数据对象，表示文件信息
- [FileProcessor.java](concurrent-design-patterns-producer-comsumer/src/main/java/com/coderlee/concurrent/design/pc/right/FileProcessor.java) 充当生产者角色，负责将文件信息放入通道
- [FileIndexThread.java](concurrent-design-patterns-producer-comsumer/src/main/java/com/coderlee/concurrent/design/pc/right/FileIndexThread.java) 充当消费者角色，从通道中取出文件信息进行处理

#### 3.2 工作流程

1. [FileProcessor](concurrent-design-patterns-producer-comsumer/src/main/java/com/coderlee/concurrent/design/pc/right/FileProcessor.java) 作为生产者，通过 `uploadFile()` 方法将 `FileInfo` 对象放入 `Channel`
2. [FileIndexThread](concurrent-design-patterns-producer-comsumer/src/main/java/com/coderlee/concurrent/design/pc/right/FileIndexThread.java) 作为消费者，在后台持续从 `Channel` 中取出 `FileInfo` 对象进行处理
3. [BlockingQueueChannel](concurrent-design-patterns-producer-comsumer/src/main/java/com/coderlee/concurrent/design/pc/right/BlockingQueueChannel.java) 使用 `ArrayBlockingQueue` 作为缓冲区，自动处理生产者和消费者之间的同步问题

#### 3.3 测试示例

[FileTest.java](concurrent-design-patterns-producer-comsumer/src/main/java/com/coderlee/concurrent/design/pc/right/FileTest.java) 演示了完整的生产者消费者流程：
1. 启动消费者线程
2. 生产者连续上传两个文件
3. 消费者异步处理这些文件
4. 最终关闭消费者线程

### 4. 模式优势

1. **解耦**：生产者和消费者之间没有直接依赖关系
2. **平衡速度差异**：通过缓冲区平衡生产者和消费者的处理速度
3. **提高系统吞吐量**：生产者和消费者可以并行工作
4. **支持并发**：多个生产者和消费者可以同时工作
5. **平滑流量削峰**：在突发流量情况下，通过缓冲区平滑处理

### 5. 适用场景

- 处理速度不匹配的生产者和消费者场景
- 需要解耦生产数据和消费数据的系统
- 需要缓冲大量数据的系统
- 需要支持并发生产和消费的场景

### 第6章 主动对象模式
#### 1. 模式概述
主动对象模式（Active Object Pattern）是一种并发设计模式，它将方法调用与方法执行分离，通过异步方式处理请求。该模式封装了控制流，使得对象的方法调用和执行发生在不同的线程中，提高了系统的并发性能和响应性。

#### 2. 核心概念
- **方法请求对象（Method Request）**: 封装了方法调用的参数和目标对象
- **调度器（Scheduler）**: 管理方法请求队列和执行线程
- **主动对象（Active Object）**: 包含自己的控制线程和服务例程的对象
- **代理（Proxy）**: 向客户端提供接口的对象

#### 3. 应用场景示例

##### 3.1 商品短链接生成与请求存储示例
模拟电商系统中生成商品短链接并存储请求的场景：

- **错误实现 (wrong包)**:
    - 所有操作都在同一线程中顺序执行
    - 生成短链接、保存商品信息、存储URL映射等操作依次进行
    - 如果任何一个环节出现延迟，整个流程都会被阻塞
    - 无法充分利用系统资源

- **正确实现 (right包)**:
    - 使用主动对象模式将耗时的存储操作异步化
    - [URLServiceImpl](concurrent-design-patterns-active-object/src/main/java/com/coderlee/concurrent/design/active/object/right/URLServiceImpl.java) 在短链接生成失败时，通过 [ProxyRequestStore](concurrent-design-patterns-active-object/src/main/java/com/coderlee/concurrent/design/active/object/right/ProxyRequestStore.java) 异步存储请求
    - [ProxyRequestStore](concurrent-design-patterns-active-object/src/main/java/com/coderlee/concurrent/design/active/object/right/ProxyRequestStore.java) 使用线程池异步执行 [DBRequestStore](concurrent-design-patterns-active-object/src/main/java/com/coderlee/concurrent/design/active/object/right/DBRequestStore.java) 的存储操作
    - 主线程不需要等待耗时的存储操作完成即可继续执行

#### 4. 核心组件分析

- [GoodsRequest](concurrent-design-patterns-active-object/src/main/java/com/coderlee/concurrent/design/active/object/right/GoodsRequest.java)
    - 封装客户端请求参数的数据对象
    - 作为方法请求对象(Method Request)的载体

- [RequestStore](concurrent-design-patterns-active-object/src/main/java/com/coderlee/concurrent/design/active/object/right/RequestStore.java) 接口
    - 定义了存储请求的方法规范

- [DBRequestStore](concurrent-design-patterns-active-object/src/main/java/com/coderlee/concurrent/design/active/object/right/DBRequestStore.java)
    - 实现 [RequestStore](concurrent-design-patterns-active-object/src/main/java/com/coderlee/concurrent/design/active/object/right/RequestStore.java) 接口，模拟实际的数据库存储操作
    - 代表主动对象模式中的服务例程(Service Routine)

- [ProxyRequestStore](concurrent-design-patterns-active-object/src/main/java/com/coderlee/concurrent/design/active/object/right/ProxyRequestStore.java)
    - 实现代理模式，向客户端提供统一接口
    - 内部使用线程池实现调度器(Scheduler)功能
    - 将方法请求封装为任务并提交给线程池执行

- [URLServiceImpl](concurrent-design-patterns-active-object/src/main/java/com/coderlee/concurrent/design/active/object/right/URLServiceImpl.java)
    - 模拟业务逻辑处理，演示主动对象模式的使用场景
    - 在处理失败时触发异步存储操作

#### 5. 工作流程

1. 客户端创建 `GoodsRequest` 对象并调用 `URLService.getShortUrlByGoodsRequest()` 方法
2. `URLServiceImpl` 尝试生成短链接，如果失败则调用 `ProxyRequestStore.getInstance().flush()` 异步存储请求
3. `ProxyRequestStore` 将存储请求封装为Callable任务并提交给线程池
4. 线程池中的工作线程执行 `DBRequestStore.flush()` 方法，完成实际的存储操作
5. 主线程无需等待存储操作完成即可继续执行其他任务

#### 6. 模式优势与适用场景

##### 优势
- **提高响应性**: 方法调用立即返回，实际执行在后台进行
- **并发处理**: 多个请求可以并发执行，提高系统吞吐量
- **资源管理**: 通过调度器统一管理系统资源
- **解耦**: 调用方与执行方完全解耦

##### 适用场景
- 需要异步处理耗时操作的场景
- 需要提高系统并发性能的应用
- 需要解耦方法调用与执行的系统
- 处理大量并发请求的服务端应用

### # 第7章 线程池模式
#### 1. 模式概述
线程池模式是一种并发设计模式，用于管理和复用线程资源。通过预先创建一组线程并将其保存在池中，避免了频繁创建和销毁线程的开销，提高了系统性能和资源利用率。
#### 2. 核心应用场景
##### 2.1 消息推送系统示例
项目通过消息推送场景展示了线程池模式的应用：
- [MessageService](concurrent-design-patterns-thread-pool/src/main/java/com/coderlee/concurrent/design/thread/pool/common/MessageService.java) 接口定义了消息发送的标准操作
- [MessageServiceImpl](concurrent-design-patterns-thread-pool/src/main/java/com/coderlee/concurrent/design/thread/pool/common/MessageServiceImpl.java) 实现了具体的发送逻辑，包括模拟耗时操作
- 展示了错误和正确的线程池使用方式
#### 3. 实现分析
##### 3.1 错误实现 (`wrong` 包)
###### 3.1.1 直接创建线程方式 ([MessageTest.java](concurrent-design-patterns-thread-pool/src/main/java/com/coderlee/concurrent/design/thread/pool/wrong/MessageTest.java))
**存在的问题**：
- 每次都需要创建新线程，消耗系统资源
- 无法控制并发线程数量，可能导致系统资源耗尽
- 线程生命周期管理困难
###### 3.1.2 不合适的线程池 ([MessageWrongThreadPoolTest.java](concurrent-design-patterns-thread-pool/src/main/java/com/coderlee/concurrent/design/thread/pool/wrong/MessageWrongThreadPoolTest.java))
**存在的问题**：
- 使用 `Executors.newCachedThreadPool()` 创建的线程池没有上限
- 在高并发场景下可能创建过多线程，导致系统资源耗尽
- 缺乏对线程池参数的精细控制
##### 3.2 正确实现 (`right` 包)
###### 3.2.1 合理配置的线程池 ([MessageRightThreadPool.java](concurrent-design-patterns-thread-pool/src/main/java/com/coderlee/concurrent/design/thread/pool/right/MessageRightThreadPool.java))
**优势**：
- 合理设置核心线程数和最大线程数
- 使用有界队列防止内存溢出
- 自定义线程命名便于调试
- 设置拒绝策略处理过载情况
- 添加关闭钩子优雅关闭线程池
#### 4. 自定义线程池实现
##### 4.1 简单线程池实现 ([ThreadPool.java](concurrent-design-patterns-thread-pool/src/main/java/com/coderlee/concurrent/design/thread/pool/threadpool/ThreadPool.java))
项目还提供了自定义线程池的实现：
- 使用 `BlockingQueue` 作为工作队列
- 内部维护工作线程列表
- 实现基本的任务提交和执行机制
##### 4.2 核心组件
- [ThreadPool](concurrent-design-patterns-thread-pool/src/main/java/com/coderlee/concurrent/design/thread/pool/common/ThreadPool.java): 自定义线程池类
- [WorkThread](concurrent-design-patterns-thread-pool/src/main/java/com/coderlee/concurrent/design/thread/pool/common/ThreadPool.java): 工作线程类，负责从队列中取任务执行
- [ThreadPoolTest](concurrent-design-patterns-thread-pool/src/main/java/com/coderlee/concurrent/design/thread/pool/common/ThreadPoolTest.java): 测试类，验证线程池功能
#### 5. 模式优势与适用场景
##### 5.1 优势
- **资源复用**: 减少线程创建和销毁的开销
- **性能提升**: 避免频繁创建线程的性能损耗
- **控制并发**: 可以有效控制系统并发线程数量
- **管理便利**: 提供统一的线程管理和监控机制
- **提高响应性**: 任务提交后可以立即执行，无需等待线程创建
##### 5.2 适用场景
- 高并发处理大量短期异步任务
- 需要控制并发线程数量的场景
- 执行大量相似性质任务的应用
- 需要提高系统资源利用率的场合
- 对响应时间有要求的服务端应用
#### 6. 最佳实践
1. **合理配置参数**:
    - 根据业务特点设置合适的核心线程数和最大线程数
    - 选择适当的队列类型和容量
    - 设置合理的拒绝策略
2. **优雅关闭**:
    - 使用 `shutdown()` 或 `shutdownNow()` 方法关闭线程池
    - 添加关闭钩子确保程序退出时正确释放资源
3. **监控和调优**:
    - 监控线程池运行状态
    - 根据实际负载调整参数配置
4. **避免常见陷阱**:
    - 不要使用 `Executors` 工具类创建无界线程池
    - 注意任务执行异常的处理
    - 避免在任务中执行阻塞操作影响线程池性能

### 第8章 线程特有存储模式(Thread Specific Storage Pattern)
#### 1. 模式概述
线程特有存储模式是一种并发设计模式，它为每个线程提供独立的数据存储空间，确保线程间数据隔离。该模式通过`ThreadLocal`实现，使得每个线程都有自己独立的变量副本，避免了多线程环境下的数据竞争问题。
#### 2. 核心应用场景
##### 2.1 SimpleDateFormat线程安全问题解决
在多线程环境中，`SimpleDateFormat`不是线程安全的，直接共享使用会导致解析错误。通过ThreadLocal为每个线程提供独立的`SimpleDateFormat`实例：
- 错误实现 [WrongSimpleDateFormat.java](concurrent-design-patterns-threadlocal/src/main/java/com/coderlee/concurrent/design/threadlocal/wrong/WrongSimpleDateFormat.java):
    - 多个线程共享同一个`SimpleDateFormat`实例
    - 在高并发环境下会出现日期解析错误
- 正确实现:
    - [RightSimpleDateFormat.java](concurrent-design-patterns-threadlocal/src/main/java/com/coderlee/concurrent/design/threadlocal/right/RightSimpleDateFormat.java) 使用`ThreadLocal.withInitial()`初始化每个线程的`SimpleDateFormat`
    - [RightSimpleDateFormat2.java](concurrent-design-patterns-threadlocal/src/main/java/com/coderlee/concurrent/design/threadlocal/right/RightSimpleDateFormat2.java) 通过懒加载方式为每个线程创建`SimpleDateFormat`
##### 2.2 线程上下文数据传递
在复杂的业务处理中，经常需要在线程执行过程中传递上下文数据：
- 错误实现 [ThreadLocalWrongTest.java](concurrent-design-patterns-threadlocal/src/main/java/com/coderlee/concurrent/design/threadlocal/wrong/ThreadLocalWrongTest.java):
    - 未清理ThreadLocal数据，可能导致内存泄漏
    - 在线程池环境下可能出现数据污染
- 正确实现 [ThreadLocalRightTest.java](concurrent-design-patterns-threadlocal/src/main/java/com/coderlee/concurrent/design/threadlocal/right/ThreadLocalRightTest.java):
    - 在finally块中调用`THREAD_LOCAL.remove()`清理数据
    - 确保线程复用时不会出现数据混淆
#### 3. 核心组件分析
##### 3.1 ThreadLocal基础使用
[demo/ThreadLocalTest.java](concurrent-design-patterns-threadlocal/src/main/java/com/coderlee/concurrent/design/threadlocal/demo/ThreadLocalTest.java) 展示了ThreadLocal的基本用法：
- 每个线程设置和获取自己独立的数据
- 线程间数据完全隔离
##### 3.2 ThreadLocal关键方法
- `withInitial()`: 提供初始值的工厂方法
- `get()`: 获取当前线程的变量值
- `set()`: 设置当前线程的变量值
- `remove()`: 清理当前线程的变量值，防止内存泄漏
#### 4. 模式优势与适用场景
##### 4.1 优势
- **线程安全**: 每个线程拥有独立的数据副本，天然线程安全
- **数据隔离**: 线程间数据完全隔离，避免相互干扰
- **简化编程**: 无需显式同步机制就能保证线程安全
- **性能良好**: 避免了锁竞争带来的性能损耗
##### 4.2 适用场景
- 需要在线程内共享数据但线程间隔离的场景
- 非线程安全对象在多线程环境中的使用
- 上下文信息在线程执行链路中的传递
- 日志追踪ID、用户身份信息等跨方法传递
#### 5. 最佳实践与注意事项
##### 5.1 必须清理ThreadLocal数据
在线程池环境下，线程会被复用，如果不清理ThreadLocal中的数据，可能导致：
- 数据泄露到下一个任务
- 内存泄漏问题
应在适当的时候（如finally块中）调用`remove()`方法清理数据。
##### 5.2 合理使用初始值
可以通过`withInitial()`方法提供默认值，或者采用懒加载方式初始化，避免不必要的对象创建。
##### 5.3 避免存储大对象
ThreadLocal中存储的对象生命周期与线程绑定，在Web应用等长生命周期线程中应避免存储大对象，防止内存泄漏。
#### 6. 与其他模式的关系
- 与不可变模式结合使用，可以进一步增强线程安全性
- 是实现上下文传递的重要手段，常用于分布式追踪系统
- 与线程池模式配合使用时需特别注意数据清理问题

### 第9章 串行线程封闭模式 (Serial Thread Confinement Pattern)
#### 1. 模式概述
串行线程封闭模式是一种并发设计模式，它通过将多个线程的任务交给单一专用线程来顺序执行，从而避免了多线程并发访问共享资源时的同步问题。该模式利用线程封闭的思想，确保同一时刻只有一个线程访问特定资源，从根本上消除线程安全问题。
#### 2. 核心应用场景
##### 2.1 文件下载服务示例
在文件下载场景中，需要确保文件客户端的线程安全性，同时高效处理多个下载请求：
- **错误实现** ([wrong包](concurrent-design-patterns-thread-close/src/main/java/com/coderlee/concurrent/design/thread/close/wrong)):
    - [FileServiceImpl.java](concurrent-design-patterns-thread-close/src/main/java/com/coderlee/concurrent/design/thread/close/wrong/FileServiceImpl.java) 中每个线程直接调用 [FileClient](concurrent-design-patterns-thread-close/src/main/java/com/coderlee/concurrent/design/thread/close/common/FileClient.java) 的方法
    - 由于 [FileClient](concurrent-design-patterns-thread-close/src/main/java/com/coderlee/concurrent/design/thread/close/common/FileClient.java) 不是线程安全的，多线程并发访问会导致潜在问题
    - [FileWrongTest.java](concurrent-design-patterns-thread-close/src/main/java/com/coderlee/concurrent/design/thread/close/wrong/FileWrongTest.java) 和 [FileThreadPoolTest.java](concurrent-design-patterns-thread-close/src/main/java/com/coderlee/concurrent/design/thread/close/wrong/FileThreadPoolTest.java) 展示了直接多线程访问带来的风险
- **正确实现** ([right包](concurrent-design-patterns-thread-close/src/main/java/com/coderlee/concurrent/design/thread/close/right)):
    - [WorkThread.java](concurrent-design-patterns-thread-close/src/main/java/com/coderlee/concurrent/design/thread/close/right/WorkThread.java) 继承自 [AbstractTerminationThread](concurrent-design-patterns-thread/src/main/java/com/coderlee/concurrent/design/thread/AbstractTerminationThread.java)，作为专门处理文件下载的工作者线程
    - 使用 [ArrayBlockingQueue](concurrent-design-patterns-thread-close/src/main/java/com/coderlee/concurrent/design/thread/close/right/WorkThread.java) 作为任务队列，实现生产者-消费者模式
    - [FileServiceImpl.java](concurrent-design-patterns-thread-close/src/main/java/com/coderlee/concurrent/design/thread/close/right/FileServiceImpl.java) 将下载请求放入队列，由专用线程顺序处理
    - [FileRightTest.java](concurrent-design-patterns-thread-close/src/main/java/com/coderlee/concurrent/design/thread/close/right/FileRightTest.java) 展示了正确的串行线程封闭使用方式
#### 3. 核心组件分析
##### 3.1 共同组件
- [FileClient.java](concurrent-design-patterns-thread-close/src/main/java/com/coderlee/concurrent/design/thread/close/common/FileClient.java):
    - 模拟文件客户端，包含初始化和下载文件的功能
    - 本身不是线程安全的，需要通过串行线程封闭模式来保证线程安全
##### 3.2 错误实现组件
- [FileService.java](concurrent-design-patterns-thread-close/src/main/java/com/coderlee/concurrent/design/thread/close/wrong/FileService.java):
    - 定义文件服务接口
- [FileServiceImpl.java](concurrent-design-patterns-thread-close/src/main/java/com/coderlee/concurrent/design/thread/close/wrong/FileServiceImpl.java):
    - 直接调用 [FileClient](concurrent-design-patterns-thread-close/src/main/java/com/coderlee/concurrent/design/thread/close/common/FileClient.java)，存在线程安全隐患
##### 3.3 正确实现组件
- [FileService.java](concurrent-design-patterns-thread-close/src/main/java/com/coderlee/concurrent/design/thread/close/right/FileService.java):
    - 扩展了文件服务接口，添加了初始化和关闭方法
- [FileServiceImpl.java](concurrent-design-patterns-thread-close/src/main/java/com/coderlee/concurrent/design/thread/close/right/FileServiceImpl.java):
    - 使用 [WorkThread](concurrent-design-patterns-thread-close/src/main/java/com/coderlee/concurrent/design/thread/close/right/WorkThread.java) 处理下载请求
    - 实现了两阶段终止模式，可以优雅关闭
- [WorkThread.java](concurrent-design-patterns-thread-close/src/main/java/com/coderlee/concurrent/design/thread/close/right/WorkThread.java):
    - 继承自 [AbstractTerminationThread](concurrent-design-patterns-thread/src/main/java/com/coderlee/concurrent/design/thread/AbstractTerminationThread.java)
    - 使用阻塞队列作为任务缓冲区
    - 专门负责顺序处理文件下载任务
- [FileRightTest.java](concurrent-design-patterns-thread-close/src/main/java/com/coderlee/concurrent/design/thread/close/right/FileRightTest.java):
    - 测试类，展示如何正确使用串行线程封闭模式
#### 4. 工作流程
1. 多个线程通过 [FileService.downloadFile()](concurrent-design-patterns-thread-close/src/main/java/com/coderlee/concurrent/design/thread/close/right/FileService.java) 提交下载任务
2. [FileServiceImpl](concurrent-design-patterns-thread-close/src/main/java/com/coderlee/concurrent/design/thread/close/right/FileServiceImpl.java) 将任务放入 [WorkThread](concurrent-design-patterns-thread-close/src/main/java/com/coderlee/concurrent/design/thread/close/right/WorkThread.java) 的阻塞队列
3. 专用的 [WorkThread](concurrent-design-patterns-thread-close/src/main/java/com/coderlee/concurrent/design/thread/close/right/WorkThread.java) 从队列中取出任务顺序执行
4. 通过 [FileService.shutdown()](concurrent-design-patterns-thread-close/src/main/java/com/coderlee/concurrent/design/thread/close/right/FileService.java) 实现优雅关闭
#### 5. 模式优势与适用场景
##### 5.1 优势
- **线程安全**: 通过线程封闭消除并发访问问题
- **简化设计**: 无需复杂的同步机制
- **有序处理**: 任务按照提交顺序执行
- **资源可控**: 可以控制并发线程数量
- **易于管理**: 结合两阶段终止模式实现优雅关闭
##### 5.2 适用场景
- 处理非线程安全组件的并发访问
- 需要保证任务执行顺序的场景
- 需要限制并发资源使用的场合
- 对共享资源进行串行化访问的需求

### 第10章 主仆模式 (Master-Slave Pattern)
#### 1. 模式概述
主仆模式是一种并发设计模式，其中一个主节点(Master)负责分配任务并协调多个工作节点(Slave)的执行。该模式通过将复杂任务分解为多个子任务并行处理，提高系统处理能力和效率。
#### 2. 核心应用场景
##### 2.1 商品热度统计示例
项目通过分析日志文件统计商品访问热度的场景展示了主仆模式的应用：
- **错误实现** ([wrong包](concurrent-design-patterns-master-slave/src/main/java/com/coderlee/concurrent/design/master/slave/wrong)):
    - [FileService.java](concurrent-design-patterns-master-slave/src/main/java/com/coderlee/concurrent/design/master/slave/wrong/FileService.java) 定义了文件服务接口
    - [FileServiceImpl.java](concurrent-design-patterns-master-slave/src/main/java/com/coderlee/concurrent/design/master/slave/wrong/FileServiceImpl.java) 串行处理文件，效率低下
    - [FileTest.java](concurrent-design-patterns-master-slave/src/main/java/com/coderlee/concurrent/design/master/slave/wrong/FileTest.java) 测试类，演示串行处理方式
- **正确实现** ([right包](concurrent-design-patterns-master-slave/src/main/java/com/coderlee/concurrent/design/master/slave/right)):
    - 采用主仆模式并行处理日志文件，显著提高处理效率
#### 3. 核心组件分析
##### 3.1 数据模型
- [HotGoodsLog.java](concurrent-design-patterns-master-slave/src/main/java/com/coderlee/concurrent/design/master/slave/right/HotGoodsLog.java):
    - 商品热度日志实体类，封装单条商品访问记录
    - 包含商品ID(goodsId)和访问时间戳(timeStamp)字段
##### 3.2 核心实现组件
- [HotGoodsEnumeration.java](concurrent-design-patterns-master-slave/src/main/java/com/coderlee/concurrent/design/master/slave/right/HotGoodsEnumeration.java):
    - 自定义实现的`Enumeration<InputStream>`接口
    - 将一组日志文件名转换为对应的输入流序列
    - 用于顺序读取多个日志文件的内容
- [HotGoodsMaster.java](concurrent-design-patterns-master-slave/src/main/java/com/coderlee/concurrent/design/master/slave/right/HotGoodsMaster.java):
    - Master节点，负责管理多个工作节点([HotGoodsSlave](concurrent-design-patterns-master-slave/src/main/java/com/coderlee/concurrent/design/master/slave/right/HotGoodsLog.java))
    - 协调工作节点对日志数据的分析与汇总
    - 主要职责:
        - 创建并启动Slave节点
        - 将日志文件分发给不同的工作节点处理
        - 收集并汇总各Slave节点的处理结果
- [HotGoodsSlave.java](concurrent-design-patterns-master-slave/src/main/java/com/coderlee/concurrent/design/master/slave/right/HotGoodsSlave.java):
    - Slave节点，用于接收并执行由Master下发的日志解析任务
    - 继承自`AbstractTerminationThread`，支持两阶段终止模式
    - 主要职责:
        - 从阻塞队列中获取待处理的日志文件
        - 解析日志文件内容，统计商品访问次数
        - 将统计结果更新到共享的`ConcurrentMap`中
- [HotGoodsTest.java](concurrent-design-patterns-master-slave/src/main/java/com/coderlee/concurrent/design/master/slave/right/HotGoodsTest.java):
    - 测试类，演示如何使用 `HotGoodsMaster` 和 `HotGoodsSlave` 分析日志文件
    - 输出商品热度排名结果
#### 4. 工作流程
1. `HotGoodsTest` 创建 `HotGoodsMaster` 实例并加载待分析的日志文件清单
2. `HotGoodsMaster` 创建指定数量的 `HotGoodsSlave` 工作节点并启动
3. `HotGoodsMaster` 将日志文件按策略分发给各个 `HotGoodsSlave` 
4. 每个 `HotGoodsSlave` 从任务队列中获取日志文件并解析
5. `HotGoodsSlave` 将解析结果更新到共享的统计映射表中
6. `HotGoodsMaster` 收集所有任务完成信号并返回最终统计结果
#### 5. 模式优势与适用场景
##### 5.1 优势
- **并行处理**: 将大任务分解为多个小任务并行处理，提高处理效率
- **负载均衡**: Master可以根据策略合理分配任务给各个Slave
- **易于扩展**: 可以通过增加Slave节点来提高处理能力
- **容错性**: 单个Slave节点故障不影响整体任务执行
- **资源管理**: 统一管理和协调各个工作节点的资源使用
##### 5.2 适用场景
- 大量数据的并行处理任务
- CPU密集型计算任务的分布式处理
- 日志分析、数据挖掘等批处理场景
- 需要将复杂任务分解为子任务的场合
- 对处理能力有横向扩展需求的应用

