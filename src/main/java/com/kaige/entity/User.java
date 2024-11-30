package com.kaige.entity;

import org.babyfish.jimmer.sql.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * Entity for table "user"
 */
@Entity
@Table(name = "user")
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

