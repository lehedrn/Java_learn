package com.coderlee.concurrent.design.demo.right;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.slf4j.Slf4j;

/**
 * 检票服务类，用于管理用户的检票信息
 * 该类使用 {@link ConcurrentHashMap} 来存储用户信息，保证了多线程环境下的线程安全性。
 * 同时通过返回 {@link Collections#unmodifiableMap(Map)} 来防止外部代码修改内部的用户映射表，
 * 采用了防御性复制，实现了不可变对象的设计模式，提高并发安全性。
 *
 * @author coderlee
 * @see ConcurrentHashMap
 * @see Collections#unmodifiableMap(Map)
 */
@Slf4j
public class TicketCheck {

    /**
     * 存储用户信息的线程安全映射表
     * 使用 {@link ConcurrentHashMap} 保证并发访问的安全性
     */
    private Map<String, User> userMap = new ConcurrentHashMap<>();

    /**
     * 更新指定用户的检票信息
     *
     * @param userKey 用户的唯一标识键
     * @param user 用户对象，包含用户的检票信息
     */
    public void updateUser(String userKey, User user) {
        // 记录当前检票的用户信息，包括线程名称和用户详情
        log.info("{}--当前检票的用户是: {}", Thread.currentThread().getName(), user.toString());
        // 将用户信息存入映射表中
        userMap.put(userKey, user);
    }

    /**
     * 根据用户键获取用户信息
     *
     * @param userKey 用户的唯一标识键
     * @return 对应的用户对象，如果不存在则返回null
     */
    public User getUser(String userKey) {
        return userMap.get(userKey);
    }

    /**
     * 获取所有用户的不可修改映射表
     * 使用 {@link Collections#unmodifiableMap(Map)} 包装原始映射表，
     * 防止外部代码修改内部数据结构，实现不可变对象模式
     *
     * @return 不可修改的用户映射表视图
     * @see Collections#unmodifiableMap(Map)
     */
    public Map<String, User> getUserMap() {
        // 返回原始映射表
        // 不可取，会导致线程不安全，外部可能会修改userMap
//        return userMap;
        // 返回不可修改的映射表，保护内部数据不被外部修改
        return Collections.unmodifiableMap(userMap);
    }

    /**
     * 打印当前所有用户的检票信息
     * 主要用于调试和日志记录
     */
    public void printUserMap() {
        // 输出当前用户映射表的所有内容
        log.info("当前userMap为====>>: {}", userMap);
    }
}
