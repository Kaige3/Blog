package com.kaige.repository;

import com.kaige.entity.Friend;
import com.kaige.entity.FriendFetcher;
import com.kaige.entity.FriendTable;
import com.kaige.entity.Tables;
import org.babyfish.jimmer.spring.repository.JRepository;
import org.babyfish.jimmer.spring.repository.support.SpringPageFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface FriendRepository extends JRepository<Friend, BigInteger>, Tables {

    FriendTable friend = FriendTable.$;
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
}
