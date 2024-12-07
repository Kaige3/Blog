package com.kaige;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import com.kaige.entity.*;
import com.kaige.entity.dto.CommentInput;
import org.babyfish.jimmer.sql.JSqlClient;
import org.babyfish.jimmer.sql.ast.mutation.SimpleSaveResult;
import org.babyfish.jimmer.sql.ast.table.Table;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.Date;
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
//    @Transactional
    void contextLoads() {
//        Comment comment = jSqlClient.createQuery(commentTable)
//                .where(commentTable.Published().eq(true))
//                .where(commentTable.id().eq(BigInteger.valueOf(1)))
//                .where(commentTable.parentId().isNull())
//                .select(commentTable.fetch(
//                        CommentFetcher.$
//                                .nickname()
//                )).fetchOneOrNull();
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
