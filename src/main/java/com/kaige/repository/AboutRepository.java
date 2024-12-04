package com.kaige.repository;

import com.kaige.entity.About;
import com.kaige.entity.AboutFetcher;
import com.kaige.entity.AboutTable;
import com.kaige.entity.Tables;
import org.babyfish.jimmer.spring.repository.JRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AboutRepository extends JRepository<About,Long>, Tables {
    AboutTable about = AboutTable.$;

    default boolean getAboutCommentEnabled(){
        About about1 = sql().createQuery(about)
                .where(about.nameEn().eq("commentEnabled"))
                .select(about.fetch(
                        AboutFetcher.$
                                .value()
                )).fetchOneOrNull();
        assert about1 != null;
        return about1.value().equals("true");
    }
}
