package com.kaige.service;

import com.kaige.entity.Blog;
import com.kaige.entity.dto.*;
import org.babyfish.jimmer.Page;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public interface BlogService {
    Map<String, Object> getArchiveBlogAndCountByIsPublished();

    Page<BlogByCategoryView> getBlogListByCategoryName(String categoryName, Integer pageNum);

    Page<BlogInfoView> getBlogListByIsPublished(Integer pageNum);

    BlogDetailView getBlogByIdAndIsPublished(Long id);

    String getBlogPassword(BigInteger id);

    List<Blog> getSearchBlogListByQueryAndPublished(String trim);

    Boolean getCommentEnabledByBlogId(BigInteger blogId);

    Boolean getPublishedByBlogId(BigInteger blogId);

    List<NewBlogView> getNewBlogListByIsPublished();

    List<RandomBlogView> getRandomBlogList();

    void updateViewsToRedis(BigInteger id);


    org.springframework.data.domain.Page<BlogDetailView> getBlogList(String title, Integer categoryId, Integer pageNum, Integer pageSize);

//    void deleteBlogTagByBlogId(BigInteger id);

    void deleteBlogByBlogId(BigInteger id,List<BigInteger> TagIdList);

     List<Blog> getTagIdsByBlogId(BigInteger id);

    void deleteBlog(BigInteger id);

    void updateTop(BigInteger id, Boolean top);

    void updateRecommend(BigInteger id, Boolean recommend);

    void updatePublished(BigInteger id, Boolean published);

    BlogDetailView getBlogById(BigInteger id);

    void saveBlog(BlogInput blog);

    void saveBlogTag(BigInteger id, BigInteger id1);

    void updateBlog(BlogInput blog);

    long countBlogByCategoryId(BigInteger id);

    List<Blog> getBlogsByTagId(BigInteger id);

    List<BlogIdAndTitleView> getBlogIdAndTitle();
}
