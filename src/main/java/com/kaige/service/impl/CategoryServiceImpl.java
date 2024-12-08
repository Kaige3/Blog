package com.kaige.service.impl;

import com.kaige.constant.RedisKeyConstants;
import com.kaige.entity.dto.CategoryView;
import com.kaige.repository.CategoryRepository;
import com.kaige.service.CategoryService;
import com.kaige.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private RedisService redisService;


    @Override
    public List<CategoryView> getCategoryNameList() {
        //TODO 先从redis中查询数据，如果有，直接返回
        String categoryNameListKey = RedisKeyConstants.CATEGORY_NAME_LIST;
        List<CategoryView> categoryViewsFromRedis = redisService.getListByValues(categoryNameListKey);
        if(categoryViewsFromRedis!= null){
            return categoryViewsFromRedis;
        }
        // 否则，从数据库中查询数据，然后存入redis中
         List<CategoryView> categoryViews = categoryRepository.getCategoryNameList();
         redisService.saveListToValue(categoryNameListKey,categoryViews);
         return categoryViews;
    }
}
