package com.kaige.repository;

import com.kaige.entity.About;
import com.kaige.entity.AboutFetcher;
import com.kaige.entity.AboutTable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AboutRepositoryTest {

    @Autowired
    private AboutRepository aboutRepository;
    AboutTable about = AboutTable.$;

    @Test
    void getAboutCommentEnabled() {
        About about1 = aboutRepository.sql().createQuery(about)
                .where(about.nameEn().eq("commentEnabled"))
                .select(about.fetch(
                        AboutFetcher.$
                                .value()
                )).fetchOneOrNull();
        assert about1 != null;
        boolean equals = about1.value().equals("true");
        System.out.println(equals);
        System.out.println(about1.toString());
    }
}