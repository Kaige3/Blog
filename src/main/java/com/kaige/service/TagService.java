package com.kaige.service;

import com.kaige.entity.Blog;
import com.kaige.entity.Tag;
import org.babyfish.jimmer.Page;
import org.springframework.stereotype.Service;

public interface TagService {
     Page<Blog> getBlogInfoListByTagNameAndIsPublished(String tagName, Integer pageNum);
}
