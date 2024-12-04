package com.kaige.controller.web;

import com.kaige.entity.Result;
import com.kaige.enums.CommentOpenStateEnum;
import com.kaige.utils.TokenAndPasswordVerify;
import com.kaige.utils.comment.CommentUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommentController {

    @Autowired
    CommentUtils commentUtils;

    @GetMapping("/comments")
    public Result comments(@RequestParam Integer page,
                           @RequestParam(defaultValue = "") Long blogId,
                           @RequestParam(defaultValue = "1") Integer pageNum,
                           @RequestParam(defaultValue = "10")Integer pageSize,
                           @RequestHeader(value = "Authorization",defaultValue = "") String jwt){
        CommentOpenStateEnum commentOpenStateEnum = commentUtils.judgeCommentState(page, blogId);
        switch (commentOpenStateEnum){
            case NOT_FOUND -> {
                return Result.create(404,"该博客不存在");
            }
            case CLOSE -> {
                return Result.create(403,"评论功能已关闭");
            }
            case PASSWORD -> TokenAndPasswordVerify.judgeTokenAndPasswordIsOK(jwt,blogId);
        }
        return Result.ok("你好了");
    }


}
