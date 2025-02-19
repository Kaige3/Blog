package com.kaige.controller.web;

import com.kaige.constant.RedisKeyConstants;
import com.kaige.entity.*;
import com.kaige.entity.vo.FriendInfoVo;
import com.kaige.service.FriendService;
import com.kaige.service.RedisService;
import org.babyfish.jimmer.sql.JSqlClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/apis")
public class FriendController {

    @Autowired
    private FriendService friendService;


    /**
     * 获取友联列表和友联信息
     * @return
     */
    @GetMapping("/friends")
    public Result friend() {
        List<Friend> friendList = friendService.getFriendList();
//        TODO 思考实现方式 为什么需要两个参数  ,
        FriendInfoVo setting = friendService.getSiteFriendInfo();

        Map<String, Object> map = new HashMap<>();
        map.put("friendList", friendList);
        map.put("friendInfo", setting);
        return Result.ok("获取成功",map);
    }

    /**
     * 按照昵称增加友联浏览次数
     */
    @PostMapping("/friend")
    public Result addViews(@RequestParam String nickname){
        friendService.addViewsByNickname(nickname);
        return Result.ok("增加成功");
    }
}
