package com.kaige.service.impl;

import com.kaige.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    RedisTemplate redisTemplate;
    @Override
    // 从 Redis 中获取 Map 数据
    public <T> Map<String , T> getMapByValue(String redisKey) {
        Map<String,T > redisResult = (Map<String, T>) redisTemplate.opsForValue().get(redisKey);
        return redisResult;
    }

    @Override
    // 将 Map 数据保存到 Redis 中
    public <T> void saveMapToValue(String redisKey, HashMap<String, T> map) {
        redisTemplate.opsForValue().set(redisKey,map);
    }
}
