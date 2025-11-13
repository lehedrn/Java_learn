package com.coderlee.concurrent.design.pipeline.framework;

import lombok.Getter;

/**
 * 管道异常类，表示在管道处理过程中发生的异常。
 */
@Getter
public class PipeException extends RuntimeException {
    /**
     * 标识哪个Pipe实例抛出了异常
     */
    private final Pipe<?, ?> pipe;

    /**
     * 抛出异常时处理的输入数据
     */
    private final Object input;

    /**
     * 构造函数，创建一个新的管道异常实例
     *
     * @param message 异常消息
     * @param pipe    抛出异常的管道实例
     * @param input   异常发生时的输入数据
     */
    public PipeException(String message, Pipe<?, ?> pipe, Object input) {
        super(message);
        this.pipe = pipe;
        this.input = input;
    }

    /**
     * 构造函数，创建一个新的管道异常实例
     *
     * @param message 异常消息
     * @param cause   异常原因
     * @param pipe    抛出异常的管道实例
     * @param input   异常发生时的输入数据
     */
    public PipeException(String message, Throwable cause, Pipe<?, ?> pipe, Object input) {
        super(message, cause);
        this.pipe = pipe;
        this.input = input;
    }
}

