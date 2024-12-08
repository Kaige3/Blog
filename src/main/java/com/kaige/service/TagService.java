package com.kaige.service;

import com.kaige.entity.Blog;
import com.kaige.entity.Tag;
import com.kaige.entity.dto.TagView;
import org.babyfish.jimmer.Page;
import org.springframework.stereotype.Service;

import java.util.List;

public interface TagService {
     Page<Blog> getBlogInfoListByTagNameAndIsPublished(String tagName, Integer pageNum);

    List<TagView> getTagList();
}
