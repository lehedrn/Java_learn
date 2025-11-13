package com.coderlee.concurrent.design.pipeline.framework;

import com.coderlee.concurrent.design.thread.AbstractTerminationThread;
import com.coderlee.concurrent.design.thread.TerminationToken;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * 工作线程管道装饰器，使用固定数量的工作线程执行管道任务。
 *
 * @param <IN>  输入类型
 * @param <OUT> 输出类型
 * @see Pipe
 */
@Slf4j
public class WorkerThreadPipeDecorator<IN, OUT> implements Pipe<IN, OUT> {
    /**
     * 工作任务队列
     */
    protected final BlockingQueue<IN> workQueue;

    /**
     * 自定义线程集合
     */
    private final Set<AbstractTerminationThread> workThreads = new HashSet<>();

    /**
     * 线程停止标识
     */
    private final TerminationToken terminationToken = new TerminationToken();

    /**
     * Pipe代理
     */
    private final Pipe<IN, OUT> delegate;

    /**
     * 构造函数
     *
     * @param delegate   委托管道
     * @param workCount  工作线程数
     */
    public WorkerThreadPipeDecorator(Pipe<IN, OUT> delegate, int workCount) {
        this(new SynchronousQueue<IN>(), delegate, workCount);
    }

    /**
     * 构造函数
     *
     * @param workQueue  工作队列
     * @param delegate   委托管道
     * @param workCount  工作线程数
     */
    public WorkerThreadPipeDecorator(BlockingQueue<IN> workQueue, Pipe<IN, OUT> delegate, int workCount) {
        if (workCount <= 0){
            throw new IllegalArgumentException("工作线程数量不能小于或者等于0");
        }
        this.workQueue = workQueue;
        this.delegate = delegate;
        for (int i = 0; i < workCount; i++){
            workThreads.add(new AbstractTerminationThread(terminationToken) {
                @Override
                protected void doRun() throws InterruptedException {
                    try{
                        dispatch();
                    }finally {
                        terminationToken.noExecuteTaskCount.decrementAndGet();
                    }
                }
            });
        }
    }

    /**
     * 分发任务给委托管道处理
     *
     * @throws InterruptedException 如果线程被中断
     */
    protected void dispatch() throws InterruptedException{
        IN input = workQueue.take();
        delegate.process(input);
    }

    /**
     * 设置当前Pipe实例的下一个Pipe实例。
     *
     * @param nextPipe 下一个Pipe实例
     */
    @Override
    public void setNextPipe(Pipe<?, ?> nextPipe) {
        delegate.setNextPipe(nextPipe);
    }

    /**
     * 初始化当前Pipe实例对外提供的服务。
     *
     * @param pipeContext 管道上下文
     */
    @Override
    public void init(PipeContext pipeContext) {
        delegate.init(pipeContext);
        for (AbstractTerminationThread thread : workThreads){
            thread.start();
        }
    }

    /**
     * 关闭当前Pipe实例对外提供的服务。
     *
     * @param timeout 超时时间
     * @param unit    时间单位
     */
    @Override
    public void shutdown(long timeout, TimeUnit unit) {
        for (AbstractTerminationThread thread : workThreads){
            thread.terminate();
            try{
                thread.join(TimeUnit.MILLISECONDS.convert(timeout, unit));
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        delegate.shutdown(timeout, unit);
    }

    /**
     * 对输入元素进行处理，并将处理结果作为下一个Pipe实例的输入。
     *
     * @param input 输入数据
     * @throws InterruptedException 如果线程被中断
     */
    @Override
    public void process(IN input) throws InterruptedException {
        workQueue.put(input);
        terminationToken.noExecuteTaskCount.incrementAndGet();
    }
}
