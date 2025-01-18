package com.kaige.controller.admin;

import com.kaige.entity.Friend;
import com.kaige.entity.Result;
import com.kaige.entity.dto.FriendInput;
import com.kaige.entity.vo.FriendInfoVo;
import com.kaige.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.Map;

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

    /**
     * 保存友联
     */
    @PostMapping("/friend")
    public Result saveFriend(@RequestBody FriendInput friendInput){
        friendService.saveFriend(friendInput);
        return Result.ok("保存成功");
    }

    /**
     * 更新友联
     */
    @PutMapping("/friend")
    public Result updateFriend(@RequestBody FriendInput friendInput){
        friendService.updateFriendInfo(friendInput);
        return Result.ok("更新成功");
    }

    /**
     * 按id删除友联
     */
    @DeleteMapping("/friend")
    public Result deleteFriend(@RequestParam BigInteger id){
        friendService.deleteFriend(id);
        return Result.ok("删除成功");
    }

    /**
     * 获取站点友联信息
     */
    @GetMapping("/friendInfo")
    public Result getFriendInfo(){
        FriendInfoVo siteFriendInfo = friendService.getSiteFriendInfo();
        return Result.ok("请求成功",siteFriendInfo);
    }

    /**
     * 开放或者关闭站点评论权
     */
    @PutMapping("/friendInfo/commentEnabled")
    public Result updateCommentEnabled(@RequestParam Boolean commentEnabled){
        friendService.updateCommentEnabled(commentEnabled);
        return Result.ok("更新成功");
    }

    /**
     * 修改友联信息页面
     */
    @PutMapping("/friendInfo/content")
    public Result updateFriendInfo(@RequestBody Map map){
        friendService.updateFriendInfoContent((String)map.get("content"));
        return Result.ok("更新成功");
    }
}
