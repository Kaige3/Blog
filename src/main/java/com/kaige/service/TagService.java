package com.kaige.service;

import com.kaige.entity.Blog;
import com.kaige.entity.Tag;
import com.kaige.entity.dto.BlogDetailView;
import com.kaige.entity.dto.BlogInfoView;
import com.kaige.entity.dto.TagInput;
import com.kaige.entity.dto.TagView;
import org.babyfish.jimmer.Page;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

public interface TagService {
    Page<BlogDetailView> getBlogInfoListByTagNameAndIsPublished(String tagName, Integer pageNum);

    List<TagView> getTagList();

    Tag getTagById(BigInteger t);

    Tag getTagByName(String t);

    void saveTag(Tag produce);

    org.springframework.data.domain.Page<TagView> pageOfTags(Integer pageNum, Integer pageSize);

    void updateTag(TagInput tagInput);

    void deleteTagById(BigInteger id);
}
