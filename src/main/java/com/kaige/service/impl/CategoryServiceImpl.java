package com.kaige.service.impl;

import com.kaige.constant.RedisKeyConstants;
import com.kaige.entity.Category;
import com.kaige.entity.CategoryProps;
import com.kaige.entity.CategoryTable;
import com.kaige.entity.dto.CategoryInput;
import com.kaige.entity.dto.CategoryView;
import com.kaige.handler.exception.PersistenceException;
import com.kaige.repository.CategoryRepository;
import com.kaige.service.CategoryService;
import com.kaige.service.RedisService;
import org.babyfish.jimmer.sql.JSqlClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private RedisService redisService;

    JSqlClient jSqlClient;
    @Autowired
    public CategoryServiceImpl(JSqlClient jSqlClient) {
        this.jSqlClient = jSqlClient;
    }


    @Override
    public List<CategoryView> getCategoryNameList() {
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

    @Override
    public List<CategoryView> getCategoryList() {
        return categoryRepository.getCategoryList();
    }

    @Override
    public Category getCategoryById(BigInteger cate) {
        return categoryRepository.getCategoryById(cate);
    }

    @Override
    public Category getCategoryByName(String cate) {
        return categoryRepository.getCategoryByName(cate);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveCategory(Category produce) {
        try {
            categoryRepository.save(produce);
        } catch (Exception e) {
            throw new PersistenceException("添加分类失败");
        }
        redisService.deleteCacheByKey(RedisKeyConstants.CATEGORY_NAME_LIST);
    }

    @Override
    public Page<CategoryView> getCategoryListOfPage(Integer pageNum, Integer pageSize) {
        return categoryRepository.getCategoryListOfPage(pageNum, pageSize);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCategory(Category categoryInput) {
        try {
            categoryRepository.update(categoryInput);
        } catch (Exception e) {
            throw new PersistenceException("更新分类失败");
        }
        redisService.deleteCacheByKey(RedisKeyConstants.CATEGORY_NAME_LIST);
        redisService.deleteCacheByKey(RedisKeyConstants.HOME_BLOG_INFO_LIST);
    }

    @Override
    public void deleteCategory(BigInteger id) {
        CategoryTable categoryTable = CategoryTable.$;
        jSqlClient.deleteById(categoryTable.getClass(),id);

        redisService.deleteCacheByKey(RedisKeyConstants.CATEGORY_NAME_LIST);
    }
}
