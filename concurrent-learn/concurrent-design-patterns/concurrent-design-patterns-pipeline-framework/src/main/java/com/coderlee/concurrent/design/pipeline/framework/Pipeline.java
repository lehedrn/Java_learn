package com.coderlee.concurrent.design.pipeline.framework;

/**
 * 管道线接口，扩展了{@link Pipe}接口，提供了添加处理阶段的功能。
 *
 * @param <IN>  输入类型
 * @param <OUT> 输出类型
 * @see Pipe
 */
public interface Pipeline<IN, OUT> extends Pipe<IN, OUT> {
    /**
     * 添加处理阶段
     *
     * @param pipe 要添加的管道实例
     */
    void addPipe(Pipe<?, ?> pipe);
}
