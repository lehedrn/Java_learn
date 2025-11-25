# `CompletableFuture` 常用方法

## 1. 获得结果和触发计算

**获取结果**

- `public T get()`
不见不散
- `public T get(long timeout, TimeUnit unit)`
过时不候
- `public T join()`
与`get()`类似，但不会抛出`InterruptedException`和`ExecutionException`
- `public T getNow(T valueifAbsent)`
  - 计算完成就返回正常值
  - 计算未完成则返回传入的默认值
  - 立即返回结果，不阻塞

**主动触发计算**

- `public boolean complete(V value)`
是否打断 `get()`/`join()` 方法立即返回括号中的值

**代码示例**
[CompletableFutureAPIDemo.java](../../../src/main/java/com/coderlee/juc1/cf/CompletableFutureAPIDemo.java)

## 2. 对计算结果进行处理

- `thenApply()`
  - 计算结果存在依赖关系，这两个线程串行化
  - 由于存在依赖关系（当前步错，不走下一步），当前步骤有异常的话就叫停

- `handle()`
  - 计算结果存在依赖关系，这两个线程串行化
  - 有异常也可以往下走一步

**代码示例**
[CompletableFutureAPI2Demo.java](../../../src/main/java/com/coderlee/juc1/cf/CompletableFutureAPI2Demo.java)

## 3. 对计算结果进行消费

- `thenAccept()`
接收结果的任务处理，并消费结果，无返回结果

**对比**
- `thenRun(Runnable runnable)` : 任务A执行完执行任务B，并且不需要A的结果
- `thenAccept(Consumer acttion)` : 任务A执行完执行任务B，B需要A的结果，但是任务B没有返回值
- `thenApply(Function fn)` ： 任务A执行完执行B，B需要A的结果，同时任务B有返回值

**代码示例**
[CompletableFutureAPI3Demo.java](../../../src/main/java/com/coderlee/juc1/cf/CompletableFutureAPI3Demo.java)

**`CompletableFuture`和线程池说明**
- 如果没有传入自定义线程池，都用默认线程池ForkJoinPool
- 传入一个线程池，如果你执行第一个任务时，传入了一个自定义线程池
  - 调用 `thenRun` 方法执行第二个任务时，则第二个任务和第一个任务时共用同一个线程池
  - 调用 `thenRunAsync` 执行第二个任务时，则第一个任务使用的是你自定义的线程池，第二个任务使用的是ForkJoin线程池

> 线程处理太快，系统优化切换原则，直接使用main线程处理，`thenAccept` 和 `thenAcceptAsync`，`thenApply` 和 `thenApplyAsync` 等，之间的区别同理。


## 4. 对计算速度进行选用

**`applyToEither`**
谁快就用谁的结果

**代码示例**
[CompletableFutureFastDemo.java](../../../src/main/java/com/coderlee/juc1/cf/CompletableFutureFastDemo.java)

## 5. 对计算结果进行合并

**`thenCombine`**
- 两个`CompletableStage`任务都完成后，最终能把两个任务的结果一起交给thenCombine来处理
- 先完成的先等着，等待其他分支任务

**代码示例**
[CompletableFutureCombineDemo.java](../../../src/main/java/com/coderlee/juc1/cf/CompletableFutureCombineDemo.java)