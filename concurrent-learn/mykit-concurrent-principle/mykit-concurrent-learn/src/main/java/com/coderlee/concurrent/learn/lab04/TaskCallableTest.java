package com.coderlee.concurrent.learn.lab04;

/**
 * 异步任务测试类。
 * <p>
 * 该类用于测试无返回结果的异步任务模型。
 * 通过将任务丢进线程或线程池中运行，并使用回调接口来获取任务的执行结果数据。
 * 
 * 具体实现中，
 * 定义了一个回调接口，
 * 在接口中定义了接收任务结果数据的方法，
 * 并在回调接口的实现类中完成对结果数据的处理逻辑。
 * </p>
 */
public class TaskCallableTest {

    /**
     * 主方法，程序入口。
     * <p>
     * 创建一个实现了 {@code TaskCallable} 接口的任务处理器实例 {@code TaskHandler}，
     * 并将其与任务执行器 {@code TaskExecutor} 关联。然后通过启动一个新的线程来执行任务，
     * 从而触发回调逻辑以处理任务结果。
     * </p>
     *
     * @param args 命令行参数（未使用）
     */
    public static void main(String[] args) {
        // 创建一个任务处理器实例，该实例实现了 TaskCallable 接口
        TaskCallable<TaskResult> taskCallable = new TaskHandler();

        // 将任务处理器与任务执行器关联，并传入任务描述信息
        TaskExecutor taskExecutor = new TaskExecutor(taskCallable, "测试回调任务");

        // 启动一个新的线程来执行任务
        new Thread(taskExecutor).start();
    }
}