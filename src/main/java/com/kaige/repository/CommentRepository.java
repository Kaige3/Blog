package com.kaige.repository;

import com.kaige.entity.Comment;
import com.kaige.entity.CommentFetcher;
import com.kaige.entity.CommentTable;
import com.kaige.entity.Tables;
import org.babyfish.jimmer.spring.repository.JRepository;
import org.babyfish.jimmer.sql.ast.Expression;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface CommentRepository extends JRepository<Comment,Integer>, Tables {

    CommentTable commentTable = CommentTable.$;

    default List<Comment> getPageCommentList(Integer page, Long blogId) {
        BigInteger blogid1 = null;
        if (blogId != null) {
            blogid1 = BigInteger.valueOf(blogId);
        }
        return  sql().createQuery(commentTable)
//                .whereIf( page == 0 && blogId !=null,commentTable.blogId().eq(blogid1))
//                .whereIf(commentTable.blogId().eqIf(blogId !=null, blogid1))
                .whereIf(blogId !=null && page == 0,commentTable.blogId().eq(blogid1))
                .where(commentTable.page().eq(page))
                .where(commentTable.Published().eq(true))
                .where(commentTable.parentId().isNull())
                .select(commentTable.fetch(
                        CommentFetcher.$
                                .nickname()
                                .content()
                                .avatar()
                                .createTime()
                                .website()
                                .isAdminComment()
                                .parent(CommentFetcher.$.nickname())
                                .recursiveChildComment()
                )).execute();
    }

    default Integer getcountByPageAndIsPublished(Integer page, Long blogId, Object o) {
        BigInteger blogid1 = null;
        if (blogId != null) {
            blogid1 = BigInteger.valueOf(blogId);
        }
        return Math.toIntExact(sql().createQuery(commentTable)
                .whereIf(o != null, commentTable.Published().eq(true))
                .whereIf(page == 0 && blogId != 0, commentTable.blogId().eq(blogid1))
                .where(commentTable.page().eq(page))
                .select(commentTable)
                .fetchUnlimitedCount());


    }
}
