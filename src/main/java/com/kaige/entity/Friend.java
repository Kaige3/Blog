package com.kaige.entity;

import jakarta.annotation.Nullable;
import lombok.Data;
import org.babyfish.jimmer.sql.*;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * Entity for table "friend"
 */
@Entity
@Table(name = "friend")
public interface Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY
    )
    BigInteger id();

    /**
     * 昵称
     */
    String nickname();

    /**
     * 描述
     */
    String description();

    /**
     * 站点
     */
    String website();

    /**
     * 头像
     */
    String avatar();

    /**
     * 公开或隐藏
     */
    @Column(name = "is_published")
    boolean Published();

    /**
     * 点击次数
     */
    Integer views();

    /**
     * 创建时间
     */
    LocalDateTime createTime();
}

