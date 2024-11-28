package com.kaige.service.impl;

import com.kaige.entity.*;
import com.kaige.service.TagService;
import org.babyfish.jimmer.Page;
import org.babyfish.jimmer.sql.JSqlClient;
import org.babyfish.jimmer.sql.ast.Predicate;
import org.springframework.stereotype.Service;

@Service
public class TagServiceImpl implements TagService {

    private static final String orderBy = "is_top desc, create_time desc";
    private JSqlClient jSqlClient;

    public TagServiceImpl(JSqlClient jSqlClient) {
        this.jSqlClient = jSqlClient;
    }
    @Override
    public Page<Blog> getBlogInfoListByTagNameAndIsPublished(String tagName, Integer pageNum) {
        BlogTable blog = BlogTable.$;
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
}
