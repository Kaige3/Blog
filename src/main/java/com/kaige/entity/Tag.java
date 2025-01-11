package com.kaige.entity;

import org.babyfish.jimmer.sql.*;

import javax.validation.constraints.Null;

import java.math.BigInteger;
import java.util.List;

/**
 * Entity for table "tag"
 */
@Entity
@Table(name = "kaige_blog.tag")
public interface Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY
    )
    BigInteger id();

    @Column(name = "tag_name")
    @Key
    String tagName();

    @ManyToMany(mappedBy = "tags")
    List<Blog> blogs();

    /**
     * 标签颜色（可选）
     */
    @Null
    String color();
}

