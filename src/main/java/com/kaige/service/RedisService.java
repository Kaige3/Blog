package com.kaige.service;

import com.kaige.entity.Blog;
import com.kaige.entity.SiteSetting;
import com.kaige.entity.dto.NewBlogView;
import com.kaige.entity.vo.FriendInfoVo;
import org.babyfish.jimmer.Page;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface RedisService {
     <T> Map<String,T> getMapByValue(String redisKey);

     <T> void saveMapToValue(String redisKey, HashMap<String, T> map);

    Page<Blog> getBlogInfoPageResultByPublish(String rediskey, Integer pageNum);

    void saveKVToHash(String rediskey, Object key , Object value);

    <T> T getObjectByValue(String redisKey, Class t);

    void saveObjectToValue(String redisKey, FriendInfoVo friendInfoVo1);

    Object getValueByHashKey(String hash, Object key);

    <T>List<T> getListByValues(String newBlogListKey);

    <T> void saveListToValue(String newBlogListKey, List<T> newBlogViews);
}
