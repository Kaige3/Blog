package com.kaige.service;

import com.kaige.entity.Friend;
import com.kaige.entity.SiteSetting;
import com.kaige.entity.vo.FriendInfoVo;
import org.springframework.data.domain.Page;

import java.math.BigInteger;
import java.util.List;

public interface FriendService {
    List<Friend> getFriendList();

    FriendInfoVo getSiteFriendInfo();

    void addViewsByNickname(String nickname);

    Page<Friend> getFriendListOfPage(Integer pageNum, Integer pageSize);

    void updateFriend(BigInteger id, Boolean isPublic);
}
