package com.coderlee.juc;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * CallableDemo 类演示了如何使用 `Callable` 和 `FutureTask` 实现带有返回值的多线程任务。
 * 
 * <p>该类通过以下步骤完成异步计算任务：
 * <ol>
 *   <li>定义了一个实现了 `Callable<Integer>` 接口的内部类 [MyCallable]，用于执行耗时的整数累加计算。</li>
 *   <li>使用 `FutureTask` 包装 [MyCallable] 实例，并通过 `Thread` 启动异步任务。</li>
 *   <li>调用 `FutureTask.get()` 方法阻塞主线程，直到任务完成并返回结果。</li>
 *   <li>处理可能发生的线程中断或任务执行异常。</li>
 * </ol>
 * 
 * <p><strong>使用场景</strong>：
 * <ul>
 *   <li>需要从线程中获取计算结果的场景，例如复杂的数学计算、数据处理等。</li>
 *   <li>需要对线程执行过程中的异常进行捕获和处理的场景。</li>
 *   <li>结合线程池（如 `ExecutorService`）实现批量异步任务的管理和结果收集。</li>
 * </ul>
 * 
 * <p><strong>使用 `Callable` 的优势</strong>：
 * <ul>
 *   <li>支持返回值：`Callable` 的 [call()] 方法可以返回泛型结果，适用于需要获取线程执行结果的场景。</li>
 *   <li>更好的异常处理：`Callable` 支持抛出受检异常，便于处理任务中的错误逻辑。</li>
 *   <li>灵活性更高：结合 `Future` 或线程池，可以实现任务的取消、超时控制、批量执行等功能。</li>
 *   <li>`Callable` + `FetureTask`，可以实现与 `CountDownLanch` 一样的闭锁效果。</li>
 * </ul>
 * 
 * <p><strong>使用 `Callable` 的劣势</strong>：
 * <ul>
 *   <li>复杂性增加：相较于 `Runnable`，使用 `Callable` 需要引入 `FutureTask` 或线程池，增加了代码复杂度。</li>
 *   <li>阻塞问题：调用 `Future.get()` 方法会阻塞当前线程，可能导致性能瓶颈。</li>
 *   <li>不适合简单任务：对于无需返回值或复杂异常处理的任务，直接使用 `Runnable` 更加简洁高效。</li>
 * </ul>
 * 
 * <p>本示例展示了 Java 多线程编程中获取异步任务结果以及异常处理的完整流程。
 */
public class CallableDemo {
    public static void main(String[] args) {
        // 创建 MyCallable 的实例
        MyCallable mc = new MyCallable();
        
        // 使用 FutureTask 包装 Callable 实例，以便在新线程中执行任务
        FutureTask<Integer> ft = new FutureTask<>(mc);
        
        // 启动一个新线程来执行 FutureTask 中的任务
        new Thread(ft).start();
        
        try {
            // 调用 FutureTask 的 get() 方法阻塞当前线程，直到任务完成并返回结果
            Integer sum = ft.get();
            
            // 打印计算结果
            System.out.println("sum=" + sum);
            System.out.println("=====================================");
        } catch (InterruptedException | ExecutionException e) {
            // 捕获线程中断或任务执行中的异常，并打印堆栈信息
            e.printStackTrace();
        }
    }
}

/**
 * MyCallable 是一个实现了 `Callable<Integer>` 接口的类，用于执行耗时的计算任务。
 * 其核心方法 [call()] 计算从 0 到 999,999,998 的整数累加和，并返回结果。
 */
class MyCallable implements Callable<Integer> {

    /**
     * 实现 `Callable` 接口的 call() 方法。
     * 该方法执行一个耗时的计算任务：对从 0 到 999,999,998 的所有整数进行累加。
     *
     * @return 返回累加的结果（类型为 Integer）。
     * @throws Exception 如果在任务执行过程中发生错误，则抛出异常。
     */
    @Override
    public Integer call() throws Exception {
        int sum = 0;
        
        // 循环累加从 0 到 999,999,998 的整数
        for (int i = 0; i < 999999999; i++) {
            sum += i;
        }
        
        // 返回累加的结果
        return sum;
    }
}