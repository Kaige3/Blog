package com.kaige.service;

import com.kaige.entity.Friend;
import com.kaige.entity.SiteSetting;
import com.kaige.entity.vo.FriendInfoVo;

import java.util.List;

public interface FriendService {
    List<Friend> getFriendList();

    FriendInfoVo getSiteFriendinfo();
}
