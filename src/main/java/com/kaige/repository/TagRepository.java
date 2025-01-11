package com.kaige.repository;

import com.kaige.entity.Tables;
import com.kaige.entity.Tag;
import com.kaige.entity.TagFetcher;
import com.kaige.entity.TagTable;
import org.babyfish.jimmer.spring.repository.JRepository;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface TagRepository extends JRepository<Tag,Long>, Tables {

    TagTable tag = TagTable.$;
    default Tag getById(BigInteger t) {
        return sql().createQuery(tag)
                .where(tag.id().eq(t))
                .select(tag.fetch(
                        TagFetcher.$
                                .tagName()
                                .color()
                )).fetchOneOrNull();
    }

    default @Nullable Tag getTagByName(String t) {
        return sql().createQuery(tag)
               .where(tag.tagName().eq(t))
               .select(tag.fetch(
                        TagFetcher.$
                               .tagName()
                               .color()
                )).fetchOneOrNull();
    }
}
