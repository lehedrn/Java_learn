# `Thread.start()` 源码分析

## 目录
1. [Thread.start() 方法概述](#1-threadstart-方法概述)
2. [源码解析](#2-源码解析)
    - [Java 层面源码](#java-层面源码-threadjava)
    - [关键点说明](#关键点说明)
3. [如何查看对应的OpenJDK源码（JDK8）](#3-如何查看对应的openjdk源码基于jdk8)
4. [底层实现机制](#4-底层实现机制)
5. [总结](#5-总结)

## 1. Thread.start() 方法概述

`Thread.start()` 是 Java 中启动线程的关键方法。调用此方法会使得 JVM 调用 `Thread` 对象的 `run()` 方法，并且是在一个新的执行线程中运行。

线程状态流转：
```
NEW -> RUNNABLE (start()) -> RUNNING -> TERMINATED
```

## 2. 源码解析

### Java 层面源码 `Thread.Java`

```java
public synchronized void start() {
    // 检查线程状态，只有 NEW 状态的线程才能被启动
    if (threadStatus != 0)
        throw new IllegalThreadStateException();
    
    // 将线程添加到线程组中
    group.add(this);
    
    boolean started = false;
    try {
        // 调用 native 方法启动线程
        start0();
        started = true;
    } finally {
        // 如果启动失败，则从线程组中移除该线程
        if (!started) {
            group.threadStartFailed(this);
        }
    }
}

// Native 方法声明
private native void start0();
```

### 关键点说明

- **状态检查**：`threadStatus` 字段用于跟踪线程的状态，一个线程只能被启动一次。
- **线程组管理**：通过 `group.add(this)` 将线程加入到相应的线程组中。
- **native 方法调用**：真正的线程创建和启动工作由 `start0()` 这个 native 方法完成。

## 3. 如何查看对应的OpenJDK源码——基于JDK8

- 找到对应的JNI接口: `jdk\src\share\native\java\lang\Thread.c`
- 找到`start0`方法对应的实现：`JVM_StartThread`
- 找到对应的 `jvm.cpp` : `hotspot\src\share\vm\prims\jvm.cpp`
- 阅读代码找到最后的 `Thread::start(native_thread)`
- 深入到 `thread.cpp` : `hotspot\src\share\vm\runtime\thread.cpp`

## 4. 底层实现机制

在 HotSpot JVM 中，`start0()` 的具体实现在 `hotspot\src\share\vm\prims\jvm.cpp` 文件中的 `JVM_StartThread` 函数里：

- 创建操作系统级别的线程
- 设置线程入口点为 `java.lang.Thread.run()` 方法
- 启动新创建的操作系统线程

JVM 层实现流程：

```
[Java] Thread.start()
     ↓
[C++] JVM_StartThread()
     ↓
创建 JavaThread & OSThread
     ↓
设置入口函数为 thread_entry()
     ↓
调用 os::start_thread()
     ↓
OS 创建本地线程并运行
```


`JVM_StartThread` 核心代码：

```cpp
JVM_ENTRY(void, JVM_StartThread(JNIEnv* env, jobject jthread))
  JVMWrapper("JVM_StartThread");
  JavaThread *native_thread = NULL;

  // We cannot hold the Threads_lock when we throw an exception,
  // due to rank ordering issues. Example:  we might need to grab the
  // Heap_lock while we construct the exception.
  bool throw_illegal_thread_state = false;

  // We must release the Threads_lock before we can post a jvmti event
  // in Thread::start.
  {
    // Ensure that the C++ Thread and OSThread structures aren't freed before
    // we operate.
    MutexLocker mu(Threads_lock);

    // Since JDK 5 the java.lang.Thread threadStatus is used to prevent
    // re-starting an already started thread, so we should usually find
    // that the JavaThread is null. However for a JNI attached thread
    // there is a small window between the Thread object being created
    // (with its JavaThread set) and the update to its threadStatus, so we
    // have to check for this
    if (java_lang_Thread::thread(JNIHandles::resolve_non_null(jthread)) != NULL) {
      throw_illegal_thread_state = true;
    } else {
      // We could also check the stillborn flag to see if this thread was already stopped, but
      // for historical reasons we let the thread detect that itself when it starts running

      jlong size =
             java_lang_Thread::stackSize(JNIHandles::resolve_non_null(jthread));
      // Allocate the C++ Thread structure and create the native thread.  The
      // stack size retrieved from java is signed, but the constructor takes
      // size_t (an unsigned type), so avoid passing negative values which would
      // result in really large stacks.
      size_t sz = size > 0 ? (size_t) size : 0;
      native_thread = new JavaThread(&thread_entry, sz);

      // At this point it may be possible that no osthread was created for the
      // JavaThread due to lack of memory. Check for this situation and throw
      // an exception if necessary. Eventually we may want to change this so
      // that we only grab the lock if the thread was created successfully -
      // then we can also do this check and throw the exception in the
      // JavaThread constructor.
      if (native_thread->osthread() != NULL) {
        // Note: the current thread is not being used within "prepare".
        native_thread->prepare(jthread);
      }
    }
  }

  if (throw_illegal_thread_state) {
    THROW(vmSymbols::java_lang_IllegalThreadStateException());
  }

  assert(native_thread != NULL, "Starting null thread?");

  if (native_thread->osthread() == NULL) {
    // No one should hold a reference to the 'native_thread'.
    delete native_thread;
    if (JvmtiExport::should_post_resource_exhausted()) {
      JvmtiExport::post_resource_exhausted(
        JVMTI_RESOURCE_EXHAUSTED_OOM_ERROR | JVMTI_RESOURCE_EXHAUSTED_THREADS,
        "unable to create new native thread");
    }
    THROW_MSG(vmSymbols::java_lang_OutOfMemoryError(),
              "unable to create new native thread");
  }

  Thread::start(native_thread);

JVM_END
```


从方法 `JVM_StartThread` 末尾的 `Thread::start(native_thread);` 我们可以继续深入到 `thread.cpp` 文件中 `Thread::start(JavaThread *thread)` 方法：

```cpp
void Thread::start(Thread* thread) {
  trace("start", thread);
  // Start is different from resume in that its safety is guaranteed by context or
  // being called from a Java method synchronized on the Thread object.
  if (!DisableStartThread) {
    if (thread->is_Java_thread()) {
      // Initialize the thread state to RUNNABLE before starting this thread.
      // Can not set it after the thread started because we do not know the
      // exact thread state at that time. It could be in MONITOR_WAIT or
      // in SLEEPING or some other state.
      java_lang_Thread::set_thread_status(((JavaThread*)thread)->threadObj(),
                                          java_lang_Thread::RUNNABLE);
    }
    os::start_thread(thread);
  }
}
```


通过 `os::start_thread(thread);` 方法启动操作系统级别的线程。


## 5. 总结

`Thread.start()` 是启动线程的标准方式，它不仅负责触发 `run()` 方法的异步执行，还完成了从 Java 到 OS 层线程映射的核心工作。通过层层调用，最终由操作系统创建新的线程实体并执行指定的任务。

- `start0()` 是 native 方法，具体实现在 JVM 内部；
- 实际上每个平台有不同的实现方式（Linux 使用 pthread，Windows 使用 Windows API）；
- 在 JVM 初始化过程中也会创建多个内部线程（GC、编译器线程等）；