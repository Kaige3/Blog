package com.kaige.entity;

import org.babyfish.jimmer.sql.Entity;
import org.babyfish.jimmer.sql.Id;
import org.babyfish.jimmer.sql.GeneratedValue;

import javax.validation.constraints.Null;

import org.babyfish.jimmer.sql.GenerationType;

import java.math.BigInteger;

/**
 * Entity for table "about"
 */
@Entity
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
    Object value();
}

