package com.coderlee.concurrent.chapter17;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import lombok.extern.slf4j.Slf4j;

/**
 * 线程安全的读写缓存实现类。
 * <p>
 * 该类通过 {@link ReadWriteLock} 实现了高效的并发读写操作，适用于多线程环境下对缓存数据的访问。
 * 缓存使用 {@link HashMap} 存储键值对，并通过读写锁保证数据一致性。
 * </p>
 *
 * @param <K> 缓存中键的类型
 * @param <V> 缓存中值的类型
 */
@Slf4j
public class ConcurrentReadWriteCache<K, V> implements ReadWriteCache<K, V> {

    /**
     * 使用 volatile 修饰的缓存存储容器，确保多线程环境下的可见性。
     */
    private volatile Map<K, V> map = new HashMap<>();

    /**
     * 读写锁实例，用于控制并发访问。
     */
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * 读锁，允许多个线程同时读取缓存数据。
     */
    private final Lock readLock = lock.readLock();

    /**
     * 写锁，确保只有一个线程能够修改缓存数据。
     */
    private final Lock writeLock = lock.writeLock();

    /**
     * 将指定的键值对存入缓存中。
     * <p>
     * 写操作通过写锁保证线程安全。如果缓存中已经存在相同的键，则其对应的值将被新值替换。
     * </p>
     *
     * @param key   要存储的键，不能为空
     * @param value 要存储的值，可以为空
     */
    @Override
    public void put(K key, V value) {
        try {
            writeLock.lock(); // 获取写锁，确保独占写操作
            log.info("{} 写数据开始", Thread.currentThread().getName());
            map.put(key, value); // 将键值对存入缓存
        } finally {
            log.info("{} 写数据结束", Thread.currentThread().getName());
            writeLock.unlock(); // 释放写锁
        }
    }

    /**
     * 根据指定的键从缓存中获取对应的值。
     * <p>
     * 如果缓存中不存在该键，则尝试从数据库中加载数据并将其写入缓存。
     * 读操作通过读锁保证高效并发访问，而写操作通过写锁保证线程安全。
     * </p>
     *
     * @param key 要检索的键，不能为空
     * @return 与指定键关联的值，如果键不存在则返回 null
     */
    @Override
    public V get(K key) {
        V value = null;
        try {
            readLock.lock(); // 获取读锁，允许多线程并发读取
            log.info("{} 读数据开始", Thread.currentThread().getName());
            value = map.get(key); // 尝试从缓存中获取值
        } finally {
            log.info("{} 读数据结束", Thread.currentThread().getName());
            readLock.unlock(); // 释放读锁
        }
        if (null != value) { // 如果缓存命中，则直接返回值
            return value;
        }
        try {
            writeLock.lock(); // 获取写锁，确保独占写操作
            // 二次验证缓存中是否存在数据
            /* 这是因为在高并发的场景下，可能会存在多个线程来竞争写锁的现象。例如：第一次执行get()方法时，缓存中的数据为空。如果
            此时有三个线程同时调用get()方法，同时运行到 w.lock() 代码处，由于写锁的排他性。此时只有一个线程会获取到写锁，其他
            两个线程则阻塞在 w.lock() 处。获取到写锁的线程继续往下执行查询数据库，将数据写入缓存，之后释放写锁。
            此时，另外两个线程竞争写锁，某个线程会获取到锁，继续往下执行，如果在 w.lock() 后没有 v = m.get(key); 再次查询缓存
            的数据，则这个线程会直接查询数据库，将数据写入缓存后释放写锁。最后一个线程同样会按照这个流程执行。
            这里，实际上第一个线程已经查询过数据库，并且将数据写入缓存了，其他两个线程就没必要再次查询数据库了，直接从缓存中
            查询出相应的数据即可。所以，在 w.lock() 后添加 v = m.get(key); 再次查询缓存的数据，能够有效的减少高并发场景下重复
            查询数据库的问题，提升系统的性能。 */
            value = map.get(key);
            if (null == value) {
                log.info("{} 缓存数据不存在，从数据库中读取数据并写入缓存开始", Thread.currentThread().getName());
                value = getvalueFromDB(key); // 从数据库中加载数据
                map.put(key, value); // 将数据写入缓存
            }
        } finally {
            log.info("{} 从数据库中读取数据并写入缓存结束", Thread.currentThread().getName());
            writeLock.unlock(); // 释放写锁
        }
        return value; // 返回最终获取的值
    }

    /**
     * 模拟从数据库中加载数据的方法。
     * <p>
     * 该方法仅为示例实现，实际项目中应替换为真实的数据库查询逻辑。
     * </p>
     *
     * @param key 要检索的键
     * @return 从数据库中加载的值
     */
    private V getvalueFromDB(K key) {
        return (V) ("coderlee_" + key); // 模拟数据库返回值
    }
}