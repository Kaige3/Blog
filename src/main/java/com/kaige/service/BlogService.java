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
}
