package com.kaige.service.impl;

import com.kaige.constant.RedisKeyConstants;
import com.kaige.entity.*;
import com.kaige.entity.dto.BlogDetailView;
import com.kaige.entity.dto.BlogInfoView;
import com.kaige.entity.dto.TagInput;
import com.kaige.entity.dto.TagView;
import com.kaige.handler.exception.NotFoundException;
import com.kaige.repository.TagRepository;
import com.kaige.service.RedisService;
import com.kaige.service.TagService;
import org.babyfish.jimmer.Page;
import org.babyfish.jimmer.sql.JSqlClient;
import org.babyfish.jimmer.sql.ast.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private RedisService redisService;
    @Autowired
    TagRepository tagRepository;

    private static final String orderBy = "is_top desc, create_time desc";
    private JSqlClient jSqlClient;

    public TagServiceImpl(JSqlClient jSqlClient) {
        this.jSqlClient = jSqlClient;
    }
    BlogTable blog = BlogTable.$;

    TagTable tagTable = TagTable.$;

    @Override
    public Page<BlogDetailView> getBlogInfoListByTagNameAndIsPublished(String tagName, Integer pageNum) {
        return  jSqlClient.createQuery(blog)
                .where(blog.tags(tagTableEx -> tagTableEx.tagName().eq(tagName)))
                .where(blog.Published().eq(true))
                .orderBy(Predicate.sql("%v", it -> it.value(orderBy)))
                .select(blog.fetch(BlogDetailView.class))
                .fetchPage(pageNum - 1, 10);
    }

    @Override
    public List<TagView> getTagList() {
        // 从redis中查询数据
        String tagColorListKey = RedisKeyConstants.TAG_COLOR_List;
        List<TagView> listByValues = redisService.getListByValues(tagColorListKey);
        if (listByValues!= null) {
            return listByValues;
        }
        // 否则，从数据库中查询数据，然后存入redis中
        List<TagView> execute = jSqlClient.createQuery(tagTable)
                .select(tagTable.fetch(TagView.class))
                .execute();
        redisService.saveListToValue(tagColorListKey,execute);
        return execute;
    }

    @Override
    public Tag getTagById(BigInteger t) {
        Tag tag = tagRepository.getById(t);
        if (tag == null) {
            throw new NotFoundException("标签不存在");
        }
        return tag;
    }

    @Override
    public Tag getTagByName(String t) {

        return tagRepository.getTagByName(t);
    }

    @Override
    public void saveTag(Tag produce) {
        try {
            tagRepository.save(produce);
        } catch (Exception e) {
            throw new RuntimeException("保存便签失败");
        }
    }

    @Override
    public org.springframework.data.domain.Page<TagView> pageOfTags(Integer pageNum, Integer pageSize) {
        try {
            return tagRepository.pageOfTags(pageNum,pageSize);
        } catch (Exception e) {
            throw new RuntimeException("分页查询失败");
        }
    }

    @Override
    public void updateTag(TagInput tagInput) {
        tagRepository.update(tagInput);
    }

    @Override
    public void deleteTagById(BigInteger id) {
        tagRepository.deleteById(id);
    }


}
