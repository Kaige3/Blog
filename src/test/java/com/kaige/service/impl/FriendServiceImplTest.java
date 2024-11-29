package com.kaige.service.impl;

import com.kaige.exception.PersistenceException;
import com.kaige.service.FriendService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FriendServiceImplTest {

    @Autowired
    private FriendService friendService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Transactional  // 确保测试中发生的数据库操作会回滚
    public void testAddViewsByNickname_Success() {
        String nickname = "Bob";
        friendService.addViewsByNickname(nickname);

        // 查询数据库，检查 views 字段是否增加
        // 假设你有一个方法可以查询数据
        int views = getViewsByNickname(nickname);
        assertEquals(201, views);  // 初始值加 1
    }


    @Test
    @Transactional
    public void addViewsByNickname() {
        String nickname = "invalidNickname";

        // 使用不存在的昵称，模拟操作失败
        assertThrows(PersistenceException.class, () -> {
            friendService.addViewsByNickname(nickname);
        });
    }
    private int getViewsByNickname(String nickname) {
        // 你可以根据你的数据库操作方式，查询数据库中的 view 值
        // 例如使用 JPA 或者直接 SQL 查询
        String sql = "SELECT views FROM friend WHERE nickname = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{nickname}, Integer.class);
    }
}