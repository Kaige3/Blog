package com.kaige;

import com.kaige.entity.*;
import com.kaige.entity.dto.CommentInput;
import com.kaige.entity.dto.UserInput;
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

    @Test
    void test1() {
        UserTable user = UserTable.$;
        UserInput userInput = new UserInput();
        userInput.setUsername("Kaige");
        User user1 = jSqlClient.createQuery(user)
                .where(user.username().eq(userInput.getUsername()))
                .select(user.fetch(
                        UserFetcher.$
                                .allScalarFields()
                )).fetchOneOrNull();
        System.out.println(user1);
    }

}
