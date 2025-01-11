package com.kaige.entity;

import org.babyfish.jimmer.sql.*;

import java.math.BigInteger;
import java.util.List;

/**
 * Entity for table "category"
 */
@Entity
@Table(name = "kaige_blog.category")
public interface Category {

    @GeneratedValue(strategy = GenerationType.IDENTITY
    )
    @Id
    BigInteger id();

    /**
     * 分类名称
     */
    @Key
    String categoryName();

    @OneToMany(mappedBy = "category")
    List<Blog> blogs();
}

