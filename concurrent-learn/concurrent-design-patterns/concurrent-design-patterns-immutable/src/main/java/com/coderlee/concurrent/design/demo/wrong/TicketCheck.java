package com.coderlee.concurrent.design.demo.wrong;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.slf4j.Slf4j;

/**
 * TicketCheck 类用于模拟一个检票系统，支持对用户信息的更新和查询。
 * 该类使用线程安全的 {@link ConcurrentHashMap} 来存储用户数据，确保在并发环境下的安全性。
 * 用户信息通过唯一的 `userKey` 进行标识，并支持更新用户名和身份证号。
 *
 * <p>注意：当前实现中存在潜在的线程安全问题，因为 {@link User} 对象本身不是线程安全的。</p>
 * 
 */
@Slf4j
public class TicketCheck {

    /**
     * 存储用户信息的线程安全映射表。
     * 键为用户唯一标识符 `userKey`，值为对应的 {@link User} 对象。
     */
    private Map<String, User> userMap = new ConcurrentHashMap<>();

    /**
     * 更新指定用户的用户名和身份证号。
     * 
     * @param userKey 唯一标识用户的键
     * @param userName 用户的新用户名
     * @param idCard 用户的新身份证号
     * 
     * <p>该方法首先从 {@link #userMap} 中获取用户对象，然后更新其属性，
     * 并将更新后的用户对象重新放入映射表中。</p>
     * 
     * <p>注意：由于 {@link User#set(String, Long)} 方法可能被多个线程同时调用，
     * 因此需要确保 {@link User} 的线程安全性。
     * 但是由于 {@link User} 本身不是不可变类，所以，并不是线程安全的
     * </p>
     */
    public void updateUser(String userKey, String userName, Long idCard) {
        // 从线程安全的映射表中获取用户对象
        User user = userMap.get(userKey);

        // 更新用户的用户名和身份证号
        user.set(userName, idCard);

        // 记录日志，输出当前线程名称和用户信息
        log.info("{}--当前检票的用户是: {}", Thread.currentThread().getName(), user.toString());

        // 将更新后的用户对象重新放入映射表
        userMap.put(userKey, user);
    }

    /**
     * 根据用户唯一标识符获取用户对象。
     * 
     * @param userKey 唯一标识用户的键
     * @return 返回与 `userKey` 关联的 {@link User} 对象，如果不存在则返回 null
     */
    public User getUser(String userKey) {
        return userMap.get(userKey);
    }
}