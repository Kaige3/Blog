package com.kaige.entity;

import org.babyfish.jimmer.sql.Entity;
import org.babyfish.jimmer.sql.Id;
import org.babyfish.jimmer.sql.GeneratedValue;
import org.babyfish.jimmer.sql.GenerationType;

import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * Entity for table "user"
 */
@Entity
public interface User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY
    )
    BigInteger id();

    /**
     * 用户名
     */
    String username();

    /**
     * 密码
     */
    String password();

    /**
     * 昵称
     */
    String nickname();

    /**
     * 头像地址
     */
    String avatar();

    /**
     * 邮箱
     */
    String email();

    /**
     * 创建时间
     */
    LocalDateTime createTime();

    /**
     * 更新时间
     */
    LocalDateTime updateTime();

    /**
     * 角色访问权限
     */
    String role();
}

