package com.kaige.controller.web;

import com.kaige.entity.Blog;
import com.kaige.entity.Result;
import com.kaige.entity.Tag;
import com.kaige.entity.dto.CategoryView;
import com.kaige.entity.dto.NewBlogView;
import com.kaige.entity.dto.RandomBlogView;
import com.kaige.entity.dto.TagView;
import com.kaige.service.BlogService;
import com.kaige.service.CategoryService;
import com.kaige.service.SiteSettingService;
import com.kaige.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class HomeController {

    @Autowired
    SiteSettingService siteSettingService;
    @Autowired
    private BlogService blogService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    TagService tagService;

    /**
     * 获取站点信息配置，最新推荐博客，分类列表，随机博客
     */
    @GetMapping("/site")
    public Result site(){
        /**
         * 先从 SiteSetting 表中查询数据，按照type 分组，返回一个map
         * 接着，从blog, category,tag 中查询数据，put 到map 中
         */
        Map<String,Object> map = siteSettingService.getSiteInfo();
        List<NewBlogView> newBlogViews = blogService.getNewBlogListByIsPublished();
        List<CategoryView> categoryViews = categoryService.getCategoryNameList();
        List<TagView> tags = tagService.getTagList();
        List<RandomBlogView> randomBlogViews = blogService.getRandomBlogList();
        map.put("newBlogList",newBlogViews);
        map.put("categoryList",categoryViews);
        map.put("tagList",tags);
        map.put("randomBlogList",randomBlogViews);
        return Result.ok("获取成功",map);
    }
}
