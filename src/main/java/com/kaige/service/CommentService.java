package com.kaige.service;

import com.kaige.entity.Comment;
import com.kaige.entity.dto.CommentInput;
import org.babyfish.jimmer.Page;

import java.math.BigInteger;
import java.util.List;

public interface CommentService {
    List<Comment> getPageCommentList(Integer page, BigInteger blogId);

    Integer getcountByPageAndIsPublished(Integer page, BigInteger blogId, Object o);

    Comment getCommentById(Integer parentCommentId);

    void saveComment(CommentInput comment);
}
