package com.kaige.service;

import com.kaige.entity.Comment;
import com.kaige.entity.dto.CommentInput;
import org.babyfish.jimmer.Page;

import java.math.BigInteger;

public interface CommentService {
    Page<Comment> getPageCommentList(Integer pageNum,Integer page, BigInteger blogId,Integer pageSize);

    Integer getcountByPageAndIsPublished(Integer page, BigInteger blogId, Boolean isPublished);

    Comment getCommentById(Integer parentCommentId);

    void saveComment(CommentInput comment);

    void deleteCommentByBlogId(BigInteger id);

    org.springframework.data.domain.Page<Comment> getCommentListOfPage(Integer page, BigInteger pageId, Integer pageNum, Integer pageSize);

    void updateCommentPublished(Integer id, Boolean published);

    void updateCommentNotice(Integer id, Boolean notice);

    void deleteCommentById(Integer id);

    void updateComment(CommentInput commentInput);
}
