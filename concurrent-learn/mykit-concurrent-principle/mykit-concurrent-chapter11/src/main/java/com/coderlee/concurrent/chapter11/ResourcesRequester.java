package com.coderlee.concurrent.chapter11;

import java.util.ArrayList;
import java.util.List;

/**
 * ResourcesRequester 类用于管理资源的申请与释放操作。
 * <p>
 * 该类的核心功能是确保资源能够被一次性申请和释放，避免部分资源占用的情况。
 * 主要应用场景为需要同时获取多个资源的并发环境，防止死锁或资源竞争问题。
 */
public class ResourcesRequester {

    // 存放已申请资源的集合，确保资源的状态能够被统一管理
    private List<Object> resources = new ArrayList<Object>();

    /**
     * 尝试一次性申请所有所需的资源。
     * <p>
     * 如果任意一个资源已经被占用，则申请失败并返回 false；否则，将资源加入集合并返回 true。
     *
     * @param source 需要申请的第一个资源对象
     * @param target 需要申请的第二个资源对象
     * @return 如果资源申请成功，返回 true；如果资源已被占用，返回 false
     */
    public synchronized boolean applyResources(Object source, Object target) {
        // 检查资源是否已被占用
        if (resources.contains(source) || resources.contains(target)) {
            return false; // 资源已被占用，申请失败
        }
        // 将资源添加到已申请集合中
        resources.add(source);
        resources.add(target);
        return true; // 资源申请成功
    }

    /**
     * 释放指定的资源。
     * <p>
     * 该方法会从资源集合中移除对应的资源对象，确保资源可以被其他线程使用。
     *
     * @param source 需要释放的第一个资源对象
     * @param target 需要释放的第二个资源对象
     */
    public synchronized void releaseResources(Object source, Object target) {
        // 从资源集合中移除指定的资源
        resources.remove(source);
        resources.remove(target);
    }
}