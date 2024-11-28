package com.kaige.controller.web;

import com.kaige.constant.RedisKeyConstants;
import com.kaige.entity.*;
import com.kaige.entity.vo.FriendInfoVo;
import com.kaige.service.FriendService;
import com.kaige.service.RedisService;
import org.babyfish.jimmer.sql.JSqlClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class FriendController {

    @Autowired
    private FriendService friendService;


    @GetMapping("/friend")
    public Result friend() {

        List<Friend> friendList = friendService.getFriendList();
        FriendInfoVo setting = friendService.getSiteFriendinfo();

        Map<String, Object> map = new HashMap<>();
        map.put("friendList", friendList);
        map.put("friendInfo", setting);
        return Result.ok("获取成功",map);
    }
}
