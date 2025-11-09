# concurrent-design-patterns 

## 项目简介
《实战高并发设计模式>的配套源码，包含所有示例代码。


## 目录结构

## 目录结构
```
concurrent-design-patterns
    ├── concurrent-design-patterns-immutable  # 第1章 不可变模式
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