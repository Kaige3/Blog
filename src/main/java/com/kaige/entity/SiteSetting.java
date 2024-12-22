package com.kaige.entity;

import org.babyfish.jimmer.sql.*;

import javax.validation.constraints.Null;

import java.math.BigInteger;

/**
 * Entity for table "site_setting"
 */
@Entity
@Table(name = "site_setting")
@Serialized
public interface SiteSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY
    )
    BigInteger id();

    @Null
    String nameEn();

    @Null
    @Column(name = "name_zh")
    String name();

    @Null
    String value();

    /**
     * 1基础设置，2页脚设置，3资料卡，4友联信息
     */
    @Null
    Integer type();
}

