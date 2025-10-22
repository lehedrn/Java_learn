package com.coderlee.datastructures.queue;

/**
 * 循环队列的实现类。
 * 使用数组模拟循环队列，并提供基本的队列操作方法，如添加、删除、展示等。
 */
class CircleArrayQueue implements MyQueue {
    private int maxSize; // 队列的最大容量（实际容量+1，用于避免假溢出）
    private int front;   // 指向队列头部元素的位置
    private int rear;    // 指向队列尾部下一个空位的位置
    private int[] arr;   // 存储队列数据的数组

    /**
     * 构造函数，初始化循环队列。
     *
     * @param arrayMaxSize 用户指定的队列最大容量。
     *                     实际数组长度为 `arrayMaxSize + 1`，以避免假溢出问题。
     */
    public CircleArrayQueue(int arrayMaxSize) {
        this.maxSize = arrayMaxSize + 1; // 实际容量比用户指定多1
        this.arr = new int[this.maxSize]; // 初始化数组
    }

    /**
     * 判断队列是否已满。
     *
     * @return 如果队列已满，则返回 true；否则返回 false。
     */
    public boolean isFull() {
        return (rear + 1) % maxSize == front; // 通过模运算判断队列是否已满
    }

    /**
     * 判断队列是否为空。
     *
     * @return 如果队列为空，则返回 true；否则返回 false。
     */
    public boolean isEmpty() {
        return rear == front; // 当头指针和尾指针相等时，队列为空
    }

    /**
     * 向队列中添加一个元素。
     *
     * @param n 要添加到队列中的整数值。
     * @throws RuntimeException 如果队列已满，则抛出异常。
     */
    public void add(int n) {
        if (isFull()) {
            throw new RuntimeException("队列已满，无法添加数据"); // 队列已满时抛出异常
        }
        arr[rear] = n; // 将值存储到尾指针位置
        rear = (rear + 1) % maxSize; // 更新尾指针位置（循环移动）
    }

    /**
     * 从队列中获取并移除一个元素。
     *
     * @return 队列的第一个元素。
     * @throws RuntimeException 如果队列为空，则抛出异常。
     */
    public int get() {
        if (isEmpty()) {
            throw new RuntimeException("队列已空，无法获取数据"); // 队列为空时抛出异常
        }
        int value = arr[front]; // 获取头指针指向的元素
        front = (front + 1) % maxSize; // 更新头指针位置（循环移动）
        return value; // 返回获取的元素
    }

    /**
     * 显示队列中的所有元素。
     * 打印当前队列中的所有有效元素，按照其在队列中的顺序输出。
     */
    public void show() {
        if (isEmpty()) {
            System.out.println("队列为空，无法展示数据"); // 队列为空时打印提示信息
            return;
        }
        for (int i = front; i < front + size(); i++) {
            // 使用模运算处理循环队列的索引
            System.out.printf("arr[%d]=%d\n", i % maxSize, arr[i % maxSize]); // 打印每个有效元素
        }
    }

    /**
     * 计算队列中当前元素的数量。
     *
     * @return 队列中存储的有效元素个数。
     */
    private int size() {
        return (rear + maxSize - front) % maxSize; // 通过公式计算有效元素数量
    }

    /**
     * 查看队列头部的元素，但不移除它。
     *
     * @return 队列头部的元素。
     * @throws RuntimeException 如果队列为空，则抛出异常。
     */
    public int head() {
        if (isEmpty()) {
            throw new RuntimeException("队列为空，无法获取数据"); // 队列为空时抛出异常
        }
        return arr[front]; // 返回头指针指向的元素
    }
}