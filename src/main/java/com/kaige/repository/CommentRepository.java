package com.kaige.repository;

import com.kaige.entity.Comment;
import com.kaige.entity.CommentFetcher;
import com.kaige.entity.CommentTable;
import com.kaige.entity.Tables;
import com.kaige.entity.dto.CommentInput;
import org.babyfish.jimmer.spring.repository.JRepository;
import org.babyfish.jimmer.sql.ast.Expression;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface CommentRepository extends JRepository<Comment,Integer>, Tables {

    CommentTable commentTable = CommentTable.$;

    default List<Comment> getPageCommentList(Integer page, BigInteger blogId) {
//        BigInteger blogid1 = null;
//        if (blogId != null) {
//            blogid1 = BigInteger.valueOf(blogId);
//        }
        return  sql().createQuery(commentTable)
//                .whereIf( page == 0 && blogId !=null,commentTable.blogId().eq(blogid1))
//                .whereIf(commentTable.blogId().eqIf(blogId !=null, blogid1))
                .whereIf(blogId !=null && page == 0,commentTable.blogId().eq(blogId))
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
                                .AdminComment()
                                .parent(CommentFetcher.$.nickname())
                                .recursiveChildComment()
                )).execute();
    }

    default Integer getcountByPageAndIsPublished(Integer page, BigInteger blogId, Object o) {
//        BigInteger blogid1 = null;
//        if (blogId != null) {
//            blogid1 = BigInteger.valueOf(blogId);
//        }
        return Math.toIntExact(sql().createQuery(commentTable)
                .whereIf(o != null, commentTable.Published().eq(true))
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
}
