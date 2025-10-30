package com.coderlee.concurrent.chapter19.service.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.coderlee.concurrent.chapter19.service.RedisDistributeLock;

@Service("redisDistributeLockV3")
public class RedisDistributeLockV3Impl implements RedisDistributeLock {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    private ThreadLocal<String> threadLocal = new ThreadLocal<String>();
    @Override
    public boolean tryLock(String key, long timeout, TimeUnit unit) {
        Boolean isLocked = false;
        if (threadLocal.get() == null){
            String currentThreadId = this.getCurrentThreadId();
            threadLocal.set(currentThreadId);
            isLocked = stringRedisTemplate.opsForValue().setIfAbsent(key, currentThreadId, timeout, unit);
        }else{
            isLocked = true;
        }
        return isLocked;
    }

    @Override
    public void releaseLock(String key) {
        //当前线程中绑定的线程id与Redis中的线程id相同时，再执行删除锁的操作
        if (threadLocal.get().equals(stringRedisTemplate.opsForValue().get(key))){
            stringRedisTemplate.delete(key);
            //防止内存泄露
            threadLocal.remove();
        }
    }

    private String getCurrentThreadId(){
        return String.valueOf(Thread.currentThread().getId());
    }
}