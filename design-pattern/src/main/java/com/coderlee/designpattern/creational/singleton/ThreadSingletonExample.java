package com.coderlee.designpattern.creational.singleton;

import com.coderlee.designpattern.creational.singleton.supports.SingletonTestUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 线程级别单例模式实现
 * <p>
 * 该实现确保每个线程都有且仅有一个实例，不同线程之间的实例相互独立。
 * 使用 ConcurrentHashMap 存储每个线程的实例，以线程 ID 作为键。
 * </p>
 * 适用场景：
 * <ul>
 *     <li>需要在线程内部保持状态一致性的场景</li>
 *     <li>每个线程需要独立的资源管理器或配置对象</li>
 *     <li>避免线程间共享状态导致的并发问题</li>
 * </ul>
 *
 * @author coderlee
 * @date 2026-03-04
 */
@Slf4j
public class ThreadSingletonExample {
    /**
     * 存储每个线程的单例实例
     * Key: 线程 ID
     * Value: 该线程对应的单例实例
     * 使用 ConcurrentHashMap 保证线程安全的并发访问
     */
    private static final ConcurrentHashMap<Long, ThreadSingletonExample> INSTANCES = new ConcurrentHashMap<>();

    /**
     * 私有构造函数，防止外部直接实例化
     * 确保只能通过 getInstance() 方法获取实例
     */
    private ThreadSingletonExample() {}

    /**
     * 获取当前线程的单例实例
     * <p>
     * 该方法会根据当前线程 ID 返回对应的实例：
     * - 如果当前线程已有实例，直接返回
     * - 如果当前线程没有实例，创建新实例并存储
     * </p>
     *
     * @return 当前线程对应的单例实例
     * @see Thread#getId() 获取当前线程 ID
     * @see ConcurrentHashMap#computeIfAbsent(Object, java.util.function.Function) 线程安全的原子操作
     */
    public static ThreadSingletonExample getInstance() {
        // 获取当前执行线程的唯一标识符
        long threadId = Thread.currentThread().getId();
        
        // 使用 computeIfAbsent 方法实现原子操作：
        // 1. 检查 map 中是否存在该 threadId 对应的实例
        // 2. 如果存在，直接返回现有实例
        // 3. 如果不存在，调用 lambda 表达式创建新实例并存入 map
        // 整个过程是线程安全的，无需额外的同步控制
        return INSTANCES.computeIfAbsent(threadId, k -> new ThreadSingletonExample());
    }

    /**
     * 主方法 - 测试线程级别单例模式
     * <p>
     * 使用 SingletonTestUtil 工具类来验证线程单例的正确性：
     * - 在多个线程中分别调用 getInstance() 方法
     * - 验证同一线程内多次调用返回相同实例
     * - 验证不同线程之间返回不同实例
     * </p>
     *
     * @param args 命令行参数（未使用）
     * @see SingletonTestUtil#testThreadSingleton(String, java.util.function.Supplier) 测试工具方法
     */
    public static void main(String[] args) {
        // 调用测试工具类，传入类名和 getInstance 方法引用进行测试
        SingletonTestUtil.testThreadSingleton(
            ThreadSingletonExample.class.getSimpleName(), 
            ThreadSingletonExample::getInstance
        );
    }
}
