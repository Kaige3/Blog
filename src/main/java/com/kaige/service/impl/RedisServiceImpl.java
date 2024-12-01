package com.kaige.service.impl;

import com.kaige.entity.Blog;
import com.kaige.entity.vo.FriendInfoVo;
import com.kaige.service.RedisService;
import com.kaige.utils.JacksonUtils;
import org.babyfish.jimmer.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RedisServiceImpl implements RedisService {

    @Autowired(required = true)
    RedisTemplate redisTemplate;
    @Override
    // 从 Redis 中获取 Map 数据
    public <T> Map<String , T> getMapByValue(String redisKey) {
        return (Map<String, T>) redisTemplate.opsForValue().get(redisKey);
    }

    @Override
    // 将 Map 数据保存到 Redis 中
    public <T> void saveMapToValue(String redisKey, HashMap<String, T> map) {
        redisTemplate.opsForValue().set(redisKey,map);
    }

    @Override
    public Page<Blog> getBlogInfoPageResultByPublish(String rediskey, Integer pageNum) {
         if(redisTemplate.opsForHash().hasKey(rediskey,pageNum)){
             Object object = redisTemplate.opsForHash().get(rediskey, pageNum);
             Page<Blog> page = JacksonUtils.convertValue(object, Page.class);
             return page;
         }
        return null;
    }

    @Override
    public void saveKVToHash(String rediskey, Object key, Object value) {
        redisTemplate.opsForHash().put(rediskey,key,value);
    }

    @Override
    public <T> T getObjectByValue(String redisKey, Class t) {
        Object object = redisTemplate.opsForValue().get(redisKey);
        T object1 = (T) JacksonUtils.convertValue(object, t);
        return object1;
    }

    @Override
    public void saveObjectToValue(String redisKey, FriendInfoVo friendInfoVo1) {
        redisTemplate.opsForValue().set(redisKey,friendInfoVo1);
    }

    @Override
    public Object getValueByHashKey(String hash, Object key) {
        return redisTemplate.opsForHash().get(hash,key);
    }
}
