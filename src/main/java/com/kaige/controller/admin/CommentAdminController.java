package com.kaige.controller.admin;

import com.kaige.entity.Comment;
import com.kaige.entity.Result;
import com.kaige.entity.dto.BlogIdAndTitleView;
import com.kaige.entity.dto.CommentInput;
import com.kaige.service.BlogService;
import com.kaige.service.CommentService;
import com.kaige.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class CommentAdminController {

    @Autowired
    CommentService commentService;

    @Autowired
    BlogService blogService;

    /**
     * 分页查询评论列表
     */
    @GetMapping("/comments")
    public Result comments(@RequestParam(defaultValue = "")Integer page,
                           @RequestParam(defaultValue = "")BigInteger pageId,
                           @RequestParam(defaultValue = "1")Integer pageNum,
                           @RequestParam(defaultValue = "10")Integer pageSize){
       Page<Comment> commentPage = commentService.getCommentListOfPage(page,pageId,pageNum,pageSize);
        return Result.ok("请求成功",commentPage);
    }

    /**
     * 获取所有文章id和title
     */
    @GetMapping("/blogIdAndTitle")
    public Result blogIdAndTitle(){
         List<BlogIdAndTitleView> blogLists = blogService.getBlogIdAndTitle();
        return Result.ok("请求成功",blogLists);
    }

    /**
     * 更新评论公开状态
     */
    @PutMapping("/comment/published")
    public Result updatePublished(@RequestParam Integer id,@RequestParam Boolean published){
        commentService.updateCommentPublished(id,published);
        return Result.ok("请求成功");
    }

    /**
     * 更新评论邮件提醒状态
     */
    @PutMapping("/comment/notice")
    public Result updateNotice(@RequestParam Integer id,@RequestParam Boolean notice){
        commentService.updateCommentNotice(id,notice);
        return Result.ok("请求成功");
    }

    /**
     * 按照id删除评论
     */
    @DeleteMapping("/comment")
    public Result deleteComment(@RequestParam Integer id){
        commentService.deleteCommentById(id);
        return Result.ok("请求成功");
    }
    /**
     * 修改评论
     */
    @PutMapping("/comment")
    public Result updateComment(@RequestBody CommentInput commentInput){
        if (StringUtils.isEmpty(commentInput.getNickname(),commentInput.getAvatar(),commentInput.getEmail(),commentInput.getIp(),commentInput.getContent())){
            return Result.error("参数有误");
        }
        commentService.updateComment(commentInput);
        return Result.ok("请求成功");
    }
}
