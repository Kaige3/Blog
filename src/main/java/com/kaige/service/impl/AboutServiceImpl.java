package com.kaige.service.impl;

import com.kaige.constant.RedisKeyConstants;
import com.kaige.entity.About;
import com.kaige.handler.exception.PersistenceException;
import com.kaige.repository.AboutRepository;
import com.kaige.service.AboutService;
import com.kaige.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class AboutServiceImpl implements AboutService {

    @Autowired
    private AboutRepository aboutRepository;
    @Autowired
    private RedisService redis;
    @Override
    public boolean getAboutCommentEnabled() {
        return aboutRepository.getAboutCommentEnabled();
    }

    @Override
    public Map<String, String> getAboutInfo() {
        List<About> aboutList = aboutRepository.getAboutInfo();
        HashMap<String, String> map = new HashMap<>(16);
        for (About about : aboutList) {
            map.put(about.nameEn(),about.value());
        }
        return map;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAbout(Map<String, String> map) {
        Set<String> keySet = map.keySet();
        for (String key : keySet) {
            updateONEAbout(key,map.get(key));
        }
        deleteAboutRedisCache();
    }

    /**
     * @Description: 删除关于我的缓存
     * @Author: Kaige
     * @Date: 2020-07-21
     */
    private void deleteAboutRedisCache() {
        redis.deleteCacheByKey(RedisKeyConstants.ABOUT_INFO_MAP);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateONEAbout(String key, String value) {
        if (aboutRepository.updateONEAbout(key,value) !=1){
            throw new PersistenceException("修改失败");
        }
    }
}
