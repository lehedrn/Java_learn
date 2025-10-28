package com.coderlee.concurrent.chapter17;

/**
 * 读写缓存接口，定义了缓存的基本操作。
 * <p>
 * 该接口提供了一种通用的读写缓存机制，允许用户通过键值对的形式存储和检索数据。
 * 缓存的操作包括写入（put）和读取（get），适用于需要高效数据访问的场景。
 * </p>
 *
 * @param <K> 缓存中键的类型
 * @param <V> 缓存中值的类型
 */
public interface ReadWriteCache<K, V> {

    /**
     * 将指定的键值对存入缓存中。
     * <p>
     * 如果缓存中已经存在相同的键，则其对应的值将被新值替换。
     * </p>
     *
     * @param key   要存储的键，不能为空
     * @param value 要存储的值，可以为空
     */
    void put(K key, V value);

    /**
     * 根据指定的键从缓存中获取对应的值。
     * <p>
     * 如果缓存中不存在该键，则返回 null。
     * </p>
     *
     * @param key 要检索的键，不能为空
     * @return 与指定键关联的值，如果键不存在则返回 null
     */
    V get(K key);
}