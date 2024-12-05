package com.kaige;

import com.kaige.entity.Comment;
import com.kaige.entity.CommentFetcher;
import com.kaige.entity.CommentTable;
import org.babyfish.jimmer.sql.JSqlClient;
import org.babyfish.jimmer.sql.ast.table.Table;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class KaigeBlogApplicationTests {

    private final JSqlClient jSqlClient;

    @Autowired
    KaigeBlogApplicationTests(JSqlClient jSqlClient) {
        this.jSqlClient = jSqlClient;
    }

    CommentTable commentTable = CommentTable.$;

    @Test
    void contextLoads() {
        List<Comment> execute = jSqlClient.createQuery(commentTable)
                .where(commentTable.Published().eq(true))
                .where(commentTable.parentId().isNull())
                .select(commentTable.fetch(
                        CommentFetcher.$
                                .recursiveChildComment()
                )).execute();
        System.out.println(execute);

    }

}
