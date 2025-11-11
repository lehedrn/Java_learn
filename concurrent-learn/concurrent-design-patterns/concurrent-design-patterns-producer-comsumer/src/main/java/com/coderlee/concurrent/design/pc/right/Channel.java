package com.coderlee.concurrent.design.pc.right;

/**
 * 通道接口，定义了生产者-消费者模式中的数据传输通道
 *
 * @param <T> 通道中传输的数据类型
 * @see BlockingQueueChannel
 */
public interface Channel<T> {
    /**
     * 从通道中取出数据，如果通道为空则阻塞等待
     *
     * @return 通道中的数据
     * @throws InterruptedException 当线程被中断时抛出
     */
    T take() throws InterruptedException;

    /**
     * 向通道中放入数据，如果通道已满则阻塞等待
     *
     * @param t 要放入通道的数据
     * @throws InterruptedException 当线程被中断时抛出
     */
    void put(T t) throws InterruptedException;
}

