package com.kaige.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

public interface RedisService {
     <T> Map<String,T> getMapByValue(String redisKey);

     <T> void saveMapToValue(String redisKey, HashMap<String, T> map);
}
