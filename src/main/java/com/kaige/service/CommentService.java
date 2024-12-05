package com.kaige.service;

import com.kaige.entity.Comment;
import org.babyfish.jimmer.Page;

import java.math.BigInteger;
import java.util.List;

public interface CommentService {
    List<Comment> getPageCommentList(Integer page, Long blogId);
}
