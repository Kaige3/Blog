package com.kaige.repository;

import com.kaige.entity.*;
import org.babyfish.jimmer.spring.repository.JRepository;
import org.babyfish.jimmer.sql.JSqlClient;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

public interface BlogRepository extends JRepository<Blog,Long>, Tables {

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
                .fetchOneOrNull();
    }

    default List<Blog> getSearchBlogListByQueryAndPublished(String trim){
        return sql().createQuery(blog)
                .where(blog.title().like("%"+trim+"%"))
                .select(blog.fetch(
                        BlogFetcher.$
                                .title()
                                .content()
                ))
                .execute();
    }

   default Boolean getCommentEnabledByBlogId(BigInteger blogId){
       @Nullable Blog blog1 = sql().createQuery(blog)
               .where(blog.id().eq(blogId))
               .select(blog.fetch(
                       BlogFetcher.$
                               .CommentEnabled()
               ))
               .fetchOneOrNull();
       assert blog1 != null;
       return blog1.CommentEnabled();
   }

   default Boolean getPublisheByBlogId(BigInteger blogId){
       Blog blog1 = sql().createQuery(blog)
               .where(blog.id().eq(blogId))
               .select(blog.fetch(
                       BlogFetcher.$
                               .Published()
               ))
               .fetchOneOrNull();
       assert blog1 != null;
       return blog1.Published();
   }
}
