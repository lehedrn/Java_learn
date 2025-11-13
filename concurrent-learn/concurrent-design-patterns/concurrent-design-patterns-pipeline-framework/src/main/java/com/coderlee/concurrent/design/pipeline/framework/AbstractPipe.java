package com.coderlee.concurrent.design.pipeline.framework;

import java.util.concurrent.TimeUnit;

/**
 * 管道抽象基类，实现了基本的管道功能，包括设置下一个管道、初始化上下文等。
 *
 * @param <IN>  输入类型
 * @param <OUT> 输出类型
 * @see Pipe
 */
public abstract class AbstractPipe<IN, OUT> implements Pipe<IN, OUT> {
    /**
     * 当前正在处理的Pipe的下一个Pipe
     */
    protected volatile Pipe<?, ?> nextPipe = null;

    /**
     * 记录的Pipe的上下文
     */
    protected volatile PipeContext pipeContext;

    /**
     * 初始化当前Pipe实例对外提供的服务。
     *
     * @param pipeContext 管道上下文
     */
    @Override
    public void init(PipeContext pipeContext) {
        this.pipeContext = pipeContext;
    }

    /**
     * 设置当前Pipe实例的下一个Pipe实例。
     *
     * @param nextPipe 下一个Pipe实例
     */
    @Override
    public void setNextPipe(Pipe<?, ?> nextPipe) {
        this.nextPipe = nextPipe;
    }

    /**
     * 关闭当前Pipe实例对外提供的服务。
     *
     * @param timeout 超时时间
     * @param unit    时间单位
     */
    @Override
    public void shutdown(long timeout, TimeUnit unit) {
        //TODO 暂时啥也不做
    }

    /**
     * 具体的处理逻辑，由子类实现
     *
     * @param input 输入数据
     * @return 处理结果
     * @throws PipeException 如果处理过程中发生异常
     */
    protected abstract OUT doProcess(IN input) throws PipeException;

    /**
     * 对输入元素进行处理，并将处理结果作为下一个Pipe实例的输入。
     *
     * @param input 输入数据
     * @throws InterruptedException 如果线程被中断
     */
    @Override
    public void process(IN input) throws InterruptedException {
        try{
            OUT out = doProcess(input);
            if (nextPipe != null && out != null){
                //将当前Pipe的输出作为下一个Pipe的输入
                ((Pipe<OUT, ?>) nextPipe).process(out);
            }
        }catch (InterruptedException e){
            //中断线程的执行
            Thread.currentThread().interrupt();
        }catch (PipeException e){
            pipeContext.handleError(e);
        }catch (Exception e){
            pipeContext.handleError(new PipeException(e.getMessage(), e, this, input));
        }
    }
}
