package com.coderlee.concurrent.chapter19.service;

import java.util.concurrent.TimeUnit;

public interface RedisDistributeLock {

    /**
     * 加锁
     */
    boolean tryLock(String key, long timeout, TimeUnit unit);

    /**
     * 解锁
     */
    void releaseLock(String key);

}
