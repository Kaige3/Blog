package com.kaige.repository;

import com.kaige.entity.Comment;
import com.kaige.entity.CommentFetcher;
import com.kaige.entity.CommentTable;
import com.kaige.entity.Tables;
import com.kaige.entity.dto.CommentInput;
import org.babyfish.jimmer.Page;
import org.babyfish.jimmer.spring.repository.JRepository;
import org.babyfish.jimmer.sql.ast.Expression;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface CommentRepository extends JRepository<Comment,Integer>, Tables {

    CommentTable commentTable = CommentTable.$;


    default Integer getcountByPageAndIsPublished(Integer page, BigInteger blogId, Boolean isPublished) {

        return Math.toIntExact(sql().createQuery(commentTable)
                .whereIf(isPublished != null, commentTable.Published().eq(true))
                .whereIf(page == 0 && !blogId.equals(BigInteger.valueOf(0)), commentTable.blogId().eq(blogId))
                .where(commentTable.page().eq(page))
                .select(commentTable)
                .fetchUnlimitedCount());


    }

    default Comment getCommentById(Integer parentCommentId) {
        return sql().createQuery(commentTable)
                .where(commentTable.id().eq(parentCommentId))
                .select(commentTable.fetch(
                        CommentFetcher.$
                                .allTableFields()
                ))
                .fetchOneOrNull();
    }

    default boolean saveComment(CommentInput adminComment) {
        return sql().save(adminComment).isModified();
    }

    default Page<Comment> getPageCommentListOV(Integer pageNum, Integer page, BigInteger blogId,Integer pageSize) {
        return sql().createQuery(commentTable)
                .whereIf(blogId != null && page == 0, commentTable.blogId().eq(blogId))
                .where(commentTable.page().eq(page))
                .where(commentTable.Published().eq(true))
                .where(commentTable.parentId().eq(-1))
                .orderBy(commentTable.createTime().desc())
                .select(commentTable.fetch(
                        CommentFetcher.$
                                .nickname()
                                .content()
                                .avatar()
                                .createTime()
                                .website()
                                .AdminComment()
                                .parent(CommentFetcher.$.nickname())
                                .recursiveChildComment()
                )).fetchPage(pageNum - 1, pageSize);
    }

    default Integer deleteCommentByBlogId(BigInteger id) {
        return sql().createDelete(commentTable)
               .where(commentTable.blogId().eq(id))
               .execute();
    }
}
