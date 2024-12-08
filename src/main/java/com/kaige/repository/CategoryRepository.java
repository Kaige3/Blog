package com.kaige.repository;

import com.kaige.entity.Category;
import com.kaige.entity.CategoryTable;
import com.kaige.entity.Tables;
import com.kaige.entity.dto.CategoryView;
import org.babyfish.jimmer.spring.repository.JRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JRepository<Category,Long>, Tables {

    CategoryTable category = CategoryTable.$;
    default List<CategoryView> getCategoryNameList() {
        return sql().createQuery(category)
                .select(category.fetch(CategoryView.class))
                .execute();
    }
}
