package com.coderlee.concurrent.chapter20.service.impl;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import com.coderlee.concurrent.chapter20.service.SeckillService;

public class SeckillServiceImpl implements SeckillService {
    private static final String GOODS_CACHE = "seckill:goodsStock:";
    private static final String SECKILL_LUA_SCRIPT =
            "local resultFlag = \"0\" \n" +
            "local n = tonumber(ARGV[1]) \n" +
            "local key = KEYS[1] \n" +
            "local goodsInfo = redis.call(\"HMGET\",key,\"totalCount\",\"seckillCount\") \n" +
            "local total = tonumber(goodsInfo[1]) \n" +
            "local seckill = tonumber(goodsInfo[2]) \n" +
            "if not total then \n" +
            "    return resultFlag \n" +
            "end \n" +
            "if total >= seckill + n  then \n" +
            "    local ret = redis.call(\"HINCRBY\",key,\"seckillCount\",n) \n" +
            "    return tostring(ret) \n" +
            "end \n" +
            "return resultFlag\n";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    private String getCacheKey(String id) {
        return GOODS_CACHE.concat(id);
    }
    @Override
    public int secKill(String id, int number) {
        String key = getCacheKey(id);
        DefaultRedisScript<String> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(SECKILL_LUA_SCRIPT);
        redisScript.setResultType(String.class);
        Object seckillCount =  redisTemplate.execute(redisScript, Arrays.asList(key), String.valueOf(number));
        return Integer.valueOf(seckillCount.toString());
    }
}
