package com.coderlee.concurrent.design.pipeline.framework;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 简单管道实现类，管理一系列管道实例，并提供统一的处理入口。
 *
 * @param <IN>  输入类型
 * @param <OUT> 输出类型
 * @see AbstractPipe
 * @see Pipeline
 */
@Slf4j
public class SimplePipeline<IN, OUT> extends AbstractPipe<IN, OUT> implements Pipeline<IN, OUT> {
    /**
     * 存放Pipe实例的链表，实际存放的是WorkerThreadPipeDecorator类型的对象
     */
    private final Queue<Pipe<?, ?>> pipes = new LinkedList<>();

    /**
     * 线程池
     */
    private final ExecutorService executor;

    /**
     * 默认构造函数，使用默认线程池
     */
    public SimplePipeline() {
        //存入自定义线程池
        this(new ThreadPoolExecutor(
                1,
                1,
                60,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1024),
                (r) -> {
                    Thread t = new Thread(r, "thread-execute-pipeline");
                    t.setDaemon(true);
                    return t;
                },
                new ThreadPoolExecutor.CallerRunsPolicy()));
    }

    /**
     * 构造函数，使用指定线程池
     *
     * @param executor 线程池
     */
    public SimplePipeline(ExecutorService executor) {
        this.executor = executor;
    }

    /**
     * 关闭当前Pipe实例对外提供的服务。
     *
     * @param timeout 超时时间
     * @param unit    时间单位
     */
    @Override
    public void shutdown(long timeout, TimeUnit unit) {
        Pipe<?, ?> pipe;
        while ((pipe = pipes.poll()) != null){
            pipe.shutdown(timeout, unit);
        }
        executor.shutdown();
    }

    /**
     * 具体的处理逻辑，由子类实现
     *
     * @param input 输入数据
     * @return 处理结果
     * @throws PipeException 如果处理过程中发生异常
     */
    @Override
    protected OUT doProcess(IN input) throws PipeException {
        //TODO 暂时啥也不做
        return null;
    }

    /**
     * 添加处理阶段
     *
     * @param pipe 要添加的管道实例
     */
    @Override
    public void addPipe(Pipe<?, ?> pipe) {
        pipes.add(pipe);
    }

    /**
     * 添加基于工作线程的管道装饰器
     *
     * @param delegate    委托管道
     * @param workerCount 工作线程数
     * @param <INPUT>     输入类型
     * @param <OUTPUT>    输出类型
     */
    public <INPUT, OUTPUT> void addAsWorkerThreadBasedPipe(Pipe<INPUT, OUTPUT> delegate, int workerCount){
        addPipe(new WorkerThreadPipeDecorator<>(delegate, workerCount));
    }

    /**
     * 添加基于线程池的管道装饰器
     *
     * @param delegate 委托管道
     * @param executor 线程池
     * @param <INPUT>  输入类型
     * @param <OUTPUT> 输出类型
     */
    public <INPUT, OUTPUT> void addAsThreadPoolBasedPipe(Pipe<INPUT, OUTPUT> delegate, ExecutorService executor){
        addPipe(new ThreadPoolPipeDecorator<>(delegate, executor));
    }

    /**
     * 对输入元素进行处理，并将处理结果作为下一个Pipe实例的输入。
     *
     * @param input 输入数据
     * @throws InterruptedException 如果线程被中断
     */
    @Override
    public void process(IN input) throws InterruptedException {
        Pipe<IN, ?> firstPipe = (Pipe<IN, ?>) pipes.peek();
        firstPipe.process(input);
    }

    /**
     * 初始化当前Pipe实例对外提供的服务。
     *
     * @param pipeContext 管道上下文
     */
    @Override
    public void init(PipeContext pipeContext) {
        LinkedList<Pipe<?, ?>> pipeLinkedList = (LinkedList<Pipe<?, ?>>) pipes;
        Pipe<?, ?> prevPipe = this;
        for (Pipe<?, ?> pipe : pipeLinkedList){
            prevPipe.setNextPipe(pipe);
            prevPipe = pipe;
        }
        executor.submit(new PipeInitTask((List<Pipe<?, ?>>) pipes, pipeContext));
    }

    /**
     * 管道初始化任务
     */
    static class PipeInitTask implements Runnable{

        /**
         * 管道列表
         */
        final List<Pipe<?, ?>> pipes;

        /**
         * 管道上下文
         */
        final PipeContext pipeContext;

        /**
         * 构造函数
         *
         * @param pipes       管道列表
         * @param pipeContext 管道上下文
         */
        public PipeInitTask(List<Pipe<?, ?>> pipes, PipeContext pipeContext) {
            this.pipes = pipes;
            this.pipeContext = pipeContext;
        }

        /**
         * 执行初始化任务
         */
        @Override
        public void run() {
            try{
                for (Pipe<?, ?> pipe : pipes){
                    pipe.init(pipeContext);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建默认的PipeContext
     *
     * @return 默认的PipeContext实例
     */
    public PipeContext newDefaultPipelineContext(){
        return (pe) -> {
            executor.submit(() -> {
                System.out.println(pe);
            });
        };
    }
}

