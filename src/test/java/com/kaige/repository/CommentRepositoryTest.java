package com.kaige.repository;

import com.kaige.entity.Comment;
import com.kaige.entity.CommentTable;
import org.babyfish.jimmer.sql.JSqlClient;
import org.babyfish.jimmer.sql.ast.Expression;
import org.babyfish.jimmer.sql.ast.Predicate;
import org.babyfish.jimmer.sql.ast.tuple.Tuple2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigInteger;
import java.util.List;

import static org.babyfish.jimmer.sql.ast.Expression.rowCount;
import static org.babyfish.jimmer.sql.ast.Predicate.sql;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    CommentTable commentTable = CommentTable.$;

    @Test
    void getcountByPageAndIsPublished() {
        long l = commentRepository.sql().createQuery(commentTable)
                .where(commentTable.page().eq(2))
                .select(commentTable)
                .fetchUnlimitedCount();

//        int size = execute.size();
//        for (Object object :execute) {
//            System.out.println("当前的数"+object);
//        }
        System.out.println("有几条评论"+l);
    }
}