package com.kaige.repository;

import com.kaige.entity.SiteSetting;
import com.kaige.entity.SiteSettingFetcher;
import com.kaige.entity.SiteSettingTable;
import com.kaige.entity.Tables;
import org.babyfish.jimmer.spring.repository.JRepository;
import org.springframework.stereotype.Repository;

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
}
