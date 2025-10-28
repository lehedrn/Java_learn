package com.coderlee.concurrent.chapter17;

import java.util.stream.IntStream;

/**
 * 测试类，用于验证 {@link ReadWriteCache} 的实现类在多线程环境下的读写操作。
 * <p>
 * 该类通过创建多个线程模拟并发场景，测试缓存的写入和读取功能是否正确且线程安全。
 * </p>
 */
public class ReadWriteCacheTest {

    /**
     * 主方法，程序入口点。
     * <p>
     * 该方法执行以下操作：
     * <ol>
     *   <li>创建一个 {@link ConcurrentReadWriteCache} 实例。</li>
     *   <li>启动多个线程并发写入数据到缓存中。</li>
     *   <li>启动多个线程并发从缓存中读取数据。</li>
     * </ol>
     * </p>
     *
     * @param args 命令行参数（未使用）
     */
    public static void main(String[] args) {
        // 创建缓存实例
        ReadWriteCache<String, String> readWriteCache = new ConcurrentReadWriteCache<>();

        // 模拟并发写入操作
        IntStream.range(0, 5).forEach(i -> {
            new Thread(() -> {
                String key = "name_".concat(String.valueOf(i)); // 构造键
                String value = "coderlee_".concat(String.valueOf(i)); // 构造值
                readWriteCache.put(key, value); // 写入缓存
            }, "Thread-" + i).start(); // 启动线程
        });

        // 模拟并发读取操作
        IntStream.range(0, 10).forEach(i -> {
            new Thread(() -> {
                String key = "name_".concat(String.valueOf(i)); // 构造键
                readWriteCache.get(key); // 从缓存中读取数据
            }, "Thread-" + i).start(); // 启动线程
        });
    }
}
