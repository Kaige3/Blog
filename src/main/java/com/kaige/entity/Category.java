package com.kaige.entity;

import org.babyfish.jimmer.sql.*;

import java.math.BigInteger;
import java.util.List;

/**
 * Entity for table "category"
 */
@Entity
@Table(name = "Kaige_blog.category")
public interface Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY
    )
    BigInteger id();

    /**
     * 分类名称
     */
    String categoryName();

    @OneToMany(mappedBy = "category")
    List<Blog> blogs();
}

