package com.kaige.controller.web;

import com.kaige.entity.*;
import com.kaige.service.BlogService;
import org.babyfish.jimmer.Page;
import org.babyfish.jimmer.sql.JSqlClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoryController {

    @Autowired
    private BlogService blogService;

    /**
     * 根据分类名称获取 公开 文章列表
     */
    @GetMapping("/category")
    public Result getBlogListByCategoryName(@RequestParam String categoryName,
                                            @RequestParam(defaultValue = "1") Integer pageNum){
        Page<Blog> PageResult = blogService.getBlogListByCategoryName(categoryName,pageNum);
        return Result.ok("获取成功",PageResult);
    }


}
