package com.kaige.repository;

import com.kaige.entity.Comment;
import com.kaige.entity.CommentFetcher;
import com.kaige.entity.CommentTable;
import com.kaige.entity.Tables;
import org.babyfish.jimmer.spring.repository.JRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface CommentRepository extends JRepository<Comment,Long>, Tables {

    CommentTable commentTable = CommentTable.$;

    default List<Comment> getPageCommentList(Integer page, Long blogId) {
//        BigInteger blogid1= (BigInteger)blogId;
        BigInteger blogid1 = null;
        if (blogId != null) {
            blogid1 = BigInteger.valueOf(blogId);
        }
        return  sql().createQuery(commentTable)
                .where(commentTable.page().eq(page))
                .whereIf(blogId !=null && page == 0,commentTable.blogId().eq(blogid1))
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
}
