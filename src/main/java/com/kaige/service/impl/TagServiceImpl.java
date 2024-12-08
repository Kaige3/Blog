package com.kaige.service.impl;

import com.kaige.entity.*;
import com.kaige.entity.dto.TagView;
import com.kaige.service.TagService;
import org.babyfish.jimmer.Page;
import org.babyfish.jimmer.sql.JSqlClient;
import org.babyfish.jimmer.sql.ast.Predicate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    private static final String orderBy = "is_top desc, create_time desc";
    private JSqlClient jSqlClient;

    public TagServiceImpl(JSqlClient jSqlClient) {
        this.jSqlClient = jSqlClient;
    }
    BlogTable blog = BlogTable.$;

    TagTable tagTable = TagTable.$;

    @Override
    public Page<Blog> getBlogInfoListByTagNameAndIsPublished(String tagName, Integer pageNum) {
            return jSqlClient.createQuery(blog)
                    .where(blog.tags(tagTableEx -> tagTableEx.tagName().eq(tagName)))
                .where(blog.Published().eq(true))
                .orderBy(Predicate.sql("%v",it->it.value(orderBy)))
                .select(blog.fetch(
                        BlogFetcher.$
                                .allScalarFields()
                                .category(
                                        CategoryFetcher.$
                                               .allScalarFields()
                                )
                                .tags(TagFetcher.$
                                      .allScalarFields()
                )))
                .fetchPage(pageNum-1,10);

    }

    @Override
    public List<TagView> getTagList() {
        //TODO 先从redis中查询数据，如果有，直接返回
        // 否则，从数据库中查询数据，然后存入redis中
        return jSqlClient.createQuery(tagTable)
                .select(tagTable.fetch(TagView.class))
                .execute();
    }


}
