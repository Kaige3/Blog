package com.kaige.service;

import com.kaige.entity.Category;
import com.kaige.entity.dto.CategoryInput;
import com.kaige.entity.dto.CategoryView;
import org.springframework.data.domain.Page;

import java.math.BigInteger;
import java.util.List;

public interface CategoryService {
    List<CategoryView> getCategoryNameList();

    List<CategoryView> getCategoryList();

    Category getCategoryById(BigInteger cate);

    Category getCategoryByName(String cate);

    void saveCategory(Category produce);

    Page<CategoryView> getCategoryListOfPage(Integer pageNum, Integer pageSize);

    void updateCategory(Category categoryInput);

    void deleteCategory(BigInteger id);
}
