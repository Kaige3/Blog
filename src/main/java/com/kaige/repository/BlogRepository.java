package com.kaige.repository;

import com.kaige.entity.*;
import com.kaige.entity.dto.*;
import org.babyfish.jimmer.Page;
import org.babyfish.jimmer.spring.repository.JRepository;
import org.babyfish.jimmer.sql.JSqlClient;
import org.babyfish.jimmer.sql.ast.Predicate;
import org.babyfish.jimmer.sql.ast.query.ConfigurableRootQuery;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

public interface BlogRepository extends JRepository<Blog,Long>, Tables {

    BlogTable blog = BlogTable.$;
    default BlogDetailView getBlogByIdAndIsPublished(Long id){
        return sql()
                .createQuery(blog)
                .where(blog.id().eq(BigInteger.valueOf(id)))
                .where(blog.Published().eq(true))
                .select(blog.fetch(
                        BlogDetailView.class
                ))
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


    default List<NewBlogView> getNewBlogListByIsPublished(int newBlogPageSize) {

         return sql().createQuery(blog)
                .where(blog.Published().eq(true))
                .orderBy(blog.createTime().desc())
                .select(blog.fetch(
                        NewBlogView.class
                ))
                .limit(newBlogPageSize)
                .execute();

    }

    default List<RandomBlogView> getRandomBlogList() {
//        return sql().createQuery(blog)
//                .where(blog.Published().eq(true))
//                .where(blog.Recommend().eq(true))
//                .orderBy(Predicate.sql("%v", it -> it.value("rand()")))
//                .select(blog.fetch(RandomBlogView.class))
//                .execute();
        List<RandomBlogView> execute = sql().createQuery(blog)
                .where(blog.Published().eq(true))
                .where(blog.Recommend().eq(true))
                .orderBy(Predicate.sql("RAND()"))
                .select(blog.fetch(RandomBlogView.class))
                .limit(2)
                .execute();
        return execute;
    }

    default Page<BlogInfoView> getBlogListByIsPublished(Integer pageNum,Integer pageSize,String orderBy) {
        return sql().createQuery(blog)
                .where(blog.Published().eq(true))
                .orderBy(Predicate.sql("%v", it -> it.value(orderBy)))
                .select(blog.fetch(
                        BlogInfoView.class
                ))
                .fetchPage(pageNum-1, pageSize);
    }

    default List<BLogViewsView> getBlogViewMap() {
         return sql().createQuery(blog)
                .select(blog.fetch(BLogViewsView.class))
                .execute();
    }
}
