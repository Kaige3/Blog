package com.kaige.entity;

import jakarta.annotation.Nullable;
import org.babyfish.jimmer.Formula;
import org.babyfish.jimmer.sql.*;
import org.babyfish.jimmer.sql.ast.Predicate;

import javax.validation.constraints.Null;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

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

//    返回以 query为中心的 21个字符
//    @Formula(dependencies = "content")
//    default String content2(String query){
//        int index = content().indexOf(query);
//        int start = Math.max(0,index - 10);
//        int end = Math.min(content().length(),index +query.length() + 10);
//        String substring = content().substring(start, end);
//        return substring;
//    }

//    @Formula(dependencies = "content")
//     default String content2(){
//        return Predicate.sql("regexp_like(%e,%v)",
//                it ->
//                it.expression())  // 传入正则表达式参数
//                .value("content", content()  // 传入 content 字段
//    }


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
     * 阅读时间
     */
    int readTime();

    /**
     * 文章分类
     */
    @ManyToOne
    @JoinColumn(name = "category_id",foreignKeyType = ForeignKeyType.FAKE)
    @Nullable
    Category category();
    /**
     * 文章标签
     */
    @ManyToMany
    @JoinTable(
            name = "blog_tag",
//            inverseJoinColumnName = "tag_id",
//            joinColumnName = "blog_id",
//            ,foreignKeyType = ForeignKeyType.FAKE
            joinColumns = {
                    @JoinColumn(name = "blog_id",foreignKeyType = ForeignKeyType.FAKE),
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "tag_id",foreignKeyType = ForeignKeyType.FAKE),
            }
    )
    List<Tag> tags();

    /**
     * 是否置顶
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
//    TODO 建立与用户的多对一关系
    BigInteger userId();
}

