package com.coderlee.concurrent.design.pipeline.framework;

/**
 * 管道上下文接口，用于处理管道中的异常情况。
 */
public interface PipeContext {
    /**
     * 处理异常
     *
     * @param pipeException 管道异常
     */
    void handleError(PipeException pipeException);
}
