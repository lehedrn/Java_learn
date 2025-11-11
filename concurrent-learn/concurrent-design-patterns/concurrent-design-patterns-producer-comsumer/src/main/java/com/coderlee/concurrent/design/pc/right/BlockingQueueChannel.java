package com.coderlee.concurrent.design.pc.right;

import java.util.concurrent.BlockingQueue;

/**
 * 基于BlockingQueue实现的通道
 *
 * @param <T> 通道中传输的数据类型
 * @see Channel
 */
public class BlockingQueueChannel<T> implements Channel<T> {

    /**
     * 用于存储数据的阻塞队列
     */
    private final BlockingQueue<T> queue;

    /**
     * 构造函数
     *
     * @param queue 用于存储数据的阻塞队列
     */
    public BlockingQueueChannel(BlockingQueue<T> queue) {
        this.queue = queue;
    }

    /**
     * 从队列中取出数据，如果队列为空则阻塞等待
     *
     * @return 队列中的数据
     * @throws InterruptedException 当线程被中断时抛出
     */
    @Override
    public T take() throws InterruptedException {
        return queue.take();
    }

    /**
     * 向队列中放入数据，如果队列已满则阻塞等待
     *
     * @param t 要放入队列的数据
     * @throws InterruptedException 当线程被中断时抛出
     */
    @Override
    public void put(T t) throws InterruptedException {
        queue.put(t);
    }
}
