package com.coderlee.concurrent.learn.lab04;

import java.io.Serializable;

import lombok.Data;

/**
 * 定义任务执行结果数据的封装类。
 * <p>
 * 该类用于封装异步任务或并发任务的执行结果，包含任务状态、消息以及结果数据。
 * 实现了 {@link Serializable} 接口，以支持序列化操作，便于在网络传输或持久化场景中使用。
 * </p>
 */
@Data
public class TaskResult implements Serializable {
    
    // 序列化版本号，确保反序列化兼容性
    private static final long serialVersionUID = 4200170154821024244L; 

    /**
     * 任务状态。
     * <p>
     * 表示任务的执行状态，例如成功、失败或其他自定义状态码。
     * </p>
     */
    private Integer taskStatus;

    /**
     * 任务消息。
     * <p>
     * 提供与任务执行相关的附加信息，例如错误描述或成功提示。
     * </p>
     */
    private String taskMessage;

    /**
     * 任务结果数据。
     * <p>
     * 存储任务执行后返回的具体数据内容，通常为业务逻辑处理的结果。
     * </p>
     */
    private String taskResult;
}
