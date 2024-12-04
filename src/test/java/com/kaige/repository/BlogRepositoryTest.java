package com.kaige.repository;

import com.kaige.entity.Blog;
import com.kaige.entity.BlogFetcher;
import com.kaige.entity.BlogTable;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BlogRepositoryTest {

    @Autowired
    private BlogRepository blogRepository;
    BlogTable blogTable = BlogTable.$;
    @Test
    void getCommentEnabledByBlogId() {
        @Nullable Blog exists = blogRepository.sql().createQuery(blogTable)
                .where(blogTable.id().eq(BigInteger.valueOf(3)))
                .select(blogTable.fetch(
                        BlogFetcher.$
                                .CommentEnabled()
                )).fetchOneOrNull();
        System.out.println(exists.CommentEnabled());
    }
}