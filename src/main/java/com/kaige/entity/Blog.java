package com.kaige.entity;

import org.babyfish.jimmer.sql.*;

import javax.validation.constraints.Null;

import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * Entity for table "blog"
 */
@Entity
@Table(name = "Kaige_blog.blog")
public interface Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY
    )
    BigInteger id();

    /**
     * 文章标题
     */
    String title();

    /**
     * 文章首图，用于随机文章展示
     */
    String firstPicture();

    /**
     * 文章正文
     */
    String content();

    /**
     * 描述
     */
    String description();

    /**
     * 公开或私密
     */
    @Column(name = "is_published")
    boolean Published();

    /**
     * 推荐开关
     */
    @Column(name = "is_recommend")
    boolean Recommend();

    /**
     * 赞赏开关
     */
    @Column(name = "is_appreciation")
    boolean Appreciation();

    /**
     * 评论开关
     */
    @Column(name = "is_comment_enabled")
    boolean CommentEnabled();

    /**
     * 创建事件
     */
    LocalDateTime createTime();


    /**
     * 更新事件
     */
    LocalDateTime updateTime();

    /**
     * 浏览次数
     */
    int views();

    /**
     * 文章字数
     */
    int words();

    /**
     * 阅读事件
     */
    int readTime();

    /**
     * 文章分类
     */
    int categoryId();

    /**
     * 是否指定
     */
    @Column(name = "is_top")
    boolean Top();

    /**
     * 文章密码
     */
    @Null
    String password();

    /**
     * 文章作者
     */
    @Null
    BigInteger userId();
}

