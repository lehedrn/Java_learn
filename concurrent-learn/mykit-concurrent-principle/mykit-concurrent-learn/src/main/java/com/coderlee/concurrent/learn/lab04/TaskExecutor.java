package com.coderlee.concurrent.learn.lab04;

/**
 * 任务执行类，用于异步执行任务并处理结果。
 * <p>
 * 该类实现了 {@link Runnable} 接口，表示一个可运行的任务。
 * 类中定义了一个回调接口类型的成员变量和一个任务参数（模拟任务的输入数据），
 * 并通过构造方法注入回调接口和任务参数。
 * </p>
 * <p>
 * 在 {@link #run()} 方法中执行任务逻辑，完成后将结果封装为 {@link TaskResult} 对象，
 * 并通过回调接口的方法将结果传递给调用方。
 * </p>
 */
public class TaskExecutor implements Runnable {

    /**
     * 回调接口实例，用于在任务完成后传递结果数据。
     */
    private TaskCallable<TaskResult> taskCallable;

    /**
     * 模拟任务的参数，表示任务的输入数据。
     */
    private String taskParameter;

    /**
     * 构造方法，用于初始化任务执行器。
     *
     * @param taskCallable 回调接口实例，用于处理任务结果
     * @param taskParameter 模拟任务的参数，表示任务的输入数据
     */
    public TaskExecutor(TaskCallable<TaskResult> taskCallable, String taskParameter) {
        this.taskCallable = taskCallable;
        this.taskParameter = taskParameter;
    }

    /**
     * 执行任务的核心逻辑。
     * <p>
     * 在该方法中模拟任务的执行过程，将结果数据封装为 {@link TaskResult} 对象，
     * 并通过回调接口的方法将结果传递给调用方。
     * </p>
     */
    @Override
    public void run() {
        // 模拟一系列业务逻辑，生成任务结果
        TaskResult t = new TaskResult();
        t.setTaskStatus(1); // 设置任务状态为成功（假设状态码 1 表示成功）
        t.setTaskMessage(this.taskParameter); // 将任务参数作为消息内容
        t.setTaskResult("异步回调成功"); // 设置任务结果描述

        // 调用回调接口的方法，将任务结果传递给调用方
        taskCallable.callable(t);
    }
}
