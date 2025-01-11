package com.kaige.repository;

import com.kaige.entity.Category;
import com.kaige.entity.CategoryFetcher;
import com.kaige.entity.CategoryTable;
import com.kaige.entity.Tables;
import com.kaige.entity.dto.CategoryByIdView;
import com.kaige.entity.dto.CategoryInput;
import com.kaige.entity.dto.CategoryView;
import org.babyfish.jimmer.spring.repository.JRepository;
import org.babyfish.jimmer.spring.repository.support.SpringPageFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface CategoryRepository extends JRepository<Category,Long>, Tables {

    CategoryTable category = CategoryTable.$;
    default List<CategoryView> getCategoryNameList() {
        return sql().createQuery(category)
                .select(category.fetch(CategoryView.class))
                .execute();
    }

    default List<CategoryView> getCategoryList() {
        return sql().createQuery(category)
                .select(category.fetch(CategoryView.class))
                .execute();
    }

    default Category getCategoryById(BigInteger cate) {
        return sql().createQuery(category)
                .where(category.id().eq(cate))
                .select(category.fetch(
                        CategoryFetcher.$
                              .categoryName()
                        ))
                .fetchOneOrNull();
    }

    default Category getCategoryByName(String cate) {
        return sql().createQuery(category)
               .where(category.categoryName().eq(cate))
               .select(category.fetch(
                        CategoryFetcher.$
                               .categoryName()))
               .fetchOneOrNull();
    }

    default Page<CategoryView> getCategoryListOfPage(Integer pageNum, Integer pageSize) {
        return sql().createQuery(category)
                .orderBy(category.id().desc())
                .select(category.fetch(CategoryView.class))
                .fetchPage(pageNum-1, pageSize, SpringPageFactory.getInstance());
    }
}
