package com.kaige.service.impl;

import com.kaige.entity.dto.CategoryView;
import com.kaige.repository.CategoryRepository;
import com.kaige.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;


    @Override
    public List<CategoryView> getCategoryNameList() {
        //TODO 先从redis中查询数据，如果有，直接返回
        // 否则，从数据库中查询数据，然后存入redis中
         List<CategoryView> categoryViews = categoryRepository.getCategoryNameList();
         return categoryViews;
    }
}
