/**
 * CopyOnWriteArrayList 测试类
 * {@link java.util.concurrent.CopyOnWriteArrayList} 是等效不可变类
 * 本类演示了 {@link java.util.concurrent.CopyOnWriteArrayList} 的行为特性，
 * 特别是其"写时复制"(Copy-On-Write)机制如何保证线程安全性以及对集合元素修改的可见性。
 *
 * <p>CopyOnWriteArrayList 是一种线程安全的 List 实现，适用于读多写少的并发场景。
 * 其核心思想是在写操作时创建底层数组的新副本，从而避免了读写操作之间的锁竞争。</p>
 *
 * @see java.util.concurrent.CopyOnWriteArrayList
 */
package com.coderlee.concurrent.design.cow;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public class CopyOnWriteArrayListTest {

    /**
     * 测试 CopyOnWriteArrayList 的基本行为
     *
     * <p>此方法验证了 CopyOnWriteArrayList 的一个重要特性：
     * 它保存的是对象的引用而非对象的副本，因此当原始对象被修改后，
     * 从列表中获取的对象也会反映出这些更改。</p>
     *
     * <p>注意：这与 CopyOnWriteArrayList 在写入时复制数组的行为不同，
     * 后者解决的是多个线程同时访问集合的安全问题，而不是保护集合内对象的状态。</p>
     *
     * @see java.util.concurrent.CopyOnWriteArrayList
     * @see User
     */
    @Test
    public void testCopyOnWriteArrayList() {
        // 创建一个线程安全的 CopyOnWriteArrayList 实例
        List<User> list = new CopyOnWriteArrayList<>();

        // 创建一个新的 User 对象并添加到列表中
        User user = new User("coderlee");
        list.add(user);

        // 获取列表中的第一个元素（即刚才添加的 user 对象）
        User user1 = list.get(0);

        // 打印 user1 的哈希码和数据内容
        log.info("user1对象的hashCode为===>> {}, 数据为===>> {}", user1.hashCode(), user1.toString());

        // 修改原始 user 对象的名字属性
        user.setName("coderlee002");

        // 再次从列表中获取相同的对象引用
        User user2 = list.get(0);

        // 打印 user2 的哈希码和数据内容，观察是否受到前面修改的影响
        log.info("user2对象的hashCode为===>> {}, 数据为===>> {}", user2.hashCode(), user2.toString());
    }
}
