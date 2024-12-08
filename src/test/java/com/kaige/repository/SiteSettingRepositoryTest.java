package com.kaige.repository;

import com.kaige.entity.SiteSetting;
import com.kaige.entity.SiteSettingFetcher;
import com.kaige.entity.SiteSettingTable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SiteSettingRepositoryTest {
    @Autowired
    private SiteSettingRepository siteSettingRepository;

    SiteSettingTable table = SiteSettingTable.$;

    @Test
    void getList() {

        List<SiteSetting> execute = siteSettingRepository.sql().createQuery(table)
                .select(table.fetch(
                        SiteSettingFetcher.$
                                .allTableFields()
                )).execute();
        System.out.println(execute);
    }
}