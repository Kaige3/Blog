package com.kaige.repository;

import com.kaige.entity.*;
import org.babyfish.jimmer.spring.repository.JRepository;
import org.babyfish.jimmer.sql.JSqlClient;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

public interface BlogRepository  extends JRepository<Blog,Long>, Tables {

    BlogTable blog = BlogTable.$;
    default Blog getBlogByIdAndIsPublished(Long id){
        return sql()
               .createQuery(blog)
               .where(blog.id().eq(BigInteger.valueOf(id)))
               .where(blog.Published().eq(true))
                .select(blog.fetch(
                       BlogFetcher.$
                               .allScalarFields()
                               .category(
                                       CategoryFetcher.$
                                               .categoryName()
                               )
                               .tags(TagFetcher.$
                                       .allTableFields()
                               )))
                .fetchOne();
    }
}
