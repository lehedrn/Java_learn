# art concurrent book Java并发编程的艺术

## 项目简介
《Java并发编程的艺术》的配套源码，包含所有示例代码。

## 目录结构
```
  ├── src 
  |   |── main 
  |   |   |── java 
  |   |   |   |── com 
  |   |   |   |   |── coderlee 
  |   |   |   |   |   |── artconcurrentbook
  |   |   |   |   |   |   |── chapter01
  |   |   |   |   |   |   |── chapter02
  |   |   |   |   |   |   |── chapter03
  |   |   |   |   |   |   |── chapter04
  |   |   |   |   |   |   |── chapter05
  |   |   |   |   |   |   |── chapter06
  |   |   |   |   |   |   |── chapter07
  |   |   |   |   |   |   |── chapter08
  |   |   |   |   |   |   |── chapter09
  |   |   |   |   |   |   |── chapter10
  |   |   |   |   |   |   |── chapter11
  |   |   |   |   |   └── Main.java # 项目入口类 
  ├── README.md # 项目说明文档 
  └── pom.xml # Maven 配置文件，定义项目依赖和构建信息
```

## 功能模块

- **chapter01** 并发编程的挑战
  1. ConcurrencyTest 并发执行与单线程执行效率对比（上下文切换和CPU调度是有代价的）
  2. DeadLockDemo 演示死锁
  3. 资源限制的挑战，包含硬件资源与软件资源

- **chapter02** Java并发机制的底层实现原理
  1. volatile的应用
  2. synchronized的实现原理与应用
  3. 原子操作的实现原理

- **chapter03** Java内存模型
  1. Java内存模型的基础
  2. 重排序
  3. 顺序一致性
  4. volatile的内存语义
  5. 锁的内存语义
  6. final域的内存语义
  7. happens-before
  8. 双重检查锁定与延迟初始化