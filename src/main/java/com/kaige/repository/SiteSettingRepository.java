package com.kaige.repository;

import com.kaige.entity.SiteSetting;
import com.kaige.entity.SiteSettingFetcher;
import com.kaige.entity.SiteSettingTable;
import com.kaige.entity.Tables;
import org.babyfish.jimmer.spring.repository.JRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface SiteSettingRepository extends JRepository<SiteSetting,Long>, Tables {

    SiteSettingTable table = SiteSettingTable.$;
    default List<SiteSetting> getList() {
        return sql().createQuery(table)
                .select(table.fetch(
                        SiteSettingFetcher.$
                                .allTableFields()
                )).execute();
    }

    default void deleteSiteSettingInfoById(BigInteger id) {
         sql().createDelete(table)
                .where(table.id().eq(id))
                .execute();
    }

    default String getWebTitleSuffix() {
        SiteSetting webTitleSuffix = sql().createQuery(table)
                .where(table.nameEn().eq("webTitleSuffix"))
                .select(table.fetch(
                        SiteSettingFetcher.$
                                .value()
                ))
                .fetchOneOrNull();
        return webTitleSuffix.value();
    }
}
