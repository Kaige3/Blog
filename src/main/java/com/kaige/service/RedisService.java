package com.kaige.service;

import com.kaige.entity.Blog;
import org.babyfish.jimmer.Page;

import java.util.HashMap;
import java.util.Map;

public interface RedisService {
     <T> Map<String,T> getMapByValue(String redisKey);

     <T> void saveMapToValue(String redisKey, HashMap<String, T> map);

    Page<Blog> getBlogInfoPageResultByPublish(String rediskey, Integer pageNum);

    void saveKVToHash(String rediskey, Object key , Object value);
}
