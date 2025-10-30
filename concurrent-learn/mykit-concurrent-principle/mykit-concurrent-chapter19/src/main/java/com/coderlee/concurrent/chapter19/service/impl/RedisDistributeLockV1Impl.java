package com.coderlee.concurrent.chapter19.service.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.coderlee.concurrent.chapter19.service.RedisDistributeLock;

@Service("redisDistributeLockV1")
public class RedisDistributeLockV1Impl implements RedisDistributeLock {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public boolean tryLock(String key, long timeout, TimeUnit unit) {
        return stringRedisTemplate.opsForValue().setIfAbsent(key, this.getCurrentThreadId(), timeout, unit);
    }

    @Override
    public void releaseLock(String key) {
        stringRedisTemplate.delete(key);
    }

    private String getCurrentThreadId(){
        return String.valueOf(Thread.currentThread().getId());
    }
}