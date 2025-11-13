package com.coderlee.concurrent.design.pipeline.framework;

import java.util.concurrent.TimeUnit;

/**
 * 管道接口，定义了管道的基本操作。
 *
 * @param <IN>  输入类型
 * @param <OUT> 输出类型
 */
public interface Pipe<IN, OUT> {
    /**
     * 设置当前Pipe实例的下一个Pipe实例。
     *
     * @param nextPipe 下一个Pipe实例
     */
    void setNextPipe(Pipe<?, ?> nextPipe);

    /**
     * 初始化当前Pipe实例对外提供的服务。
     *
     * @param pipeContext 管道上下文
     */
    void init(PipeContext pipeContext);

    /**
     * 关闭当前Pipe实例对外提供的服务。
     *
     * @param timeout 超时时间
     * @param unit    时间单位
     */
    void shutdown(long timeout, TimeUnit unit);

    /**
     * 对输入元素进行处理，并将处理结果作为下一个Pipe实例的输入。
     *
     * @param input 输入数据
     * @throws InterruptedException 如果线程被中断
     */
    void process(IN input) throws InterruptedException;
}

