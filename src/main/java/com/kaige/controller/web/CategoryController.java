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

    private JSqlClient jSqlClient;
    public CategoryController(JSqlClient jSqlClient) {
        this.jSqlClient = jSqlClient;
    }

    CategoryTable category = CategoryTable.$;

    /**
     * 测试数据模型 一对多 和 多对一 搭建正确
     * @return
     */
    @GetMapping("/categories")
    public Result test(){
        List<Category> execute = jSqlClient.createQuery(category)
                .select(category.fetch(
                        CategoryFetcher.$
                                .categoryName()
                                .blogs(BlogFetcher.$
                                        .allScalarFields())
                ))
                .execute();
        return Result.ok("获取成功",execute);
    }

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
