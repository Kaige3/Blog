package com.kaige.entity;

import lombok.Data;
import org.babyfish.jimmer.sql.*;

import javax.validation.constraints.Null;

import java.math.BigInteger;

/**
 * Entity for table "about"
 */
@Entity
@Table(name = "Kaige_blog.about")
public interface About {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY
    )
    BigInteger id();

    @Null
    String nameEn();

    @Null
    String nameZh();

    @Null
    String value();
}

