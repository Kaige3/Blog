package com.kaige.entity;

import org.babyfish.jimmer.sql.*;

import javax.validation.constraints.Null;

import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * Entity for table "moment"
 */
@Entity
@Table(name = "moment"
)
public interface Moment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY
    )
    BigInteger id();

    /**
     * 动态内容
     */
    @Key
    String content();

    /**
     * 创建时间
     */
    LocalDateTime createTime();

    /**
     * 点赞数量
     */
    @Null
    Integer likes();

    /**
     * 是否公开
     */
    @Column(name = "is_published")
    boolean Published();
}

