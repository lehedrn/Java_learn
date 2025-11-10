package com.coderlee.guarded.suspension.lock.buffer;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 请求缓存缓冲区实现类，基于Guarded Suspension模式实现线程安全的请求队列。
 * <p>
 * 该类使用{@link ReentrantLock}和{@link Condition}来协调生产者和消费者线程，
 * 当缓冲区为空时消费者线程会被阻塞，当缓冲区满时生产者线程会被阻塞。
 * </p>
 *
 * @see <a href="https://java-design-patterns.com/patterns/guarded-suspension/">Guarded Suspension设计模式</a>
 */
public class RequestCacheBuffer {

    /**
     * 缓冲区大小限制
     */
    private static final int LIMIT = 1024;

    /**
     * 存储请求的队列，使用{@link ArrayBlockingQueue}实现有界队列
     */
    private final Queue<Request> queue = new ArrayBlockingQueue<>(LIMIT);

    /**
     * 用于保护共享资源的可重入锁
     */
    private final Lock lock = new ReentrantLock();

    /**
     * 非满条件变量，用于通知生产者可以继续添加元素
     */
    private final Condition notFull = lock.newCondition();

    /**
     * 非空条件变量，用于通知消费者可以继续消费元素
     */
    private final Condition notEmpty = lock.newCondition();

    /**
     * 从缓冲区获取一个请求对象。
     * <p>
     * 如果当前缓冲区为空，则当前线程会被阻塞直到有新的请求被添加到缓冲区中。
     * </p>
     *
     * @return 从缓冲区中取出的请求对象，如果线程被中断则可能返回null
     */
    public Request get() {
        // 初始化返回结果
        Request request = null;
        // 获取锁
        lock.lock();
        try {
            // 当队列为空时，等待直到有元素可用
            while (queue.isEmpty()) {
                notEmpty.await();
            }
            // 从队列中取出一个请求
            request = queue.poll();
            // 通知所有等待在notFull条件上的线程，现在队列不再满了
            notFull.signalAll();
        } catch (InterruptedException e) {
            // 如果线程被中断，同样通知所有等待在notFull条件上的线程
            notFull.signalAll();
        } finally {
            // 释放锁
            lock.unlock();
        }
        return request;
    }

    /**
     * 向缓冲区添加一个请求对象。
     * <p>
     * 如果当前缓冲区已满，则当前线程会被阻塞直到有空间可用。
     * </p>
     *
     * @param request 要添加到缓冲区的请求对象
     */
    public void put(Request request) {
        // 获取锁
        lock.lock();
        try {
            // 当队列已满时，等待直到有空间可用
            while (queue.size() >= LIMIT) {
                notFull.await();
            }
            // 将请求添加到队列中
            queue.offer(request);
            // 通知所有等待在notEmpty条件上的线程，现在队列不为空了
            notEmpty.signalAll();
        } catch (InterruptedException e) {
            // 如果线程被中断，同样通知所有等待在notEmpty条件上的线程
            notEmpty.signalAll();
        } finally {
            // 释放锁
            lock.unlock();
        }
    }
}
