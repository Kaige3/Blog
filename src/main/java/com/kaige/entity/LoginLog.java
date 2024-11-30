package com.kaige.entity;

import org.babyfish.jimmer.sql.Entity;
import org.babyfish.jimmer.sql.Id;
import org.babyfish.jimmer.sql.GeneratedValue;

import javax.validation.constraints.Null;

import org.babyfish.jimmer.sql.GenerationType;

import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * Entity for table "login_log"
 */
@Entity
public interface LoginLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY
    )
    BigInteger id();

    /**
     * 用户名称
     */
    String username();

    /**
     * ip
     */
    @Null
    String ip();

    /**
     * ip来源
     */
    @Null
    String ipSource();

    /**
     * 操作系统
     */
    @Null
    String os();

    /**
     * 浏览器
     */
    @Null
    String browser();

    /**
     * 登录状态
     */
    @Null
    Boolean status();

    /**
     * 操作描述
     */
    @Null
    String description();

    /**
     * 登录时间
     */
    LocalDateTime createTime();

    /**
     * user-agent用户代理
     */
    @Null
    String userAgent();
}

