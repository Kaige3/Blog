package com.kaige.repository;

import com.kaige.entity.*;
import org.babyfish.jimmer.spring.repository.JRepository;
import org.babyfish.jimmer.spring.repository.support.SpringPageFactory;
import org.babyfish.jimmer.sql.ast.mutation.MutableUpdate;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface FriendRepository extends JRepository<Friend, BigInteger>, Tables {

    FriendTable friend = FriendTable.$;
    SiteSettingTable siteSettingTable = SiteSettingTable.$;
    default Page<Friend> getFriendListOfPage(Integer pageNum, Integer pageSize) {
         return sql().createQuery(friend)
                .select(friend.fetch(
                        FriendFetcher.$
                               .nickname()
                               .avatar()
                               .website()
                                .description()
                ))
                .fetchPage(pageNum-1, pageSize, SpringPageFactory.getInstance());
    }

    default void updateById(BigInteger id, Boolean isPublic) {
        sql().createUpdate(friend)
                .set(friend.Published(),isPublic)
                .where(friend.id().eq(id))
                .execute();
    }

    default void updateCommentEnabled(Boolean commentEnabled) {
        String i;
        if (commentEnabled){
            i = "1";
        }else {
            i = "0";
        }
        sql().createUpdate(siteSettingTable)
                .where(siteSettingTable.nameEn().eq("friendCommentEnabled"))
                .set(siteSettingTable.value(),i)
                .execute();
    }

    default void updateFriendInfoContent(String content) {
        sql().createUpdate(siteSettingTable)
               .where(siteSettingTable.nameEn().eq("friendInfoContent"))
               .set(siteSettingTable.value(),content)
               .execute();
    }
}
