package com.kaige.entity;

import org.babyfish.jimmer.Formula;
import org.babyfish.jimmer.sql.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.Null;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entity for table "comment"
 */
@Entity
public interface Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY
    )
    BigInteger id();

    /**
     * 昵称
     */
    String nickname();

    /**
     * 邮箱
     */
    String email();

    /**
     * 评论内容
     */
    String content();

    /**
     * 头像路径
     */
    String avatar();

    /**
     * 评论时间
     */
    @Null
    LocalDateTime createTime();

    /**
     * 评论者的ip地址
     */
    @Null
    String ip();

    /**
     * 公开或回收站
     */
    @Column(name = "is_published")
    boolean Published();

    /**
     * 博主回复
     */
    @Column(name = "is_admin_comment")
    boolean AdminComment();

    /**
     * 0普通文章 1关于我的页面 2友链页面
     */
    int page();

    /**
     * 接收邮件提醒
     */
    @Column(name = "is_notice")
    boolean Notice();

    /**
     * 所属的文章
     */
    @Null
    Integer blogId();

    /**
     * 父评论id,-1为根评论
     */
    int parentCommentId();

    /**
     * 个人网站
     */
    @Null
    String website();

    /**
     * 若nickname为QQ号，就把 nickname 和avatar 设置为QQ昵称 和 QQ头像，并将此字段设置为QQ号的备份
     */
    @Null
    String qq();

}

