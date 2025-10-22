package com.coderlee.datastructures.queue;

/**
 * ArrayQueue 类实现了基于数组的队列结构。
 * 提供了添加、获取、查看队列头数据以及判断队列是否为空或满的方法。
 */
public class ArrayQueue implements MyQueue {
    private int maxSize; // 队列的最大容量
    private int front; // 队列头部指针
    private int rear; // 队列尾部指针
    private int[] arr; // 存储队列数据的数组

    /**
     * 构造函数，用于初始化队列。
     * @param queueSize 队列的大小
     */
    public ArrayQueue(int queueSize) {
        this.maxSize = queueSize;
        this.arr = new int[maxSize]; // 初始化数组
        this.front = -1; // 初始化头部指针
        this.rear = -1; // 初始化尾部指针
    }

    /**
     * 判断队列是否已满。
     * @return 如果队列已满返回 true，否则返回 false
     */
    public boolean isFull() {
        return rear == maxSize - 1; // 尾部指针等于最大容量减一时队列已满
    }

    /**
     * 判断队列是否为空。
     * @return 如果队列为空返回 true，否则返回 false
     */
    public boolean isEmpty() {
        return front == rear; // 头部指针等于尾部指针时队列为空
    }

    /**
     * 向队列中添加元素。
     * @param n 要添加的元素
     */
    public void add(int n) {
        if (isFull()) {
            throw new RuntimeException("队列已满，无法添加数据"); // 如果队列已满则抛出异常
        }
        rear++; // 尾部指针后移
        arr[rear] = n; // 在尾部插入数据
    }

    /**
     * 从队列中获取元素。
     * @return 返回获取到的元素
     * @throws RuntimeException 如果队列为空，则抛出运行时异常
     */
    public int get() {
        if (isEmpty()) {
            throw new RuntimeException("队列已空，无法获取数据"); // 如果队列为空则抛出异常
        }
        front++; // 头部指针后移
        return arr[front]; // 返回头部数据
    }

    /**
     * 显示队列中的所有元素。
     */
    public void show() {
        if (isEmpty()) {
            System.out.println("队列为空，无法展示数据"); // 如果队列为空则打印提示
            return;
        }
        // for (int i = 0; i < arr.length; i++) {
        for (int i = front + 1; i <= rear; i++) {
            System.out.printf("arr[%d]=%d\n", i, arr[i]); // 打印数组中的每个元素
        }
    }

    /**
     * 查看队列头部的元素（不移除）。
     * @return 返回队列头部的元素
     * @throws RuntimeException 如果队列为空，则抛出运行时异常
     */
    public int head() {
        if (isEmpty()) {
            throw new RuntimeException("队列为空，无法获取数据"); // 如果队列为空则抛出异常
        }
        return arr[front + 1]; // 返回头部指针后一位的数据
    }
}