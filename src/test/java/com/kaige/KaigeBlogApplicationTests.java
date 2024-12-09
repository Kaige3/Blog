package com.kaige;

import com.kaige.entity.BlogTable;
import com.kaige.entity.Comment;
import com.kaige.entity.CommentTable;
import com.kaige.entity.dto.CommentInput;
import org.babyfish.jimmer.sql.JSqlClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigInteger;

@SpringBootTest
class KaigeBlogApplicationTests {

    private final JSqlClient jSqlClient;

    @Autowired
    KaigeBlogApplicationTests(JSqlClient jSqlClient) {
        this.jSqlClient = jSqlClient;
    }

    CommentTable commentTable = CommentTable.$;
    BlogTable blogTable = BlogTable.$;
    @Test
    void contextLoads() {

        CommentInput commentInput = new CommentInput();
        commentInput.setNickname("kaige");
        commentInput.setAdminComment(false);
        commentInput.setContent("测试");
        commentInput.setEmail("EMAIL");
        commentInput.setAvatar("/");
        commentInput.setPublished(true);
        commentInput.setPage(1);
        commentInput.setNotice(false);
        boolean modified = jSqlClient.save(
                commentInput
        ).isModified();
        System.out.println(modified);

    }

    @Test
    void test() {
        jSqlClient.deleteById(Comment.class,BigInteger.valueOf(9));
    }

}
