package com.coderlee.concurrent.design.pipeline.framework;

import com.coderlee.concurrent.design.thread.TerminationToken;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * 线程池管道装饰器，使用线程池执行管道任务。
 *
 * @param <IN>  输入类型
 * @param <OUT> 输出类型
 * @see Pipe
 */
@Slf4j
public class ThreadPoolPipeDecorator<IN, OUT> implements Pipe<IN, OUT> {
    /**
     * 委托管道
     */
    private final Pipe<IN, OUT> delegate;

    /**
     * 线程池
     */
    private final ExecutorService executor;

    /**
     * 线程池停止的标志
     */
    private final ThreadPoolTerminationToken threadPoolTerminationToken;

    /**
     * 闭锁，用于等待所有任务完成
     */
    private final CountDownLatch countDownLatch = new CountDownLatch(1);

    /**
     * 构造函数
     *
     * @param delegate 委托管道
     * @param executor 线程池
     */
    public ThreadPoolPipeDecorator(Pipe<IN, OUT> delegate, ExecutorService executor) {
        this.delegate = delegate;
        this.executor = executor;
        this.threadPoolTerminationToken = ThreadPoolTerminationToken.newInstance(executor);
    }

    /**
     * 初始化当前Pipe实例对外提供的服务。
     *
     * @param pipeContext 管道上下文
     */
    @Override
    public void init(PipeContext pipeContext) {
        delegate.init(pipeContext);
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
     * 关闭当前Pipe实例对外提供的服务。
     *
     * @param timeout 超时时间
     * @param unit    时间单位
     */
    @Override
    public void shutdown(long timeout, TimeUnit unit) {
        //设置停止标识
        threadPoolTerminationToken.setToShutdown(true);
        //还有未执行完的任务
        if (threadPoolTerminationToken.noExecuteTaskCount.get() > 0){
            try{
                //任务还没执行完
                if (countDownLatch.getCount() > 0){
                    countDownLatch.await(timeout, unit);
                }
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
        executor.submit(() -> {
            int noExecuteTaskCount = -1;
            try {
                delegate.process(input);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                noExecuteTaskCount = threadPoolTerminationToken.noExecuteTaskCount.decrementAndGet();
            }
            //停止的标识为true，并且任务已经执行完毕
            if (threadPoolTerminationToken.isToShutdown() && noExecuteTaskCount == 0){
                countDownLatch.countDown();
            }
        });
        threadPoolTerminationToken.noExecuteTaskCount.incrementAndGet();
    }

    /**
     * 自定义线程池停止标志
     */
    private static class ThreadPoolTerminationToken extends TerminationToken{

        /**
         * 实例映射表
         */
        private static final ConcurrentMap<ExecutorService, ThreadPoolTerminationToken> INSTANCE = new ConcurrentHashMap<>();

        /**
         * 私有构造函数
         */
        private ThreadPoolTerminationToken(){

        }

        /**
         * 获取线程池终止令牌实例
         *
         * @param executor 线程池
         * @return 终止令牌实例
         */
        static ThreadPoolTerminationToken newInstance(ExecutorService executor){
            ThreadPoolTerminationToken terminationToken = INSTANCE.get(executor);
            if (terminationToken == null){
                terminationToken = new ThreadPoolTerminationToken();
                ThreadPoolTerminationToken poolTerminationToken = INSTANCE.putIfAbsent(executor, terminationToken);
                if (poolTerminationToken != null){
                    terminationToken = poolTerminationToken;
                }
            }
            return terminationToken;
        }
    }
}

