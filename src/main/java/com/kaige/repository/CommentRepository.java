package com.kaige.repository;

import com.kaige.entity.Comment;
import com.kaige.entity.Tables;
import org.babyfish.jimmer.spring.repository.JRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JRepository<Comment,Long>, Tables {
}
