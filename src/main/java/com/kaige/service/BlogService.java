package com.kaige.service;

import com.kaige.entity.Blog;
import org.babyfish.jimmer.Page;

import java.util.Map;

public interface BlogService {
    Map<String, Object> getArchiveBlogAndCountByIsPublished();

    Page<Blog> getBlogListByCategoryName(String categoryName, Integer pageNum);

    Page<Blog> getBlogListByIsPublished(Integer pageNum);
}
