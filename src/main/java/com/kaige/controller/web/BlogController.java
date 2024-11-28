package com.kaige.controller.web;

import com.kaige.entity.Blog;
import com.kaige.entity.Result;
import com.kaige.service.BlogService;
import org.babyfish.jimmer.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlogController {

    @Autowired
    private BlogService blogService;

    /**
     * 按照创建时间 分页查询博客简要信息
     */
    @GetMapping("/blogs")
    public Result getBlogListByIsPublished(@RequestParam(defaultValue = "1") Integer pageNum) {
        Page<Blog> blogPage = blogService.getBlogListByIsPublished(pageNum);
        return Result.ok("获取成功", blogPage);
    }
}
