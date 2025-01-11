package com.kaige.controller.web;

import com.kaige.entity.Result;
import com.kaige.entity.dto.BlogDetailView;
import com.kaige.service.TagService;
import org.babyfish.jimmer.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/apis")
public class TagController {
    @Autowired
    private TagService tagService;

    /**
     * 根据标签名查询公开文章列表
     */
    @GetMapping("/tag")
    public Result getBlogInfoListByTagNameAndIsPublished(@RequestParam String tagName, @RequestParam(defaultValue = "1") Integer pageNum) {
       Page<BlogDetailView> blogPage = tagService.getBlogInfoListByTagNameAndIsPublished(tagName, pageNum);
        return Result.ok("获取成功", blogPage);
    }


}
