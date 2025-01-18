package com.kaige.repository;

import com.kaige.entity.Comment;
import com.kaige.entity.CommentFetcher;
import com.kaige.entity.CommentTable;
import com.kaige.entity.Tables;
import com.kaige.entity.dto.CommentInput;
import org.babyfish.jimmer.Page;
import org.babyfish.jimmer.spring.repository.JRepository;
import org.babyfish.jimmer.spring.repository.support.SpringPageFactory;
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

    default org.springframework.data.domain.Page<Comment> getCommentListOfPage(Integer page, BigInteger pageId, Integer pageNum, Integer pageSize) {
        return sql().createQuery(commentTable)
                .whereIf(page != null, () -> commentTable.page().eq(page))
                .whereIf(pageId != null && pageId.equals(BigInteger.valueOf(0)), () -> commentTable.blogId().eq(pageId))
                .where(commentTable.parentId().eq(-1))
                .orderBy(commentTable.createTime().desc())
                .select(commentTable.fetch(
                        CommentFetcher.$
                                .nickname()
                                .email()
                                .content()
                                .avatar()
                                .createTime()
                                .website()
                                .ip()
                                .Published()
                                .AdminComment()
                                .page()
                                .Notice()
                                .parent()
                                .qq()
                                .recursiveChildComment()
                )).fetchPage(pageNum - 1, pageSize, SpringPageFactory.getInstance());

    }

    default void updateCommentPublished(Integer id, Boolean published) {
        sql().createUpdate(commentTable)
                .where(commentTable.id().eq(id))
               .set(commentTable.Published(), published)
               .execute();
    }

    default List<Comment> getCommentListByParentId(Integer id) {
        return sql().createQuery(commentTable)
               .where(commentTable.parentId().eq(id))
                       .select(commentTable.fetch(
                               CommentFetcher.$
                                       .nickname()
                                       .email()
                                       .content()
                                       .avatar()
                                       .createTime()
                                       .website()
                                       .ip()
                                       .Published()
                                       .AdminComment()
                                       .page()
                                       .Notice()
                                       .parent()
                                       .qq()
                                       .recursiveChildComment()
               )).execute();


    }

    default void updateCommentNotice(Integer id, Boolean notice) {
        sql().createUpdate(commentTable)
               .where(commentTable.id().eq(id))
              .set(commentTable.Notice(), notice)
              .execute();
    }

    default void deleteCommentById(int id) {
        sql().createDelete(commentTable)
              .where(commentTable.id().eq(id))
              .execute();
    }
}
