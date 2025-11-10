/**
 * 资源请求者类，用于管理资源的申请和释放操作。
 *
 * 该类实现了简单的资源锁定机制，防止重复申请相同的资源，
 * 从而避免在多线程环境中出现死锁问题。
 *
 * @see <a href="https://docs.oracle.com/javase/tutorial/essential/concurrency/deadlock.html">Deadlock in Java</a>
 */
package com.coderlee.guarded.suspension.lock.deadlock.demo2;

import java.util.ArrayList;
import java.util.List;

public class ResourcesRequester {

    /**
     * 存储已申请的资源列表
     */
    private List<Object> resources = new ArrayList<>();

    /**
     * 尝试申请两个资源
     *
     * 如果任一资源已被其他线程持有，则拒绝本次申请并返回false；
     * 否则将两个资源都标记为已占用状态并返回true。
     *
     * @param source 第一个资源对象
     * @param target 第二个资源对象
     * @return 如果成功获取了两个资源返回true，否则返回false
     */
    public synchronized boolean applyResources(Object source, Object target) {
        // 检查是否有任意一个资源已经被占用
        if (resources.contains(source) || resources.contains(target)) {
            return false;
        }

        // 添加两个资源到已占用列表中
        resources.add(source);
        resources.add(target);
        return true;
    }

    /**
     * 释放两个资源
     *
     * 移除指定的两个资源从已占用列表中，使它们可以被其他线程再次申请。
     *
     * @param source 第一个资源对象
     * @param target 第二个资源对象
     */
    public synchronized void releaseResources(Object source, Object target) {
        // 从已占用列表中移除两个资源
        resources.remove(source);
        resources.remove(target);
    }
}
