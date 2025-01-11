package com.kaige.controller.admin;

import com.kaige.entity.Friend;
import com.kaige.entity.Result;
import com.kaige.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequestMapping("/admin")
public class FriendAdminController {

    @Autowired
    FriendService friendService;

    /**
     * 获取友链列表
     */
    @GetMapping("/friends")
    public Result friends(@RequestParam(defaultValue = "1")Integer pageNum,@RequestParam(defaultValue = "10")Integer pageSize){
        Page<Friend> friendListOfPage = friendService.getFriendListOfPage(pageNum, pageSize);
        return Result.ok("请求成功",friendListOfPage);
    }

    /**
     * 更新友链公开状态
     */
    @PutMapping("/friends")
    public Result updateFriend(@RequestParam BigInteger id,@RequestParam Boolean isPublic){
        friendService.updateFriend(id,isPublic);
        return Result.ok("更新成功");
    }
}
