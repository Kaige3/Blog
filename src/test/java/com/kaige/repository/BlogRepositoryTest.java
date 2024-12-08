package com.kaige.repository;

import com.kaige.entity.Blog;
import com.kaige.entity.BlogFetcher;
import com.kaige.entity.BlogTable;
import com.kaige.entity.dto.BLogViewsView;
import com.kaige.entity.dto.NewBlogView;
import com.kaige.entity.dto.RandomBlogView;
import org.babyfish.jimmer.sql.ast.Predicate;
import org.babyfish.jimmer.sql.ast.query.ConfigurableRootQuery;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigInteger;
import java.util.List;


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

    @Test
    void getNewBlogListByIsPublished() {
        List<NewBlogView> execute = blogRepository.sql().createQuery(blogTable)
                .where(blogTable.Published().eq(true))
                .orderBy(blogTable.createTime().desc())
                .select(blogTable.fetch(
                        NewBlogView.class
                ))
                .execute();
        for (NewBlogView newBlogView : execute) {
            if (!"".equals(newBlogView.getPassword())){
                newBlogView.setPassword("");
                newBlogView.setPrivacy(true);
            }else {
                newBlogView.setPrivacy(false);
            }
        }
        System.out.println(execute);
    }

    @Test
    void getRandomBlogList() {
        List<RandomBlogView> execute = blogRepository.sql().createQuery(blogTable)
                .where(blogTable.Published().eq(true))
                .where(blogTable.Recommend().eq(true))
                .orderBy(Predicate.sql("RAND()"))
                .select(blogTable.fetch(RandomBlogView.class))
                .limit(2)
                .execute();
        System.out.println(execute);
    }

    @Test
    void getBlogViewMap() {
        List<BLogViewsView> execute = blogRepository.sql().createQuery(blogTable)
                .select(blogTable.fetch(BLogViewsView.class))
                .execute();
        System.out.println(execute);
    }
}