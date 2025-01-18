package com.kaige.repository;

import com.kaige.entity.*;
import com.kaige.entity.dto.*;
import org.babyfish.jimmer.Page;
import org.babyfish.jimmer.spring.repository.JRepository;
import org.babyfish.jimmer.spring.repository.support.SpringPageFactory;
import org.babyfish.jimmer.sql.ast.Predicate;
import org.jetbrains.annotations.Nullable;

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

        return sql().createQuery(blog)
                .where(blog.Published().eq(true))
                .where(blog.Recommend().eq(true))
                .orderBy(Predicate.sql("RAND()"))
                .select(blog.fetch(RandomBlogView.class))
                .limit(5)
                .execute();
    }

    default Page<BlogInfoView> getBlogListByIsPublished(Integer pageNum,Integer pageSize,String orderBy) {
        return sql().createQuery(blog)
                .where(blog.Published().eq(true))
                .orderBy(Predicate.sql("%v", it -> it.value(orderBy)))
                .orderBy(blog.Top().desc())
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

    default org.springframework.data.domain.Page<BlogDetailView> getBlogList(String title, Integer categoryId, Integer pageNum, Integer pageSize) {
        org.springframework.data.domain.Page<BlogDetailView> blogDetailViews = sql().createQuery(blog)
                .whereIf(categoryId != null,()-> blog.categoryId().eq(BigInteger.valueOf(categoryId)))
                .whereIf(title != null, ()->blog.title().like(title))
                .orderBy(blog.createTime().desc())
                .select(blog.fetch(
                        BlogDetailView.class
                ))
                .fetchPage(0, 10, SpringPageFactory.getInstance());
        return blogDetailViews;
    }

    default Integer updateTop(BigInteger id, Boolean top) {
        return sql().createUpdate(blog)
                .where(blog.id().eq(id))
                .set(blog.Top(),top)
                .execute();
    }

    default Integer updateRecommend(BigInteger id, Boolean recommend) {
        return sql().createUpdate(blog)
                .where(blog.id().eq(id))
                .set(blog.Recommend(), recommend)
                .execute();
    }

    default Integer updatePublished(BigInteger id, Boolean published) {
        return sql().createUpdate(blog)
                .where(blog.id().eq(id))
                .set(blog.Published(), published)
                .execute();
    }

    default BlogDetailView getBlogById(BigInteger id) {
       return sql().createQuery(blog)
                .where(blog.id().eq(id))
                .select(blog.fetch(
                        BlogDetailView.class
                ))
                .fetchOneOrNull();
    }

    default int saveBlogTag(BigInteger blogId, BigInteger tagId) {
         return sql().getAssociations(BlogProps.TAGS)
                .save(blogId,tagId);
    }

    default long countBlogByCategoryId(BigInteger id) {
        return sql().createQuery(blog)
               .where(blog.categoryId().eq(id))
                .select(blog.fetch(
                        BlogDetailView.class
                ))
                .fetchUnlimitedCount();
    }

    default List<Blog> getBlogsByTagId(BigInteger id) {
        return sql().createQuery(blog)
                .where(blog.tags(tagTable -> tagTable.id().eq(id)))
                .select(blog)
                .execute();
    }

    default List<BlogIdAndTitleView> getBlogIdAndTitle() {
        return sql().createQuery(blog)
                .select(blog.fetch(
                        BlogIdAndTitleView.class
                )).execute();
    }
}
