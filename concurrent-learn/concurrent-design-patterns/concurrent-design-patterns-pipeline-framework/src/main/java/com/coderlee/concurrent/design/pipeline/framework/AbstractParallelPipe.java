package com.coderlee.concurrent.design.pipeline.framework;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * 并行处理管道抽象基类，支持将输入拆分成多个子任务并行执行后合并结果。
 *
 * @param <IN>  输入类型
 * @param <OUT> 输出类型
 * @param <V>   中间结果类型
 * @see AbstractPipe
 */
public abstract class AbstractParallelPipe<IN, OUT, V> extends AbstractPipe<IN, OUT> {
    /**
     * 线程池，用于执行并行任务
     */
    private final ExecutorService executor;

    /**
     * 构造函数，初始化线程池
     *
     * @param executor 线程池
     */
    public AbstractParallelPipe(ExecutorService executor) {
        super();
        this.executor = executor;
    }

    /**
     * 将一个input任务分解成多个子任务
     *
     * @param input 输入数据
     * @return 子任务列表
     * @throws Exception 如果构建任务失败
     */
    protected abstract List<Callable<V>> buildTasks(IN input) throws Exception;

    /**
     * 将每个阶段的结果合并成最终的结果数据
     *
     * @param subTaskResults 子任务结果列表
     * @return 合并后的结果
     * @throws Exception 如果合并结果失败
     */
    protected abstract OUT combine(List<Future<V>> subTaskResults) throws Exception;

    /**
     * 并行处理一批任务
     *
     * @param tasks 待处理的任务列表
     * @return 任务执行结果列表
     * @throws Exception 如果执行任务失败
     */
    protected List<Future<V>> invokeParallel(List<Callable<V>> tasks) throws Exception{
        return executor.invokeAll(tasks);
    }

    /**
     * 执行具体的处理逻辑，包括任务分解、并行执行和结果合并
     *
     * @param input 输入数据
     * @return 处理结果
     * @throws PipeException 如果处理过程中发生异常
     */
    @Override
    protected OUT doProcess(IN input) throws PipeException {
        OUT out = null;
        try{
            out = combine(invokeParallel(buildTasks(input)));
        }catch (Exception e){
            throw new PipeException(e.getMessage(), e, this, input);
        }
        return out;
    }
}

