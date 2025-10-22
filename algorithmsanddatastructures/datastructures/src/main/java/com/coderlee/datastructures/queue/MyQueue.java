package com.coderlee.datastructures.queue;

/**
 * MyQueue
 * 定义队列的基本操作接口。
 * 该接口提供了一个队列所需的基础方法，包括判断队列是否为空、是否已满、添加元素、获取元素等。
 */
public interface MyQueue {

    /**
     * 判断队列是否已满。
     *
     * @return 如果队列已满，则返回 true；否则返回 false。
     */
    boolean isFull();

    /**
     * 判断队列是否为空。
     *
     * @return 如果队列为空，则返回 true；否则返回 false。
     */
    boolean isEmpty();

    /**
     * 向队列中添加一个元素。
     *
     * @param n 要添加到队列中的整数值。
     * @throws Exception 如果队列已满，则抛出异常。
     */
    void add(int n) throws Exception;

    /**
     * 从队列中获取并移除一个元素。
     *
     * @return 队列的第一个元素。
     * @throws Exception 如果队列为空，则抛出异常。
     */
    int get() throws Exception;

    /**
     * 显示队列中的所有元素。
     * 该方法用于打印队列的当前状态，通常按顺序输出队列中的元素。
     */
    void show();

    /**
     * 查看队列头部的元素，但不移除它。
     *
     * @return 队列头部的元素。
     * @throws Exception 如果队列为空，则抛出异常。
     */
    int head() throws Exception;
}