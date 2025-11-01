package com.coderlee.concurrent.learn.lab01;

/**
 * ProcessThreadTest 是一个用于演示 Java 进程与线程关系的测试类。
 * 
 * ### 进程与线程的基本概念：
 * 1. **进程**：
 *    - 进程是操作系统资源分配的基本单位。
 *    - 每个进程拥有独立的内存空间、系统资源（如文件句柄）和执行环境。
 *    - 在 Java 中，启动一个程序时会创建一个进程，该进程由 JVM 管理。
 * 
 * 2. **线程**：
 *    - 线程是进程内的执行单元，也是 CPU 调度的基本单位。
 *    - 同一进程中的线程共享进程的内存空间和资源，但每个线程有自己的栈空间。
 *    - 线程的创建和切换开销较小，适合并发任务的执行。
 * 
 * ### 进程与线程的区别：
 * | 特性                | 进程                                  | 线程                              |
 * |---------------------|---------------------------------------|-----------------------------------|
 * | **资源分配**        | 拥有独立的内存空间和系统资源          | 共享所属进程的内存和资源          |
 * | **通信方式**        | 需要通过 IPC（如管道、消息队列等）    | 可以直接访问共享变量              |
 * | **创建/销毁开销**   | 较大                                 | 较小                             |
 * | **上下文切换开销**  | 较高                                 | 较低                             |
 * | **稳定性**          | 一个进程崩溃不会影响其他进程          | 一个线程崩溃可能导致整个进程崩溃  |
 * 
 * ### 本类的功能：
 * 1. 启动一个 Java 进程。
 * 2. 在进程中创建并启动一个新的线程（名为 "thread-lee-001"），该线程进入无限循环，模拟后台任务。
 * 3. 主线程输出一条提示信息，表明程序已成功启动。
 * 
 * 使用场景：
 * - 通过 `jps` 或 `ps` 命令观察 Java 进程。
 * - 使用 `jstack` 工具查看线程的运行状态，理解线程与进程的关系。
 * 
 * 一、运行后：
 * Java 启动 = 创建一个进程
 * main 线程运行
 * 新建一个线程 thread-binghe-001，进入死循环
 * 
 * 1. 观察进程
 * 使用OS命令观察进程 `jps -l` 或者 `ps -ef|grep java`
 * >
 * 1578171 ProcessThreadTest
 * 这是一整个Java进程
 * 
 * 2. 观察线程
 * 使用JVM工具查看线程 `jstack 1578171`
 * >
 * 2025-11-01 22:24:27
 * Full thread dump Java HotSpot(TM) 64-Bit Server VM (25.471-b09 mixed mode):
 * 
 * "Attach Listener" #11 daemon prio=9 os_prio=0 tid=0x00007ba3b0001000 nid=0x181adb waiting on condition [0x0000000000000000]
 *    java.lang.Thread.State: RUNNABLE
 * 
 * "DestroyJavaVM" #10 prio=5 os_prio=0 tid=0x00007ba3f400a000 nid=0x1814bd waiting on condition [0x0000000000000000]
 *    java.lang.Thread.State: RUNNABLE

 * "thread-lee-001" #9 prio=5 os_prio=0 tid=0x00007ba3f4171000 nid=0x1814cb runnable [0x00007ba3b7bfb000]
 *    java.lang.Thread.State: RUNNABLE
 *         at com.coderlee.concurrent.learn.lab01.ProcessThreadTest.lambda$0(ProcessThreadTest.java:20)
 *         at com.coderlee.concurrent.learn.lab01.ProcessThreadTest$$Lambda$1/0x00000007c0072028.run(Unknown Source)
 *         at java.lang.Thread.run(Thread.java:750)
 * 
 * "Service Thread" #8 daemon prio=9 os_prio=0 tid=0x00007ba3f40ce800 nid=0x1814c9 runnable [0x0000000000000000]
 *    java.lang.Thread.State: RUNNABLE
 * 
 * "C1 CompilerThread2" #7 daemon prio=9 os_prio=0 tid=0x00007ba3f40c3800 nid=0x1814c8 waiting on condition [0x0000000000000000]
 *    java.lang.Thread.State: RUNNABLE
 * 
 * "C2 CompilerThread1" #6 daemon prio=9 os_prio=0 tid=0x00007ba3f40c2000 nid=0x1814c7 waiting on condition [0x0000000000000000]
 *    java.lang.Thread.State: RUNNABLE
 * 
 * "C2 CompilerThread0" #5 daemon prio=9 os_prio=0 tid=0x00007ba3f40bf000 nid=0x1814c6 waiting on condition [0x0000000000000000]
 *    java.lang.Thread.State: RUNNABLE
 * 
 * "Signal Dispatcher" #4 daemon prio=9 os_prio=0 tid=0x00007ba3f40bb000 nid=0x1814c5 runnable [0x0000000000000000]
 *    java.lang.Thread.State: RUNNABLE
 * 
 * "Finalizer" #3 daemon prio=8 os_prio=0 tid=0x00007ba3f4088000 nid=0x1814c4 in Object.wait() [0x00007ba3c436c000]
 *    java.lang.Thread.State: WAITING (on object monitor)
 *         at java.lang.Object.wait(Native Method)
 *         - waiting on <0x0000000719a08f08> (a java.lang.ref.ReferenceQueue$Lock)
 *         at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:150)
 *         - locked <0x0000000719a08f08> (a java.lang.ref.ReferenceQueue$Lock)
 *         at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:171)
 *         at java.lang.ref.Finalizer$FinalizerThread.run(Finalizer.java:188)
 * 
 * "Reference Handler" #2 daemon prio=10 os_prio=0 tid=0x00007ba3f4083800 nid=0x1814c3 in Object.wait() [0x00007ba3c446c000]
 *    java.lang.Thread.State: WAITING (on object monitor)
 *         at java.lang.Object.wait(Native Method)
 *         - waiting on <0x0000000719a06b98> (a java.lang.ref.Reference$Lock)
 *         at java.lang.Object.wait(Object.java:502)
 *         at java.lang.ref.Reference.tryHandlePending(Reference.java:191)
 *         - locked <0x0000000719a06b98> (a java.lang.ref.Reference$Lock)
 *         at java.lang.ref.Reference$ReferenceHandler.run(Reference.java:153)
 * 
 * "VM Thread" os_prio=0 tid=0x00007ba3f4079800 nid=0x1814c2 runnable 
 * 
 * "GC task thread#0 (ParallelGC)" os_prio=0 tid=0x00007ba3f4020000 nid=0x1814be runnable 
 * 
 * "GC task thread#1 (ParallelGC)" os_prio=0 tid=0x00007ba3f4022000 nid=0x1814bf runnable 
 * 
 * "GC task thread#2 (ParallelGC)" os_prio=0 tid=0x00007ba3f4023800 nid=0x1814c0 runnable 
 * 
 * "GC task thread#3 (ParallelGC)" os_prio=0 tid=0x00007ba3f4025800 nid=0x1814c1 runnable 
 * 
 * "VM Periodic Task Thread" os_prio=0 tid=0x00007ba3f40d5000 nid=0x1814ca waiting on condition 
 * 
 * JNI global references: 309
 */
public class ProcessThreadTest {

    /**
     * main 方法是程序的入口点。
     * 执行逻辑如下：
     * 1. 创建并启动一个名为 "thread-lee-001" 的线程，该线程会进入无限循环，模拟后台任务。
     * 2. 在主线程中打印一条提示信息，表明程序已成功启动。
     *
     */
    public static void main(String[] args) {
        // 创建并启动一个新的线程，线程名称为 "thread-lee-001"
        new Thread(() -> {
            while (true) {
                // 线程进入无限循环，模拟后台运行的任务
            }
        }, "thread-lee-001").start();

        // 在主线程中输出提示信息，表明程序启动成功
        System.out.println("程序启动成功...");
    }
}